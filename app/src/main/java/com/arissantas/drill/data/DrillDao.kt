package com.arissantas.drill.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.arissantas.drill.model.Drill

@Dao
interface DrillDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(drill: Drill)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAll(drills: List<Drill>)

    // fixme
    @Query("DELETE from drill where day = :day")
    suspend fun deleteDay(day: Long)

    @Transaction
    suspend fun replaceAll(day: Long, drills: List<Drill>) {
        // todo: day can be inconsistent
        deleteDay(day)
        insertAll(drills)
    }

    @Query("SELECT * from drill where day = :day order by i ASC")
    suspend fun getForDay(day: Long): List<Drill>
}