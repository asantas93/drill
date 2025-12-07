package com.arissantas.drill.ui

import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.zIndex

@Composable
fun <I> DraggableList(
    list: List<I>,
    move: (from: Int, to: Int) -> Unit,
    content: @Composable (I, Modifier) -> Unit
) {
    var dragState by remember {
        mutableStateOf<Triple<Int, Float, I?>?>(null)
    }
    Column {
        list.forEachIndexed { i, el ->
            val isBeingDragged = dragState?.first == i
            val verticalOffset = if (isBeingDragged) dragState?.second ?: 0f else 0f
            // fixme: calculate offset based on cumulative sum of heights
            // todo: animate move preview
            content(
                el,
                Modifier
                    .pointerInput(Unit) {
                        detectDragGesturesAfterLongPress(
                            onDragStart = {
                                dragState = Triple(i, 0f, el)
                                println("drag $i")
                            },
                            onDragEnd = {
                                println("drag end")
                                dragState?.let { (startIndex, offset, _) ->
                                    val itemHeight = size.height.toFloat()
                                    val finalOffset = offset + itemHeight / 2
                                    val finalIndex =
                                        (startIndex + (finalOffset / itemHeight).toInt())
                                            .coerceIn(0, list.size - 1)
                                    println("drag end $startIndex -> $finalIndex")
                                    if (startIndex != finalIndex) {
                                        move(startIndex, finalIndex)
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
    }
}