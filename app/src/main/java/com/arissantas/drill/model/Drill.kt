package com.arissantas.drill.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

// fixme: add ID, hide i from vm
@Entity(tableName = "drill", indices = [Index(value = ["day"])])
data class DbDrill(
    val day: Long,
    @PrimaryKey val createdAt: Long,
    val i: Int,
    val done: Boolean = false,
    val minutesStr: String = "",
    val description: String = "",
) {
  fun asUi(): Drill {
    return Drill(createdAt = createdAt, minutesStr = minutesStr, description = description)
  }
}

data class Drill(
    val createdAt: Long,
    val minutesStr: String = "",
    val description: String = "",
) {
  fun minutes(): Int {
    return try {
      minutesStr.toInt()
    } catch (_: NumberFormatException) {
      0
    }
  }

  fun asDb(day: Long, i: Int, done: Boolean): DbDrill {
    return DbDrill(
        createdAt = createdAt,
        done = done,
        minutesStr = minutesStr,
        description = description,
        i = i,
        day = day,
    )
  }
}
