package com.logmeapp.activity.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "activity_logs")
data class ActivityLog(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val timestamp: Long = System.currentTimeMillis(),
    val category: String,
    val templateId: String, // "basic", "premium_1", "premium_2", etc.
    val mood: String? = null,
    val location: String? = null,
    val tags: String? = null // comma-separated tags
)
