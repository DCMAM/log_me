package com.logmeapp.activity.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.logmeapp.activity.billing.BillingManager
import com.logmeapp.activity.data.ActivityLog
import com.logmeapp.activity.data.ActivityLogDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ActivityLogViewModel(application: Application) : AndroidViewModel(application) {
    
    private val database = ActivityLogDatabase.getDatabase(application)
    private val dao = database.activityLogDao()
    val billingManager = BillingManager(application)
    
    val allLogs = dao.getAllLogs()
    val logCount = dao.getLogCount()
    
    private val _unlockedPremiumTemplates = MutableStateFlow<Set<String>>(emptySet())
    val unlockedPremiumTemplates: StateFlow<Set<String>> = _unlockedPremiumTemplates.asStateFlow()
    
    init {
        // Load unlocked templates from preferences or billing
        viewModelScope.launch {
            billingManager.hasActiveSubscription.collect { hasSubscription ->
                if (hasSubscription) {
                    // If user has subscription, unlock all premium templates
                    _unlockedPremiumTemplates.value = setOf(
                        "premium_1", "premium_2", "premium_3"
                    )
                }
            }
        }
    }
    
    fun insertLog(log: ActivityLog) {
        viewModelScope.launch {
            dao.insertLog(log)
        }
    }
    
    fun updateLog(log: ActivityLog) {
        viewModelScope.launch {
            dao.updateLog(log)
        }
    }
    
    fun deleteLog(log: ActivityLog) {
        viewModelScope.launch {
            dao.deleteLog(log)
        }
    }
    
    fun unlockPremiumTemplate(templateId: String) {
        val current = _unlockedPremiumTemplates.value.toMutableSet()
        current.add(templateId)
        _unlockedPremiumTemplates.value = current
    }
    
    fun isTemplateUnlocked(templateId: String): Boolean {
        return templateId == "basic" || 
               _unlockedPremiumTemplates.value.contains(templateId) ||
               billingManager.hasActiveSubscription.value
    }
    
    override fun onCleared() {
        super.onCleared()
        billingManager.destroy()
    }
}
