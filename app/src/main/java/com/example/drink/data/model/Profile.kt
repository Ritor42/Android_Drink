package com.example.drink.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import kotlin.math.roundToInt

@Entity
@Parcelize
data class Profile(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "age") val age: Int,
    @ColumnInfo(name = "weight") val weight: Int,
    @ColumnInfo(name = "unit") val unit: String
) : Parcelable {
    fun isUnitInKg(): Boolean {
        return this.unit == "KG, ML"
    }

    fun getKgWeight() : Int {
        return this.weight
    }

    fun getPoundWeight() : Int {
        return (this.weight * 2.20462262f).roundToInt()
    }

    companion object {
        fun isUnitInKg(text : String): Boolean {
            return text == "KG, ML"
        }

        fun getStoreWeightByKg(weight: Int): Int {
            return weight
        }

        fun getStoreWeightByPound(weight: Int): Int {
            return (weight / 2.20462262f).roundToInt()
        }

        fun convertKgToPound(weight: Int): Int {
            return (weight * 2.20462262f).roundToInt()
        }

        fun convertPoundToKg(weight: Int): Int {
            return (weight / 2.20462262f).roundToInt()
        }
    }
}