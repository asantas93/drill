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

class DrillViewModel : ViewModel() {

    val drillDao = MainApplication.drillDatabase.drillDao()

    val drills: MutableState<List<Drill>?> = mutableStateOf(null)
    val pendingSaves = MutableSharedFlow<List<Drill>>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            drills.value = drillDao.getAll()
            pendingSaves.debounce(2000L).collectLatest {
                drillDao.replaceAll(it)
            }
        }
    }

    fun saveDrills() {
        drills.value?.let {
            viewModelScope.launch(Dispatchers.IO) {
                pendingSaves.emit(it)
            }
        }
    }

    fun addDrill() {
        drills.value = drills.value?.let { it.plus(Drill(id = it.size)) }
    }

    fun deleteDrill(drill: Drill) {
        drills.value = drills.value?.filter { it.id != drill.id }
        saveDrills()
    }

    fun updateDrill(drill: Drill) {
        drills.value = drills.value?.map {
            if (it.id == drill.id) {
                drill
            } else {
                it
            }
        }
        saveDrills()
    }
}
