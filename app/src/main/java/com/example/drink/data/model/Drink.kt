package com.example.drink.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*
import kotlin.math.roundToInt

@Parcelize
@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Profile::class,
            parentColumns = ["id"],
            childColumns = ["profile_id"]
        )]
)
data class Drink(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "profile_id") val profileId: Int,
    @ColumnInfo(name = "amount") val amount: Int,
    @ColumnInfo(name = "datetime") val date: Long
) : Parcelable{
    fun getMlAmount() : Int {
        return this.amount
    }

    fun getOzAmount() : Int {
        return (this.amount / 29.5735296f).roundToInt()
    }

    companion object {
        fun getStoreAmountByMl(amount: Int): Int {
            return amount
        }

        fun getStoreAmountByOz(amount: Int): Int {
            return (amount * 29.5735296f).roundToInt()
        }
    }
}