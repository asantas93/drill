package com.arissantas.drill.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.arissantas.drill.model.DbDrill

@Dao
interface DrillDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(drill: DbDrill)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAll(drills: List<DbDrill>)

    @Query("DELETE from drill where day = :day")
    suspend fun deleteDay(day: Long)

    @Transaction
    suspend fun replaceAll(day: Long, drills: List<DbDrill>) {
        // todo: day can be inconsistent
        deleteDay(day)
        insertAll(drills)
    }

    @Query("SELECT * from drill where day = :day order by i ASC")
    suspend fun getForDay(day: Long): List<DbDrill>

    @Query("SELECT * from drill where day between :start and :end order by day, i ASC")
    suspend fun getForDays(start: Long, end: Long): List<DbDrill>
}