package com.arissantas.drill.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.arissantas.drill.model.Drill

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrillEditor(
    drill: Drill,
    update: (Drill) -> Unit,
    delete: (Drill) -> Unit,
    dragModifier: Modifier = Modifier
) {
    Row(
        modifier = dragModifier.fillMaxWidth(),
    ) {
        Row(verticalAlignment = Alignment.Top, modifier = Modifier.weight(1f)) {
            Checkbox(
                modifier = Modifier
                    .size(32.dp)
                    .padding(start = 4.dp),
                checked = drill.done,
                onCheckedChange = { update(drill.copy(done = it)) },
            )
            CompactTextField(
                value = drill.minutesStr,
                onValueChange = { v ->
                    update(drill.copy(minutesStr = v.filter { it.isDigit() }))
                },
                textAlign = TextAlign.End,
                modifier = Modifier
                    .width(48.dp)
                    .padding(top = 4.dp),
                isError = drill.minutesStr.isNotEmpty() && !drill.minutesStr.isDigitsOnly(),
                suffix = { Text("m") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )
            Icon(
                imageVector = Icons.Default.DragHandle,
                contentDescription = "drag to reorder",
                modifier = Modifier.padding(end = 4.dp)
            )
            CompactTextField(
                value = drill.description,
                placeholder = {
                    Text(
                        text = "e.g. Bach 2 courante, mm. 1-8, half tempo",
                        fontStyle = FontStyle.Italic
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 4.dp),
                onValueChange = { update(drill.copy(description = it)) },
            )
        }
        Row(modifier = Modifier.padding(end = 4.dp, top = 4.dp)) {
            IconButton(
                onClick = { delete(drill) },
                modifier = Modifier.size(24.dp, 24.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "delete this task without completing it",
                )
            }
        }
    }
}
