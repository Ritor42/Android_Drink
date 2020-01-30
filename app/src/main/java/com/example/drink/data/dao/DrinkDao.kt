package com.example.drink.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.drink.data.model.Drink

@Dao
interface DrinkDao {
    @Query("SELECT * FROM Drink ORDER BY datetime DESC")
    fun getAll(): LiveData<List<Drink>>

    @Query("SELECT * FROM Drink WHERE id = :id  LIMIT 1")
    fun findById(id: Int): Drink?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(event: Drink): Long

    @Update
    suspend fun update(event: Drink)

    @Delete
    suspend fun delete(event: Drink)

    @Query("DELETE FROM Drink")
    suspend fun deleteAll()
}