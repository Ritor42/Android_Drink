package com.example.drink.data.repository

import androidx.lifecycle.LiveData
import com.example.drink.data.dao.ProfileDao
import com.example.drink.data.model.Profile

class ProfileRepository(private val profileDao: ProfileDao) {

    val all: LiveData<List<Profile>> = profileDao.getAll()

    suspend fun insert(profile: Profile) {
        profileDao.insert(profile)
    }

    suspend fun update(profile: Profile) {
        profileDao.update(profile)
    }

    suspend fun delete(profile: Profile) {
        profileDao.delete(profile)
    }
}