package com.arissantas.drill.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.zIndex
import kotlin.math.abs

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <I> DraggableList(
    list: List<I>,
    key: (I) -> Any,
    move: (from: Int, to: Int) -> Unit,
    content: @Composable (I, Modifier) -> Unit
) {
    var dragState by remember {
        mutableStateOf<Pair<Int, Float>?>(null)
    }
    val heights = remember {
        mutableStateMapOf<Int, Int>()
    }
    val targetMoveIndex by remember(dragState) {
        derivedStateOf {
            dragState?.let { (start, offset) ->
                if (heights.isNotEmpty()) {
                    getEndIndex(start, offset, heights)
                } else {
                    -1
                }
            } ?: -1
        }
    }
    LazyColumn {
        itemsIndexed(list, key = { _, el -> key(el) }) { i, el ->
            val isBeingDragged = dragState?.first == i
            val offset = when {
                targetMoveIndex == -1 -> 0f
                dragState?.first == i -> 0f
                i >= targetMoveIndex && i < dragState!!.first -> (heights[dragState!!.first]
                    ?: 0).toFloat()

                i <= targetMoveIndex && i > dragState!!.first -> -(heights[dragState!!.first]
                    ?: 0).toFloat()

                else -> 0f
            }
            val animatedOffset by animateFloatAsState(targetValue = offset)
            content(
                el,
                Modifier
                    .animateItem()
                    .onSizeChanged { heights[i] = it.height }
                    .pointerInput(Unit) {
                        detectDragGesturesAfterLongPress(
                            onDragStart = {
                                dragState = Pair(i, 0f)
                                println("drag $i")
                            },
                            onDragEnd = {
                                println("drag end")
                                dragState?.let { (start, offset) ->
                                    val end = getEndIndex(start, offset, heights)
                                    if (start != end) {
                                        move(start, end)
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
                        // todo: shift unmoving items for preview
                        translationY =
                            if (isBeingDragged) dragState?.second ?: 0f else animatedOffset
                        scaleX = if (isBeingDragged) 1.05f else 1f
                        scaleY = if (isBeingDragged) 1.05f else 1f
                    }
                    .zIndex(if (isBeingDragged) 1f else 0f)
            )
        }
    }
}

private fun getEndIndex(start: Int, dragOffset: Float, heights: Map<Int, Int>): Int {
    var toMove = dragOffset
    var pos = start
    while (toMove != 0f) {
        if (toMove > 0) {
            if (pos == heights.size - 1) {
                break
            }
            val nextHeight = heights[pos + 1]
            if (toMove * 2 > nextHeight!!) {
                pos++
            }
            toMove = (toMove - nextHeight).coerceAtLeast(0f)
        } else {
            if (pos == 0) {
                break
            }
            val nextHeight = heights[pos - 1]
            if (abs(toMove) * 2 > nextHeight!!) {
                pos--
            }
            toMove = (toMove + nextHeight).coerceAtMost(0f)
        }
    }
    return pos
}