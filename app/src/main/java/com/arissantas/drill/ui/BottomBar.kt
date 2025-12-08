package com.arissantas.drill.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arissantas.drill.model.Drill

@Composable
fun BottomBar(todo: List<Drill>?, done:  List<Drill>?) {
    if (todo != null && done != null) {
        val remaining = todo.sumOf { it.minutes() }
        val total = (todo + done).sumOf { it.minutes() }
        val suffix = if (total == 0) "??" else if (remaining == 0) " :)" else ""
        Row (verticalAlignment = Alignment.CenterVertically) {
            // fixme
            WeeklyProgress(
                completed = total - remaining,
                scheduled = remaining,
                goal = 60 * 7,
                day = 20436,
                modifier = Modifier.fillMaxHeight().weight(1f)
            )
            Text(
                text = "$remaining/${total}m left\ntoday$suffix",
                modifier = Modifier.padding(horizontal = 8.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
