package com.example.drink.data.repository

import androidx.lifecycle.LiveData
import com.example.drink.data.dao.DrinkDao
import com.example.drink.data.model.Drink

class DrinkRepository(private val drinkDao: DrinkDao) {

    val all: LiveData<List<Drink>> = drinkDao.getAll()

    suspend fun insert(drink: Drink) {
        drinkDao.insert(drink)
    }

    suspend fun update(drink: Drink) {
        drinkDao.update(drink)
    }

    suspend fun delete(drink: Drink) {
        drinkDao.delete(drink)
    }
}