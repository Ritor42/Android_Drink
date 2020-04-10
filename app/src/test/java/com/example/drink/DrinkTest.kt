package com.example.drink

import com.example.drink.data.model.Drink
import org.junit.Assert
import org.junit.Test
import kotlin.math.roundToInt

class DrinkTest {
    @Test
    fun creation_isCorrect() {
        val drink = Drink(1,2, 3, 4)
        Assert.assertEquals(1, drink.id)
        Assert.assertEquals(2, drink.profileId)
        Assert.assertEquals(3, drink.amount)
        Assert.assertEquals(4, drink.date)
    }

    @Test
    fun getAmount_isCorrect() {
        val drink = Drink(1,2, 3, 4)
        Assert.assertEquals(3, drink.getMlAmount())
        Assert.assertEquals((3 / 29.5735296f).roundToInt(), drink.getOzAmount())
        Assert.assertEquals(3, Drink.getStoreAmountByMl(3))
        Assert.assertEquals((3 * 29.5735296f).roundToInt(), Drink.getStoreAmountByOz(3))
    }
}