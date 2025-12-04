package com.arissantas.drill.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.arissantas.drill.model.Drill

@Dao
interface DrillDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(drill: Drill)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAll(drills: List<Drill>)

    @Delete
    suspend fun delete(drill: Drill)

    // fixme
    @Query("DELETE from drill")
    suspend fun deleteAll()

    @Transaction
    suspend fun replaceAll(drills: List<Drill>) {
        deleteAll()
        insertAll(drills)
    }

    @Query("SELECT * from drill order by id ASC")
    suspend fun getAll(): List<Drill>

    // @Query("UPDATE drill set done = :done, minutes = :minutes, description = :description where id = :id")
    // suspend fun update(id: Int, done: Boolean, minutes: Int, description: String)
    @Update
    suspend fun update(drill: Drill)
}