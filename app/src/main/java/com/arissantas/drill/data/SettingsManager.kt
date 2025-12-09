package com.arissantas.drill.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsManager(context: Context) {
  private val dataStore = context.dataStore

  companion object {
    val WEEKLY_GOAL_KEY = intPreferencesKey("weekly_goal_minutes")
    const val DEFAULT_WEEKLY_GOAL = 420
    val DEFAULT_DRILL_MINUTES = intPreferencesKey("default_drill_minutes")
    const val DEFAULT_DEFAULT_DRILL_MINUTES = 15
  }

  val weeklyGoalFlow =
      dataStore.data.map { preferences -> preferences[WEEKLY_GOAL_KEY] ?: DEFAULT_WEEKLY_GOAL }

  suspend fun setWeeklyGoal(minutes: Int) {
    dataStore.edit { settings -> settings[WEEKLY_GOAL_KEY] = minutes }
  }

  val defaultDrillMinutesFlow =
      dataStore.data.map { preferences ->
        preferences[DEFAULT_DRILL_MINUTES] ?: DEFAULT_DEFAULT_DRILL_MINUTES
      }

  suspend fun setDefaultDrillMinutes(minutes: Int) {
    dataStore.edit { settings -> settings[DEFAULT_DRILL_MINUTES] = minutes }
  }
}
