package com.arissantas.drill.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import kotlin.time.Duration.Companion.minutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressInfoScreen(
    scheduledToday: Int,
    completedToday: Int,
    previouslyCompleted: Int,
    previouslyScheduledNotPassed: Int,
    day: Long,
    goal: Int,
    modifier: Modifier = Modifier,
) {
  val completedAll = completedToday + previouslyCompleted
  val compPlusSched = completedAll + scheduledToday + previouslyScheduledNotPassed
  val dayOfWeek = LocalDate.ofEpochDay(day).dayOfWeek.value
  val dayGoal = dayOfWeek * goal / 7
  Column(
      modifier = modifier.fillMaxWidth().padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    Text(
        "Progress is tracked by week. Today is day $dayOfWeek of 7.",
        style = MaterialTheme.typography.bodyMedium,
    )
    val dayGoalText = buildAnnotatedString {
      append("$dayOfWeek/7 of your weekly goal is ${dayGoal}m. ")
      when {
        compPlusSched > dayGoal -> {
          append("You've planned ")
          withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append("${compPlusSched - dayGoal}m")
          }
          append(" over this.")
        }
        dayGoal > compPlusSched -> {
          append("Plan ")
          withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append("${dayGoal - compPlusSched}m")
          }
          append(" to reach this today.")
        }
        else -> append("You're on track for that today.")
      }
    }
    Text(dayGoalText, style = MaterialTheme.typography.bodyMedium)
    val weekText =
        when {
          completedAll > goal ->
              "You've exceeded your weekly goal by ${(completedAll - goal).minutes}."
          goal > completedAll ->
              "You have ${(goal - completedAll).minutes} of practice left this week."
          else -> "You've met your weekly goal."
        }
    Text(
        weekText,
        style = MaterialTheme.typography.bodyMedium,
    )
    Box(modifier = Modifier.height(8.dp))
  }
}
