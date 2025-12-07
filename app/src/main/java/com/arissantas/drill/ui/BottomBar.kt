package com.arissantas.drill.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arissantas.drill.model.Drill

@Composable
fun BottomBar(todo: List<Drill>?, done:  List<Drill>?) {
    if (todo != null && done != null) {
        val remaining = todo.sumOf { it.minutes() }
        val total = (todo + done).sumOf { it.minutes() }
        val suffix = if (total == 0) "??" else if (remaining == 0) " :)" else ""
        Text(
            text = "$remaining/${total}m remaining$suffix",
            modifier = Modifier.padding(horizontal = 8.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
