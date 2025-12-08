package com.arissantas.drill.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun BottomBar(
    scheduledToday: Int,
    completedToday:  Int,
    previouslyCompleted: Int,
    previouslyScheduledNotPassed: Int,
    day: Long,
    goal: Int,
) {
        val total = scheduledToday + completedToday
        val suffix = if (total == 0) "?" else if (scheduledToday == 0) " :)" else ""
        Row (verticalAlignment = Alignment.CenterVertically) {
            WeeklyProgress(
                completed = completedToday + previouslyCompleted,
                scheduled = scheduledToday + previouslyScheduledNotPassed,
                goal = goal,
                day = day,
                modifier = Modifier.fillMaxHeight().weight(1f)
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
