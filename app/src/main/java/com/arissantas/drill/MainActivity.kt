package com.arissantas.drill

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModelProvider
import com.arissantas.drill.model.Drill
import com.arissantas.drill.ui.theme.DrillTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val drillsVm = ViewModelProvider(this)[DrillViewModel::class.java]
        setContent {
            DrillTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DrillsPage(drillsVm)
                }
            }
        }
    }
}

@Composable
fun DrillEditor(drill: Drill, update: (Drill) -> Unit, delete: (Drill) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = drill.done,
                onCheckedChange = { update(drill.copy(done = it)) },
            )
            TextField(
                value = drill.minutesStr,
                placeholder = { Text(text = "10") },
                modifier = Modifier.width(70.dp),
                onValueChange = { v ->
                    // todo: save on loss of focus? or with delay/debounce? NO!
                    update(drill.copy(minutesStr = v.filter { it.isDigit() }))
                },
                isError = drill.minutesStr.isNotEmpty() && !drill.minutesStr.isDigitsOnly(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            TextField(
                value = drill.description,
                placeholder = { Text(text = "practice what?") },
                modifier = Modifier.width(200.dp), // fixme: relative width
                onValueChange = { update(drill.copy(description = it)) },
            )
        }
        Button(
            onClick = { delete(drill) },
        ) {
            Icon(
                Icons.Default.Close,
                contentDescription = "delete this task without completing it",
                Modifier.size(ButtonDefaults.IconSize)
            )
        }
    }
}

@Composable
fun DrillsPage(vm: DrillViewModel) {
    val drills = vm.drills
    DrillList(
        drills.value,
        updateDrill = vm::updateDrill,
        deleteDrill = vm::deleteDrill,
        newDrill = vm::newDrill
    )
}

@Composable
fun DrillList(
    drills: List<Drill>?,
    deleteDrill: (Drill) -> Unit,
    updateDrill: (Drill) -> Unit,
    newDrill: () -> Unit
) {
    if (drills != null) {
        val todo = drills.filter { !it.done }
        val done = drills.filter { it.done }
        Column {
            todo.forEach {
                DrillEditor(drill = it, update = updateDrill, delete = deleteDrill)
            }
            Button(
                onClick = { newDrill() },
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "delete this task without completing it",
                    Modifier.size(ButtonDefaults.IconSize)
                )
            }
            val remaining = todo.sumOf { it.minutes() }
            val total = drills.sumOf { it.minutes() }
            val suffix = if (remaining == 0) " :)" else ""
            done.forEach {
                DrillEditor(drill = it, update = updateDrill, delete = deleteDrill)
            }
            Text(text = "$remaining/${total}m remaining$suffix")
        }
    } else {
        CircularProgressIndicator(
            modifier = Modifier.width(64.dp),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DrillListPreview() {
    DrillTheme {
        DrillList(
            listOf(
                Drill(0, 0, false, "25", "Eb scale"),
                Drill(0, 1, true, "30", "toreadors"),
                Drill(0, 2, true, "30", "intermezzo"),
                Drill(0, 3, false, "10", ""),
            ), {}, {}, {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DrillPreview() {
    DrillTheme {
        DrillEditor(Drill(0, 0, false, "120", "Eb scale"), update = {}, delete = {})
    }
}
