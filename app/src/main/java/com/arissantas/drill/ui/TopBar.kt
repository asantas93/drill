package com.arissantas.drill.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TopBar(day: Long, setDay: (Long) -> Unit, navigateToSettings: () -> Unit) {
  Row(
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.fillMaxWidth(),
  ) {
    IconButton(onClick = {}) {
      Icon(Icons.Filled.Menu, contentDescription = "open user preferences", Modifier.size(28.dp))
    }
    DayPicker(day, setDay)
    IconButton(onClick = navigateToSettings) {
      Icon(
          Icons.Filled.Settings,
          contentDescription = "open user preferences",
          Modifier.size(24.dp),
      )
    }
  }
}
