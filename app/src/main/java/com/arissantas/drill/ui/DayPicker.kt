package com.arissantas.drill.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun DayPicker(day: Long, setDay: (Long) -> Unit) {
  Row(
      horizontalArrangement = Arrangement.Center,
      verticalAlignment = Alignment.CenterVertically,
  ) {
    IconButton(onClick = { setDay(day - 1) }) {
      Icon(
          Icons.AutoMirrored.Filled.ArrowBack,
          contentDescription = "go to previous day",
          Modifier.size(24.dp),
      )
    }
    val dayText =
        if (day == LocalDate.now().toEpochDay()) {
          "Today"
        } else {
          LocalDate.ofEpochDay(day).format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))
        }
    Text(text = dayText, modifier = Modifier.width(100.dp), textAlign = TextAlign.Center)
    IconButton(onClick = { setDay(day + 1) }) {
      Icon(
          Icons.AutoMirrored.Filled.ArrowForward,
          contentDescription = "go to next day",
          Modifier.size(24.dp),
      )
    }
  }
}
