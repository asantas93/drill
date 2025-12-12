package com.arissantas.drill

fun normalizeDrill(description: String): String {
  val segments = description.split(",").map { it.trim() }.filter { it.isNotEmpty() }
  return segments.joinToString(", ")
}

fun applySuggestion(text: String, cursor: Int, suggestion: String): String {
  var cursorSegment = -1
  var prevChars = 0
  text.split(",").forEachIndexed { i, segment ->
    if (cursor <= prevChars + segment.length && cursorSegment == -1) {
      cursorSegment = i
    }
    prevChars += segment.length + 1
  }
  return text
      .split(",")
      .mapIndexed { i, segment ->
        if (cursorSegment == i) {
          suggestion
        } else {
          segment.trim()
        }
      }
      .filter { it.isNotEmpty() }
      .joinToString(", ") + ", "
}
