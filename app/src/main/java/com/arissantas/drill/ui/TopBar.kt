package com.arissantas.drill.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun TopBar(day: Long, setDay: (Long) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(
            onClick = { setDay(day - 1) }
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "go to previous day",
                Modifier.size(ButtonDefaults.IconSize)
            )
        }
        val dayText = if (day == LocalDate.now().toEpochDay()) {
            "Today"
        } else {
            LocalDate.ofEpochDay(day).format(
                DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)
            )
        }
        Text(text = dayText)
        IconButton(
            onClick = { setDay(day + 1) }
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "go to next day",
                Modifier.size(ButtonDefaults.IconSize)
            )
        }
    }
}