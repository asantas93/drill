package com.arissantas.drill

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.arissantas.drill.data.SettingsManager
import com.arissantas.drill.ui.MainScreen
import com.arissantas.drill.ui.Screen
import com.arissantas.drill.ui.SettingsScreen
import com.arissantas.drill.ui.SettingsViewModel
import com.arissantas.drill.ui.theme.DrillTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      DrillTheme {
        // A surface container using the 'background' color from the theme
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
          val context = LocalContext.current.applicationContext
          val settingsManager = SettingsManager(context)
          val svm = viewModel { SettingsViewModel(settingsManager) }
          val dvm = viewModel { DrillViewModel(settingsManager) }
          val navController = rememberNavController()

          NavHost(navController = navController, startDestination = Screen.Home.route) {
            composable(Screen.Home.route) {
              MainScreen(
                  todo = dvm.todo.value,
                  done = dvm.done.value,
                  updateDrill = dvm::updateDrill,
                  deleteDrill = dvm::deleteDrill,
                  moveTodo = dvm::moveTodo,
                  completeDrill = dvm::completeDrill,
                  uncompleteDrill = dvm::uncompleteDrill,
                  newDrill = dvm::newDrill,
                  setDay = dvm::changeDay,
                  day = dvm.day.value,
                  repeatPrevDay = dvm::repeatPrevDay,
                  goal = svm.weeklyGoal.collectAsState().value,
                  previouslyScheduledNotPassed = dvm.previouslyScheduledNotPassed.value,
                  previouslyCompleted = dvm.previouslyCompleted.value,
                  navigateToSettings = { navController.navigate(Screen.Settings.route) },
              )
            }
            composable(Screen.Settings.route) {
              SettingsScreen(
                  goBack = { navController.popBackStack() },
                  currentGoal = svm.weeklyGoal.collectAsState().value,
                  setGoal = svm::setWeeklyGoal,
                  defaultDrillMinutes = svm.defaultDrillMinutes.collectAsState().value,
                  setDefaultDrillMinutes = svm::setDefaultDrillMinutes,
              )
            }
          }
        }
      }
    }
  }
}
