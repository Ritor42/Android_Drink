package com.example.drink.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.drink.data.dao.DrinkDao
import com.example.drink.data.dao.ProfileDao
import com.example.drink.data.model.Drink
import com.example.drink.data.model.Profile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*

@Database(
    version = 1,
    exportSchema = false,
    entities = [Drink::class, Profile::class]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun drinkDao(): DrinkDao
    abstract fun profileDao(): ProfileDao

    private class AppDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    val drinkDao = database.drinkDao()
                    val profileDao = database.profileDao()

                    val profile = Profile(1, 22, 80, "KG, ML")
                    profileDao.insert(profile)

                    drinkDao.deleteAll()
                    for (x in 1 until 9) drinkDao.insert(
                        Drink(x, 1, 100 + x * 15, Date().time)
                    )
                }
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "drink_database"
                ).addCallback(AppDatabaseCallback(scope)).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}