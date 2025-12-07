package com.arissantas.drill.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.arissantas.drill.model.DbDrill

@Database(entities = [DbDrill::class], version = 1)
abstract class DrillDb : RoomDatabase() {
    abstract fun drillDao(): DrillDao

    companion object {
        const val NAME = "Drill_DB"
    }
}
