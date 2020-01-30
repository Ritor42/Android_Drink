package com.example.drink.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.drink.data.model.Profile

@Dao
interface ProfileDao {
    @Query("SELECT * FROM Profile")
    fun getAll(): LiveData<List<Profile>>

    @Query("SELECT * FROM Profile WHERE id = :id  LIMIT 1")
    fun findById(id: Int): Profile?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(event: Profile): Long

    @Update
    suspend fun update(event: Profile)

    @Delete
    suspend fun delete(event: Profile)

    @Query("DELETE FROM Profile")
    suspend fun deleteAll()
}