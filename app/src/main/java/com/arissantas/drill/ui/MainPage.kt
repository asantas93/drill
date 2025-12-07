package com.arissantas.drill.ui

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arissantas.drill.model.Drill
import com.arissantas.drill.ui.theme.DrillTheme
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPage(
    todo: List<Drill>?,
    done: List<Drill>?,
    day: Long,
    setDay: (Long) -> Unit,
    deleteDrill: (Drill) -> Unit,
    updateDrill: (Drill) -> Unit,
    completeDrill: (Drill) -> Unit,
    uncompleteDrill: (Drill) -> Unit,
    moveTodo: (Int, Int) -> Unit,
    moveDone: (Int, Int) -> Unit,
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
                    BottomBar(todo = todo, done = done)
                }
            )
        }
    ) { innerPadding ->
        if (todo != null && done != null) {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
            ) {
                val hapticFeedback = LocalHapticFeedback.current

                val lazyListState = rememberLazyListState()
                val reorderableLazyListState =
                    rememberReorderableLazyListState(lazyListState) { from, to ->
                        moveTodo(from.index, to.index)

                        hapticFeedback.performHapticFeedback(HapticFeedbackType.SegmentFrequentTick)
                    }

                LazyColumn(
                    state = lazyListState,
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(todo, key = { it.createdAt }) {
                        ReorderableItem(
                            reorderableLazyListState,
                            key = it.createdAt
                        ) { isDragging ->
                            val elevation by animateDpAsState(if (isDragging) 4.dp else 0.dp)

                            Surface(shadowElevation = elevation) {
                                DrillEditor(
                                    drill = it,
                                    update = updateDrill,
                                    delete = deleteDrill,
                                    checkAction = completeDrill,
                                    done = false,
                                    dragModifier = Modifier.draggableHandle(
                                        onDragStarted = {
                                            hapticFeedback.performHapticFeedback(
                                                HapticFeedbackType.GestureThresholdActivate
                                            )
                                        },
                                        onDragStopped = {
                                            hapticFeedback.performHapticFeedback(
                                                HapticFeedbackType.GestureEnd
                                            )
                                        },
                                    )
                                )
                            }
                        }
                    }
                }
                // var list by remember { mutableStateOf(todo) }
                // val lazyListState = rememberLazyListState()
                // val reorderableLazyListState =
                //     rememberReorderableLazyListState(lazyListState) { from, to ->
                //         //moveDrill(from.index, to.index)
                //         list = list.toMutableList().apply {
                //             add(to.index, removeAt(from.index))
                //         }
                //     }
                // LazyColumn(state = lazyListState) {
                //     items(list, key = { it.description }) { drill ->
                //         ReorderableItem(
                //             reorderableLazyListState,
                //             key = { drill.description }) { isDragging ->
                //             val interactionSource = remember { MutableInteractionSource() }
                //             // Item content
                //             Row {
                //                 DrillEditor(
                //                     drill = drill,
                //                     update = updateDrill,
                //                     delete = deleteDrill,
                //                     dragModifier = Modifier.draggableHandle(),
                //                 )
                //             }
                //         }
                //     }
                // }
                IconButton(
                    onClick = { newDrill() },
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth(),
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "add new drill",
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
                    DrillEditor(
                        drill = it,
                        update = updateDrill,
                        delete = deleteDrill,
                        checkAction = uncompleteDrill,
                        done = true,
                        )
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
            todo = listOf(
                Drill(0, "25", "Eb scale, 3 octaves"),
                Drill(4, "15", ""),
            ),
            done = listOf(
                Drill(2, "30", "toreadors"),
                Drill(3, "30", "intermezzo"),
            ),
            day = 0,
            setDay = {},
            deleteDrill = {},
            updateDrill = {},
            completeDrill = { },
            uncompleteDrill = { },
            moveTodo = { _, _ ->},
            moveDone = { _, _ -> },
            newDrill = { },
        )
    }
}
