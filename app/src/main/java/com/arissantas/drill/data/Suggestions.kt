package com.arissantas.drill.data

import java.util.TreeSet

class Suggestions {

  private val mostRecent = LinkedHashMap<String, Int>()
  private val segmentCounts = HashMap<String, Int>()
  private val segmentCountsByDrill = HashMap<String, HashMap<String, Int>>()
  private val mostUsed = TreeSet<Info>()
  private val mostUsedByDrill = HashMap<String, TreeSet<Info>>()

  companion object {
    private const val N = 3

    data class Info(val count: Int, val segment: String) : Comparable<Info> {
      override fun compareTo(other: Info): Int {
        return compareValuesBy(this, other, { it.count }, { it.segment })
      }
    }
  }

  fun suggest(prefix: String): List<String> {
    val segments = prefix.split(",").map { it.trimStart().lowercase() }
    val last = segments.last()
    if (segments.size == 1) {
      return mostRecent
          .reversed()
          .keys
          .asSequence()
          .filter { it.lowercase().contains(last) && it != last }
          .take(N)
          .toList()
    } else {
      val drill = segments.first()
      val highPriority: Sequence<String> =
          mostUsedByDrill[drill]?.reversed()?.asSequence()?.map { it.segment } ?: sequenceOf()
      val lowPriority: Sequence<String> = mostUsed.reversed().asSequence().map { it.segment }
      val used = segments.drop(1).dropLast(1).toSet()
      return (highPriority + lowPriority)
          .distinct()
          .filter {
            it.lowercase().contains(last.lowercase()) &&
                !used.contains(it.lowercase()) &&
                it != last
          }
          .take(N)
          .toList()
    }
  }

  fun add(description: String) {
    val segments = description.split(",").map { it.trim() }.filter { it.isNotEmpty() }
    if (segments.isEmpty()) {
      return
    }
    val drill = segments.first()
    val prev = mostRecent.remove(drill) ?: 0
    mostRecent[drill] = prev + 1
    segments.drop(1).forEach { segment ->
      val prevCt = segmentCounts.remove(segment) ?: 0
      segmentCounts[segment] = prevCt + 1
      val prevCtByDrill = segmentCountsByDrill[drill]?.remove(segment) ?: 0
      segmentCountsByDrill.getOrPut(drill) { HashMap() }[segment] = prevCtByDrill + 1
      mostUsed.remove(Info(prevCt, segment))
      mostUsed.add(Info(prevCt + 1, segment))
      mostUsedByDrill
          .getOrPut(drill) { TreeSet() }
          .let {
            it.remove(Info(prevCtByDrill, segment))
            it.add(Info(prevCtByDrill + 1, segment))
          }
    }
  }

  fun remove(description: String) {
    val segments = description.split(",").map { it.trim() }.filter { it.isNotEmpty() }
    if (segments.isEmpty()) {
      return
    }
    val drill = segments.first()
    val prev = mostRecent.remove(drill)!!
    if (prev > 1) {
      mostRecent[drill] = prev - 1
    }
    segments.drop(1).forEach { segment ->
      println("seg: '$segment'")
      val prevCt = segmentCounts.remove(segment)!!
      if (prevCt > 1) {
        segmentCounts[segment] = prevCt - 1
      }
      val prevCtByDrill = segmentCountsByDrill[drill]!!.remove(segment)!!
      if (prevCtByDrill > 1) {
        segmentCountsByDrill[drill]!![segment] = prevCtByDrill - 1
      }
      mostUsed.remove(Info(prevCt, segment))
      if (prevCt > 1) {
        mostUsed.add(Info(prevCt - 1, segment))
      }
      mostUsedByDrill[drill]!!.let {
        it.remove(Info(prevCtByDrill, segment))
        if (prevCtByDrill > 1) {
          it.add(Info(prevCtByDrill - 1, segment))
        }
      }
      if (mostUsedByDrill[drill]!!.isEmpty()) {
        mostUsedByDrill.remove(drill)
      }
    }
  }
}
