package com.pab.nutritrack.api.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pab.nutritrack.data.AlarmData

@Database(
    entities = [AlarmData::class],
    version = 1,
    exportSchema = false
)

abstract class NutriTrackDatabase: RoomDatabase() {

    abstract fun alarmDao(): AlarmDao

    companion object {
        @Volatile
        private var INSTANCE: NutriTrackDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): NutriTrackDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    NutriTrackDatabase::class.java, "nutritrack_local_database"
                )
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}