package com.arissantas.drill.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "drill")
data class Drill(
    @PrimaryKey val id: Int,
    val done: Boolean = false,
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
}
