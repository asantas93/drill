package com.arissantas.drill

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import com.arissantas.drill.ui.theme.DrillTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DrillTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GreetingText(
                        message = "Happy Birthday Sam!",
                        from = "From Emma",
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun GreetingText(message: String, from: String, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Text(
            text = message,
            fontSize = 100.sp,
            lineHeight = 116.sp,
            textAlign = TextAlign.Center
        )
        Text(
            text = from,
            fontSize = 36.sp,
            modifier = Modifier
                .padding(16.dp)
                .align(alignment = Alignment.End)
        )
    }
}

data class Task(val done: Boolean, val minutes: Int, val description: String)

@Composable
fun TaskEditor(task: Task, setDone: (Boolean) -> Unit, setMinutes: (Int) -> Unit, setDescription: (String) -> Unit, delete: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            val numValid = remember { mutableStateOf(task.minutes >= 0) }
            Checkbox(
                checked = task.done,
                onCheckedChange = setDone,
            )
            TextField(
                value = task.minutes.toString(),
                modifier = Modifier.width(70.dp),
                onValueChange = {
                    numValid.value = it.isDigitsOnly()
                    if (numValid.value) {
                        setMinutes(it.toInt())
                    }
                },
                isError = !numValid.value,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            TextField(
                value = task.description,
                modifier = Modifier.width(200.dp), // fixme: relative width
                onValueChange = setDescription,
            )
        }
        Button(
            onClick = delete,
        ) {
            Icon(
                Icons.Default.Close,
                contentDescription = "delete this task without completing it",
                Modifier.size(ButtonDefaults.IconSize)
            )
        }
        // todo: end day, reschedule?
    }
}

@Composable
fun TaskList(tasks: List<Task>) {
    // todo previous day button
    Column {
        tasks.forEach {
            TaskEditor(task = it, setDone = {}, setMinutes = {}, setDescription = {}, delete = {})
        }
        val remaining = tasks.filter {!it.done}.sumOf { it.minutes }
        val total = tasks.sumOf { it.minutes }
        Text(text = if (remaining > 0) "$remaining/${total}m remaining" else "all done!" )
        Text(text = "plan 20m to stay on track this week") // +- , overachiever :)
    }
}

@Preview(showBackground = true)
@Composable
fun TaskListPreview() {
    DrillTheme {
        TaskList(
            listOf(
                Task(false, 25, "Eb scale"),
                Task(true, 30, "toreadors"),
                Task(true, 30, "intermezzo"),
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TaskPreview() {
    DrillTheme {
        TaskEditor(Task(false, 120,"Eb scale"), setDone = {}, setMinutes = {}, setDescription = {}, delete = {})
    }
}
