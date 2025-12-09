package com.arissantas.drill.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.time.LocalDate

@Composable
fun BottomBar(
    scheduledToday: Int,
    completedToday: Int,
    previouslyCompleted: Int,
    previouslyScheduledNotPassed: Int,
    day: Long,
    goal: Int,
) {
  val total = scheduledToday + completedToday
  val suffix = if (total == 0) "?" else if (scheduledToday == 0) " :)" else ""
  Row(verticalAlignment = Alignment.CenterVertically) {
    val dayOfWeek = LocalDate.ofEpochDay(day).dayOfWeek.value
    val dayGoal = dayOfWeek * goal / 7
    val dayGoalText = if (total >= dayGoal) "+${total - dayGoal}m" else "-${dayGoal - total}m"
    Row(verticalAlignment = Alignment.CenterVertically) {
      Text(
          text = dayGoalText,
          modifier = Modifier.padding(horizontal = 4.dp),
          style = MaterialTheme.typography.bodySmall,
          textAlign = TextAlign.Center,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
      )
      Icon(
          Icons.Outlined.Info,
          contentDescription = "view graphic explanation",
          Modifier.size(20.dp).padding(end = 4.dp),
      )
    }
    WeeklyProgress(
        completed = completedToday + previouslyCompleted,
        scheduled = scheduledToday + previouslyScheduledNotPassed,
        goal = goal,
        day = day,
        modifier = Modifier.fillMaxHeight().weight(1f),
    )
    Text(
        text = "$scheduledToday/${total}m left\ntoday$suffix",
        modifier = Modifier.padding(horizontal = 8.dp),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
  }
}
