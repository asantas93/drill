package com.arissantas.drill.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    currentGoal: Int,
    setGoal: (Int) -> Unit,
    defaultDrillMinutes: Int,
    setDefaultDrillMinutes: (Int) -> Unit,
    goBack: () -> Unit,
) {
  var goalText by remember(currentGoal) { mutableStateOf(currentGoal.toString()) }
  var durationText by
      remember(defaultDrillMinutes) { mutableStateOf(defaultDrillMinutes.toString()) }

  Column(modifier = Modifier.fillMaxSize().padding(16.dp).safeDrawingPadding()) {
    Text(
        "Settings",
        style = MaterialTheme.typography.headlineMedium,
        modifier = Modifier.padding(bottom = 16.dp),
    )

    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
      Text("Weekly Practice Goal (minutes)")
      OutlinedTextField(
          value = goalText,
          onValueChange = { newText ->
            // Allow only digits
            goalText = newText.filter { it.isDigit() }
            // Update the actual goal state if the text is a valid number
            newText.toIntOrNull()?.let { setGoal(it) }
          },
          modifier = Modifier.width(100.dp),
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
          singleLine = true,
      )
    }

    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
      Text("Default drill duration (minutes)")
      OutlinedTextField(
          value = durationText,
          onValueChange = { newText ->
            // Allow only digits
            durationText = newText.filter { it.isDigit() }
            // Update the actual goal state if the text is a valid number
            newText.toIntOrNull()?.let { setDefaultDrillMinutes(it) }
          },
          modifier = Modifier.width(100.dp),
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
          singleLine = true,
      )
    }
  }
}
