package com.dicoding.edusafety.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [PengaduanEntity::class],
    version = 1,
    exportSchema = false
)

abstract class EduDatabase : RoomDatabase() {
    companion object {
        @Volatile
        private var INSTANCE: EduDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): EduDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    EduDatabase::class.java, "db_edusafety"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}