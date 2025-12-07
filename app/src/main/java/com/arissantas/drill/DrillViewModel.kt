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
import java.time.LocalDate
import java.time.LocalTime

class DrillViewModel : ViewModel() {

    val drillDao = MainApplication.drillDatabase.drillDao()

    val drills: MutableState<List<Drill>?> = mutableStateOf(null)
    val day = mutableStateOf(todayDay())
    val pendingSaves = MutableSharedFlow<List<Drill>>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            drills.value = drillDao.getForDay(day.value)
            pendingSaves.debounce(2000L).collectLatest {
                drillDao.replaceAll(day.value, it)
            }
        }
    }

    fun changeDay(day: Long) {
        this.day.value = day
        drills.value = null
        viewModelScope.launch(Dispatchers.IO) {
            drills.value = drillDao.getForDay(day)
        }
    }

    fun saveDrills() {
        drills.value?.let {
            viewModelScope.launch(Dispatchers.IO) {
                pendingSaves.emit(it)
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
        drills.value = drills.value?.let {
            it.plus(Drill(i = it.size, day = day.value, minutesStr = "15"))
        }
    }

    fun deleteDrill(drill: Drill) {
        drills.value =
            drills.value?.filter { it.i != drill.i }?.mapIndexed { i, d -> d.copy(i = i) }
        saveDrills()
    }

    fun updateDrill(drill: Drill) {
        drills.value = drills.value?.map {
            if (it.i == drill.i) {
                drill
            } else {
                it
            }
        }
        saveDrills()
    }

    fun moveDrill(start: Int, end: Int) {
        drills.value?.let { immutable ->
            val mutable = immutable.toMutableList()
            val moved = mutable.removeAt(start)
            mutable.add(end, moved)
            drills.value = mutable.mapIndexed { i, drill ->
                drill.copy(i = i)
            }
            saveDrills()
        }
    }
}
