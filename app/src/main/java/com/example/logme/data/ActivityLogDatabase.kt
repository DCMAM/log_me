package com.logmeapp.activity.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ActivityLog::class], version = 1, exportSchema = false)
abstract class ActivityLogDatabase : RoomDatabase() {
    abstract fun activityLogDao(): ActivityLogDao

    companion object {
        @Volatile
        private var INSTANCE: ActivityLogDatabase? = null

        fun getDatabase(context: Context): ActivityLogDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ActivityLogDatabase::class.java,
                    "activity_log_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
