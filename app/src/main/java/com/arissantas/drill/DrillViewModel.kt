package com.arissantas.drill

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arissantas.drill.model.Drill
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import java.lang.System.currentTimeMillis
import java.time.LocalDate
import java.time.LocalTime

class DrillViewModel : ViewModel() {

    val drillDao = MainApplication.drillDatabase.drillDao()
    val dayDrillsCache = HashMap<Long, Pair<List<Drill>, List<Drill>>>()

    val todo: MutableState<List<Drill>?> = mutableStateOf(null)
    val done: MutableState<List<Drill>?> = mutableStateOf(null)
    val day = mutableStateOf(todayDay())
    val pendingSaves = MutableSharedFlow<Triple<Long, List<Drill>, List<Drill>>>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val drills = getDayDrills(day.value)
            todo.value = drills.first
            done.value = drills.second
            pendingSaves.debounce(2000L).collectLatest { (day, uiTodo, uiDone) ->
                val dbDrills = uiTodo.mapIndexed { i, drill ->
                    drill.asDb(day = day, i = i, done = false)
                } + uiDone.mapIndexed { i, drill ->
                    drill.asDb(day = day, i = i + uiTodo.size, done = true)
                }
                drillDao.replaceAll(day, dbDrills)
            }
        }
    }

    private suspend fun getDayDrills(day: Long): Pair<List<Drill>, List<Drill>> {
        if (dayDrillsCache.contains(day)) {
            return dayDrillsCache[day]!!
        } else {
            val drills = drillDao.getForDay(day)
            val pair = Pair(
                drills.filter { !it.done }.map { it.asUi() },
                drills.filter { it.done }.map { it.asUi() })
            dayDrillsCache.putIfAbsent(day, pair)
            return pair
        }
    }

    fun changeDay(day: Long) {
        this.day.value = day
        todo.value = null
        done.value = null
        viewModelScope.launch(Dispatchers.IO) {
            val drills = getDayDrills(day)
            todo.value = drills.first
            done.value = drills.second
        }
    }

    fun repeatPrevDay() {
        val prev = day.value - 1
        viewModelScope.launch(Dispatchers.IO) {
            val drills = getDayDrills(prev)
            val t = currentTimeMillis()
            todo.value = (drills.first + drills.second).mapIndexed { i, drill ->
                drill.copy(createdAt = t + i) // add i for uniqueness
            }
            saveDrills()
        }
    }

    fun saveDrills() {
        if (todo.value != null && done.value != null) {
            dayDrillsCache[day.value] = Pair(todo.value!!, done.value!!)
            viewModelScope.launch(Dispatchers.IO) {
                pendingSaves.emit(Triple(day.value, todo.value!!, done.value!!))
            }
        }
    }

    fun todayDay(): Long {
        val day = LocalDate.now().toEpochDay()
        return if (LocalTime.now().hour < 3) {
            day - 1
        } else {
            day
        }
    }

    fun newDrill() {
        todo.value = todo.value?.plus(Drill(createdAt = currentTimeMillis(), minutesStr = "15"))
    }

    fun deleteDrill(drill: Drill) {
        todo.value = todo.value?.filter { it.createdAt != drill.createdAt }
        done.value = done.value?.filter { it.createdAt != drill.createdAt }
        saveDrills()
    }

    fun updateDrill(drill: Drill) {
        done.value = done.value?.map { if (it.createdAt == drill.createdAt) drill else it }
        todo.value = todo.value?.map { if (it.createdAt == drill.createdAt) drill else it }
        saveDrills()
    }

    fun completeDrill(drill: Drill) {
        todo.value = todo.value!!.filter { it.createdAt != drill.createdAt }
        done.value = listOf(drill) + done.value!!
        saveDrills()
    }

    fun uncompleteDrill(drill: Drill) {
        done.value = done.value!!.filter { it.createdAt != drill.createdAt }
        todo.value = todo.value!! + drill
        saveDrills()
    }

    fun moveTodo(from: Int, to: Int) {
        moveDrill(todo, from, to)
    }

    private fun moveDrill(list: MutableState<List<Drill>?>, from: Int, to: Int) {
        list.value?.let {
            list.value = it.toMutableList().apply {
                add(to, removeAt(from))
            }
            saveDrills()
        }
    }
}
