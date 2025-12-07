package com.arissantas.drill

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModelProvider
import com.arissantas.drill.model.Drill
import com.arissantas.drill.ui.theme.DrillTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompactTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start,
    isError: Boolean = false,
    suffix: (@Composable () -> Unit)? = null,
    placeholder: (@Composable () -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
) {
    val interactionSource = remember { MutableInteractionSource() }
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = textAlign,
        ),
        modifier = modifier,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        keyboardOptions = keyboardOptions,
    ) { innerTextField ->
        TextFieldDefaults.DecorationBox(
            interactionSource = interactionSource,
            innerTextField = innerTextField,
            value = value,
            enabled = true,
            contentPadding = PaddingValues(end = 8.dp),
            singleLine = false,
            suffix = suffix,
            placeholder = placeholder,
            container = {
                TextFieldDefaults.Container(
                    enabled = true,
                    interactionSource = interactionSource,
                    isError = isError,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = MaterialTheme.colorScheme.surface,
                    ),
                )
            },
            visualTransformation = VisualTransformation.None,
        )
    }
}

@Composable
fun DrillsPage(vm: DrillViewModel) {
    DrillList(
        vm.drills.value,
        updateDrill = vm::updateDrill,
        deleteDrill = vm::deleteDrill,
        moveDrill = vm::moveDrill,
        newDrill = vm::newDrill,
        setDay = vm::changeDay,
        day = vm.day.value
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrillList(
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
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IconButton(
                            onClick = { setDay(day - 1) }
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "go to previous day",
                                Modifier.size(ButtonDefaults.IconSize)
                            )
                        }
                        val dayText = if (day == LocalDate.now().toEpochDay()) {
                            "Today"
                        } else {
                            LocalDate.ofEpochDay(day).format(
                                DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)
                            )
                        }
                        Text(text = dayText)
                        IconButton(
                            onClick = { setDay(day + 1) }
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "go to next day",
                                Modifier.size(ButtonDefaults.IconSize)
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.height(60.dp),
                content = {
                    if (drills != null) {
                        val todo = drills.filter { !it.done }
                        val remaining = todo.sumOf { it.minutes() }
                        val total = drills.sumOf { it.minutes() }
                        val suffix = if (total == 0) "??" else if (remaining == 0) " :)" else ""
                        Text(
                            text = "$remaining/${total}m remaining$suffix",
                            modifier = Modifier.padding(horizontal = 8.dp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        if (drills != null) {
            val todo = drills.filter { !it.done }
            val done = drills.filter { it.done }
            val scrollState = rememberScrollState()
            var dragState by remember {
                mutableStateOf<Triple<Int, Float, Drill?>?>(null)
            }
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .verticalScroll(scrollState)
            ) {
                todo.forEachIndexed { i, drill ->
                    val isBeingDragged = dragState?.first == i
                    val verticalOffset = if (isBeingDragged) dragState?.second ?: 0f else 0f
                    // fixme: calculate offset based on cumulative sum of heights
                    // todo: animate move preview
                    DrillEditor(
                        drill = drill,
                        update = updateDrill,
                        delete = deleteDrill,
                        dragModifier = Modifier
                            .pointerInput(Unit) {
                                detectDragGesturesAfterLongPress(
                                    onDragStart = {
                                        dragState = Triple(i, 0f, drill)
                                        println("drag $i")
                                    },
                                    onDragEnd = {
                                        println("drag end")
                                        dragState?.let { (startIndex, offset, _) ->
                                            val itemHeight = size.height.toFloat()
                                            val finalOffset = offset + itemHeight / 2
                                            val finalIndex =
                                                (startIndex + (finalOffset / itemHeight).toInt())
                                                    .coerceIn(0, todo.size - 1)
                                            println("drag end $startIndex -> $finalIndex")
                                            if (startIndex != finalIndex) {
                                                moveDrill(startIndex, finalIndex)
                                            }
                                        }
                                        dragState = null
                                    },
                                    onDragCancel = {
                                        println("drag cancel")
                                        dragState = null
                                    },
                                    onDrag = { change, dragAmount ->
                                        println("drag $dragAmount ${dragState?.second}")
                                        change.consume()
                                        dragState =
                                            dragState?.copy(second = dragState!!.second + dragAmount.y)
                                    }
                                )
                            }
                            .graphicsLayer {
                                translationY = verticalOffset
                                scaleX = if (isBeingDragged) 1.05f else 1f
                                scaleY = if (isBeingDragged) 1.05f else 1f
                            }
                            .zIndex(if (isBeingDragged) 1f else 0f)
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
fun DrillListPreview() {
    DrillTheme {
        DrillList(
            listOf(
                Drill(0, 0, false, "25", "Eb scale, 3 octaves"),
                Drill(0, 1, true, "30", "toreadors"),
                Drill(0, 2, true, "30", "intermezzo"),
                Drill(0, 3, false, "15", ""),
            ), 0, setDay = {}, {}, {}, { _, _ -> }, {}
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
