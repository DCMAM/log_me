package com.logmeapp.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.logmeapp.activity.billing.PurchaseState
import com.logmeapp.activity.ui.screens.AddLogScreen
import com.logmeapp.activity.ui.screens.HomeScreen
import com.logmeapp.activity.ui.screens.PremiumScreen
import com.logmeapp.activity.ui.theme.LogmeTheme
import com.logmeapp.activity.viewmodel.ActivityLogViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: ActivityLogViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LogmeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LogmeApp(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun LogmeApp(viewModel: ActivityLogViewModel) {
    val navController = rememberNavController()
    val logs by viewModel.allLogs.collectAsState(initial = emptyList())
    val hasSubscription by viewModel.billingManager.hasActiveSubscription.collectAsState()
    val purchaseState by viewModel.billingManager.purchaseState.collectAsState()
    val unlockedTemplates by viewModel.unlockedPremiumTemplates.collectAsState()
    
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                logs = logs,
                hasSubscription = hasSubscription,
                onAddLog = {
                    navController.navigate("add_log")
                },
                onLogClick = { log ->
                    // You can add a detail screen here if needed
                },
                onPremiumClick = {
                    navController.navigate("premium")
                },
                onDeleteLog = { log ->
                    viewModel.deleteLog(log)
                }
            )
        }
        
        composable("add_log") {
            AddLogScreen(
                onSave = { log ->
                    viewModel.insertLog(log)
                    navController.popBackStack()
                },
                onBack = {
                    navController.popBackStack()
                },
                isTemplateUnlocked = { templateId ->
                    viewModel.isTemplateUnlocked(templateId)
                },
                onUnlockTemplate = {
                    navController.navigate("premium")
                }
            )
        }
        
        composable("premium") {
            PremiumScreen(
                hasActiveSubscription = hasSubscription,
                purchaseState = purchaseState,
                onBack = {
                    navController.popBackStack()
                },
                onPurchaseConsumable = {
                    (navController.context as? MainActivity)?.let { activity ->
                        viewModel.billingManager.purchaseConsumable(activity)
                    }
                },
                onPurchaseSubscription = {
                    (navController.context as? MainActivity)?.let { activity ->
                        viewModel.billingManager.purchaseSubscription(activity)
                    }
                },
                onResetPurchaseState = {
                    viewModel.billingManager.resetPurchaseState()
                }
            )
        }
    }
    
    // Handle purchase success
    LaunchedEffect(purchaseState) {
        if (purchaseState is PurchaseState.Success) {
            val products = (purchaseState as PurchaseState.Success).products
            products.forEach { productId ->
                // If it's a consumable, unlock a premium template
                if (productId == "premium_template_unlock") {
                    // Unlock the first locked premium template
                    val locked = listOf("premium_1", "premium_2", "premium_3")
                        .firstOrNull { !viewModel.isTemplateUnlocked(it) }
                    locked?.let { viewModel.unlockPremiumTemplate(it) }
                }
            }
        }
    }
}
