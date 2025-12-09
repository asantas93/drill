package com.arissantas.drill.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arissantas.drill.data.SettingsManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(private val settingsManager: SettingsManager) : ViewModel() {

  val weeklyGoal =
      settingsManager.weeklyGoalFlow.stateIn(
          scope = viewModelScope,
          started = SharingStarted.WhileSubscribed(5000),
          initialValue = SettingsManager.DEFAULT_WEEKLY_GOAL,
      )

  fun setWeeklyGoal(minutes: Int) {
    viewModelScope.launch { settingsManager.setWeeklyGoal(minutes) }
  }

  val defaultDrillMinutes =
      settingsManager.defaultDrillMinutesFlow.stateIn(
          scope = viewModelScope,
          started = SharingStarted.WhileSubscribed(5000),
          initialValue = SettingsManager.DEFAULT_DEFAULT_DRILL_MINUTES,
      )

  fun setDefaultDrillMinutes(minutes: Int) {
    viewModelScope.launch { settingsManager.setDefaultDrillMinutes(minutes) }
  }
}
