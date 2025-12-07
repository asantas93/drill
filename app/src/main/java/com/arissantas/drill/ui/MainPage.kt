package com.arissantas.drill.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arissantas.drill.model.Drill
import com.arissantas.drill.ui.theme.DrillTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPage(
    drills: List<Drill>?,
    day: Long,
    setDay: (Long) -> Unit,
    deleteDrill: (Drill) -> Unit,
    updateDrill: (Drill) -> Unit,
    moveDrill: (Int, Int) -> Unit,
    newDrill: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                expandedHeight = 40.dp,
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    TopBar(day, setDay)
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.height(60.dp),
                content = {
                    BottomBar(drills)
                }
            )
        }
    ) { innerPadding ->
        if (drills != null) {
            val todo = drills.filter { !it.done }
            val done = drills.filter { it.done }
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .verticalScroll(scrollState)
            ) {
                DraggableList(todo, moveDrill) { drill, modifier ->
                    DrillEditor(
                        drill = drill,
                        update = updateDrill,
                        delete = deleteDrill,
                        dragModifier = modifier,
                    )
                }
                IconButton(
                    onClick = { newDrill() },
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth(),
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "delete this task without completing it",
                            Modifier
                                .height(36.dp)
                                .padding(horizontal = 12.dp)
                        )
                        Text(
                            text = "add a drill...",
                            fontStyle = FontStyle.Italic,
                            modifier = Modifier.weight(1f),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
                done.forEach {
                    DrillEditor(drill = it, update = updateDrill, delete = deleteDrill)
                }
            }
        } else {
            CircularProgressIndicator(
                modifier = Modifier.width(64.dp),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPagePreview() {
    DrillTheme {
        MainPage(
            listOf(
                Drill(0, 0, false, "25", "Eb scale, 3 octaves"),
                Drill(0, 1, true, "30", "toreadors"),
                Drill(0, 2, true, "30", "intermezzo"),
                Drill(0, 3, false, "15", ""),
            ), 0, setDay = {}, {}, {}, { _, _ -> }, {}
        )
    }
}
