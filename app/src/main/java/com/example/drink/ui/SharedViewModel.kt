package com.example.drink.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.drink.data.AppDatabase
import com.example.drink.data.model.Drink
import com.example.drink.data.model.Profile
import com.example.drink.data.repository.DrinkRepository
import com.example.drink.data.repository.ProfileRepository
import kotlinx.coroutines.launch
import java.util.*
import kotlin.math.roundToInt

class SharedViewModel(application: Application) : AndroidViewModel(application) {
    private val drinkRepository: DrinkRepository
    private val profileRepository: ProfileRepository

    val drinks: LiveData<List<Drink>>
    val profiles: LiveData<List<Profile>>

    init {
        val database = AppDatabase.getDatabase(application, viewModelScope)
        drinkRepository = DrinkRepository(database.drinkDao())
        profileRepository = ProfileRepository(database.profileDao())

        drinks = drinkRepository.all
        profiles = profileRepository.all
    }

    fun insert(drink: Drink) {
        viewModelScope.launch {
            drinkRepository.insert(drink)
        }
    }

    fun delete(drink: Drink) {
        viewModelScope.launch {
            drinkRepository.delete(drink)
        }
    }

    fun update(drink: Drink) {
        viewModelScope.launch {
            drinkRepository.update(drink)
        }
    }

    fun update(profile: Profile) {
        viewModelScope.launch {
            profileRepository.update(profile)
        }
    }

    fun getProfileWeight(profile: Profile): Int {
        val isUnitInKg = profile.isUnitInKg()
        return if (isUnitInKg) {
            profile.getKgWeight()
        } else {
            profile.getPoundWeight()
        }
    }

    fun getStoreAmount(profile: Profile, amount: Int): Int {
        val isUnitInKg = profile.isUnitInKg()
        return if (isUnitInKg) {
            Drink.getStoreAmountByMl(amount)
        } else {
            Drink.getStoreAmountByOz(amount)
        }
    }

    fun getStoreWeight(isUnitInKg: Boolean, weight: Int): Int {
        return if (isUnitInKg) {
            Profile.getStoreWeightByKg(weight)
        } else {
            Profile.getStoreWeightByPound(weight)
        }
    }

    fun getDailyAmountText(profile: Profile): String {
        return this.getDailyAmount(profile).toString() + this.getWaterPostfix(profile.isUnitInKg())
    }

    fun getDailyGoalAmountText(profile: Profile): String {
        return this.getDailyGoalAmount(profile)
            .toString() + this.getWaterPostfix(profile.isUnitInKg())
    }

    fun getDailyLeftAmountText(profile: Profile): String {
        var leftAmount = this.getDailyGoalAmount(profile) - this.getDailyAmount(profile)
        leftAmount = if (leftAmount >= 0) leftAmount else 0
        return leftAmount.toString() + this.getWaterPostfix(profile.isUnitInKg()) + " left"
    }

    fun getTodayDrinks(drinks: List<Drink>): List<Drink> {
        val date = Date()
        date.hours = 0
        date.minutes = 0
        date.seconds = 0
        return drinks.filter { it.date >= date.time }
    }

    fun getProgress(profile: Profile): Int {
        val dailyAmount = this.getDailyAmount(profile)
        val dailyGoalAmount = this.getDailyGoalAmount(profile)
        val result = (dailyGoalAmount - dailyAmount) * 1f / dailyGoalAmount
        return if (result >= 0) ((1 - result) * 100).roundToInt() else 100
    }

    private fun getWeightPostfix(isUnitInKg: Boolean): String {
        return if (isUnitInKg) " kg" else " pound"
    }

    private fun getWaterPostfix(isUnitInMl: Boolean): String {
        return if (isUnitInMl) " ml" else " oz"
    }

    private fun getDailyAmount(profile: Profile): Int {
        val filteredDrinks = this.getTodayDrinks(drinks.value!!)
        return if (profile.isUnitInKg()) {
            filteredDrinks.sumBy { it.getMlAmount() }
        } else {
            filteredDrinks.sumBy { it.getOzAmount() }
        }
    }

    private fun getDailyGoalAmount(profile: Profile): Int {
        val weightDivider = 2.2f
        val ageMultiplier = if (profile.age < 30) 40 else if (profile.age <= 55) 35 else 30
        val goal = profile.getPoundWeight() / weightDivider * ageMultiplier / 28.3f
        return if (profile.isUnitInKg()) {
            (goal / 33.8f * 1000).toInt()
        } else {
            goal.toInt()
        }
    }
}