package com.arissantas.drill.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressInfoScreen(
    scheduledToday: Int,
    completedToday: Int,
    previouslyCompleted: Int,
    previouslyScheduledNotPassed: Int,
    day: Long,
    goal: Int,
) {
  Column(modifier = Modifier.fillMaxSize().padding(16.dp).safeDrawingPadding()) {
    Text(
        "Weekly Progress",
        style = MaterialTheme.typography.headlineMedium,
        modifier = Modifier.padding(bottom = 16.dp),
    )
    Text(
        "Your practice time is tracked by week. The first dark line represents how much time to practice to be on track at the end of the day. The second is for the whole week (it won't be visible if it's a Sunday).",
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(bottom = 16.dp),
    )
    Text(
        "The dark blue bar is how much practice you've completed this week. The light blue is how much you've scheduled today.",
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(bottom = 16.dp),
    )
    Text(
        "The number on the left is how your scheduled + completed practice time compares to the your end-of-day goal.",
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(bottom = 16.dp),
    )
    Text(
        "The numbers on the right show how much practice remains today, regardless of your goal.",
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(bottom = 16.dp),
    )

    BottomBar(
        scheduledToday = scheduledToday,
        completedToday = completedToday,
        previouslyCompleted = previouslyCompleted,
        previouslyScheduledNotPassed = previouslyScheduledNotPassed,
        day = day,
        goal = goal,
    )
  }
}
