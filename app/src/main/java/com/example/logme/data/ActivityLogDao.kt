package com.logmeapp.activity.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityLogDao {
    @Query("SELECT * FROM activity_logs ORDER BY timestamp DESC")
    fun getAllLogs(): Flow<List<ActivityLog>>
    
    @Query("SELECT * FROM activity_logs WHERE id = :id")
    suspend fun getLogById(id: Long): ActivityLog?
    
    @Query("SELECT * FROM activity_logs WHERE category = :category ORDER BY timestamp DESC")
    fun getLogsByCategory(category: String): Flow<List<ActivityLog>>
    
    @Query("SELECT * FROM activity_logs WHERE templateId = :templateId ORDER BY timestamp DESC")
    fun getLogsByTemplate(templateId: String): Flow<List<ActivityLog>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: ActivityLog): Long
    
    @Update
    suspend fun updateLog(log: ActivityLog)
    
    @Delete
    suspend fun deleteLog(log: ActivityLog)
    
    @Query("DELETE FROM activity_logs")
    suspend fun deleteAllLogs()
    
    @Query("SELECT COUNT(*) FROM activity_logs")
    fun getLogCount(): Flow<Int>
}
