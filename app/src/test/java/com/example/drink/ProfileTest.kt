package com.example.drink

import com.example.drink.data.model.Profile
import org.junit.Assert
import org.junit.Test
import kotlin.math.roundToInt

class ProfileTest {
    @Test
    fun creation_isCorrect() {
        val profile = Profile(1,2, 3, "KG, ML")
        Assert.assertEquals(1, profile.id)
        Assert.assertEquals(2, profile.age)
        Assert.assertEquals(3, profile.weight)
        Assert.assertEquals("KG, ML", profile.unit)
    }

    @Test
    fun objectMethods_isCorrect() {
        val profile = Profile(1,2, 3, "KG, ML")
        Assert.assertEquals(true, profile.isUnitInKg())
        Assert.assertEquals(3, profile.getKgWeight())
        Assert.assertEquals((3 * 2.20462262f).roundToInt(), profile.getPoundWeight())
    }

    @Test
    fun staticMethods_isCorrect() {
        Assert.assertEquals(true, Profile.isUnitInKg("KG, ML"))
        Assert.assertEquals(1, Profile.getStoreWeightByKg(1))
        Assert.assertEquals((1 / 2.20462262f).roundToInt(), Profile.getStoreWeightByPound(1))
        Assert.assertEquals((1 * 2.20462262f).roundToInt(), Profile.convertKgToPound(1))
        Assert.assertEquals((1 / 2.20462262f).roundToInt(), Profile.convertPoundToKg(1))
    }
}