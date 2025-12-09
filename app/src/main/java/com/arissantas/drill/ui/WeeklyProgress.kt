package com.arissantas.drill.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arissantas.drill.ui.theme.DrillTheme
import java.time.LocalDate
import kotlin.math.max

@Composable
fun WeeklyProgress(
    completed: Int,
    scheduled: Int,
    goal: Int,
    day: Long,
    modifier: Modifier = Modifier.fillMaxWidth(.75f).height(24.dp),
) {
  val total = completed + scheduled
  val full = max(total, goal).toFloat()
  val dayOfWeek = LocalDate.ofEpochDay(day).dayOfWeek.value
  val dayGoal = dayOfWeek * goal / 7
  Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
    Box(modifier = Modifier.weight(1f)) {
      Row(
          modifier = Modifier.fillMaxWidth().fillMaxHeight(),
          verticalAlignment = Alignment.CenterVertically,
      ) {
        if (completed > 0) {
          Box(
              modifier =
                  Modifier.weight(completed.toFloat())
                      .fillMaxHeight(.7f)
                      .background(MaterialTheme.colorScheme.primaryContainer),
          )
        }
        if (scheduled > 0) {
          Box(
              modifier =
                  Modifier.weight(scheduled.toFloat())
                      .fillMaxHeight(.7f)
                      .background(MaterialTheme.colorScheme.secondaryContainer),
          )
        }
        if (full > total) {
          Box(
              modifier =
                  Modifier.weight(full - total)
                      .fillMaxHeight(.7f)
                      .background(MaterialTheme.colorScheme.surface),
          )
        }
      }
      if (dayGoal != goal) {
        Row(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
          Box(
              modifier =
                  Modifier.weight(dayGoal.toFloat())
                      .fillMaxHeight()
                      .border(
                          BorderStroke(1.dp, color = MaterialTheme.colorScheme.onSurfaceVariant),
                          shape = RoundedCornerShape(4.dp),
                      ),
          )
          if (full > 0 && full > dayGoal) {
            Box(modifier = Modifier.weight(full - dayGoal).fillMaxHeight())
          }
        }
      }
      if (full > goal) {
        Row(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
          Box(
              modifier =
                  Modifier.weight(goal.toFloat())
                      .fillMaxHeight()
                      .border(
                          BorderStroke(1.dp, color = MaterialTheme.colorScheme.onSurfaceVariant),
                          shape = RoundedCornerShape(4.dp),
                      )
          )
          Box(modifier = Modifier.weight(full - goal).fillMaxHeight())
        }
      }
      Row(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
        Box(
            modifier =
                Modifier.fillMaxWidth()
                    .fillMaxHeight()
                    .border(
                        BorderStroke(1.dp, color = MaterialTheme.colorScheme.onSurfaceVariant),
                        shape = RoundedCornerShape(4.dp),
                    )
                    .weight(goal.toFloat())
        )
        if (total > goal) {
          Box(modifier = Modifier.fillMaxWidth().fillMaxHeight().weight(total.toFloat() - goal))
        }
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun WeeklyProgressPreview() {
  DrillTheme {
    // monday
    WeeklyProgress(120, 60, 180 * 7, 20430)
  }
}

@Preview(showBackground = true)
@Composable
fun WeeklyProgressAllDonePreview() {
  DrillTheme {
    // monday
    WeeklyProgress(120, 0, 180 * 7, 20430)
  }
}

@Preview(showBackground = true)
@Composable
fun WeeklyProgressAllTodoPreview() {
  DrillTheme {
    // monday
    WeeklyProgress(0, 120, 180 * 7, 20430)
  }
}

@Preview(showBackground = true)
@Composable
fun WeeklyProgressEmptyPreview() {
  DrillTheme {
    // monday
    WeeklyProgress(0, 0, 180 * 7, 20430)
  }
}

@Preview(showBackground = true)
@Composable
fun WeeklyProgressExceedingPreview() {
  DrillTheme {
    // sunday
    WeeklyProgress(120, 60, 160, 20436)
  }
}
