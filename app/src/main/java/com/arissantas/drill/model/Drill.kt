package com.arissantas.drill.model

import androidx.room.Entity
import androidx.room.Index

@Entity(tableName = "drill", primaryKeys = ["day", "i"], indices = [Index(value = ["day"])])
data class Drill(
    val day: Long,
    val i: Int,
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

    fun key(): String {
        return "$day-$i"
    }
}
