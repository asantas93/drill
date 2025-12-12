package com.arissantas.drill

fun normalizeDrill(description: String): String {
  val segments = description.split(",").map { it.trim() }.filter { it.isNotEmpty() }
  return segments.joinToString(", ")
}
