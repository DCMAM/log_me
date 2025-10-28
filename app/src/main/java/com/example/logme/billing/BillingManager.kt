package com.logmeapp.activity.billing

import android.app.Activity
import android.content.Context
import android.util.Log
import com.android.billingclient.api.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class BillingManager(private val context: Context) : PurchasesUpdatedListener {
    
    private var billingClient: BillingClient? = null
    private val _purchaseState = MutableStateFlow<PurchaseState>(PurchaseState.Idle)
    val purchaseState: StateFlow<PurchaseState> = _purchaseState.asStateFlow()
    
    private val _ownedProducts = MutableStateFlow<Set<String>>(emptySet())
    val ownedProducts: StateFlow<Set<String>> = _ownedProducts.asStateFlow()
    
    private val _hasActiveSubscription = MutableStateFlow(false)
    val hasActiveSubscription: StateFlow<Boolean> = _hasActiveSubscription.asStateFlow()
    
    companion object {
        // Product IDs - Replace these with your actual product IDs from Google Play Console
        const val PREMIUM_TEMPLATE_CONSUMABLE = "premium_template_unlock"
        const val PREMIUM_SUBSCRIPTION = "premium_subscription_monthly"
        
        private const val TAG = "BillingManager"
    }
    
    init {
        setupBillingClient()
    }
    
    private fun setupBillingClient() {
        billingClient = BillingClient.newBuilder(context)
            .setListener(this)
            .enablePendingPurchases()
            .build()
        
        connectToBillingClient()
    }
    
    private fun connectToBillingClient() {
        billingClient?.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    Log.d(TAG, "Billing client connected")
                    queryPurchases()
                } else {
                    Log.e(TAG, "Billing client connection failed: ${billingResult.debugMessage}")
                }
            }
            
            override fun onBillingServiceDisconnected() {
                Log.d(TAG, "Billing client disconnected")
                // Retry connection
                connectToBillingClient()
            }
        })
    }
    
    fun queryPurchases() {
        billingClient?.let { client ->
            // Query for in-app products (consumables)
            client.queryPurchasesAsync(
                QueryPurchasesParams.newBuilder()
                    .setProductType(BillingClient.ProductType.INAPP)
                    .build()
            ) { billingResult, purchases ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    handlePurchases(purchases)
                }
            }
            
            // Query for subscriptions
            client.queryPurchasesAsync(
                QueryPurchasesParams.newBuilder()
                    .setProductType(BillingClient.ProductType.SUBS)
                    .build()
            ) { billingResult, purchases ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    handlePurchases(purchases)
                }
            }
        }
    }
    
    private fun handlePurchases(purchases: List<Purchase>) {
        val owned = mutableSetOf<String>()
        var hasSubscription = false
        
        for (purchase in purchases) {
            if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                // Acknowledge purchase if not yet acknowledged
                if (!purchase.isAcknowledged) {
                    acknowledgePurchase(purchase)
                }
                
                // Add to owned products
                purchase.products.forEach { productId ->
                    owned.add(productId)
                    if (productId == PREMIUM_SUBSCRIPTION) {
                        hasSubscription = true
                    }
                }
            }
        }
        
        _ownedProducts.value = owned
        _hasActiveSubscription.value = hasSubscription
    }
    
    private fun acknowledgePurchase(purchase: Purchase) {
        val params = AcknowledgePurchaseParams.newBuilder()
            .setPurchaseToken(purchase.purchaseToken)
            .build()
        
        billingClient?.acknowledgePurchase(params) { billingResult ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                Log.d(TAG, "Purchase acknowledged")
            }
        }
    }
    
    fun purchaseConsumable(activity: Activity) {
        val productList = listOf(
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(PREMIUM_TEMPLATE_CONSUMABLE)
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        )
        
        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(productList)
            .build()
        
        billingClient?.queryProductDetailsAsync(params) { billingResult, productDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && productDetailsList.isNotEmpty()) {
                val productDetails = productDetailsList[0]
                launchBillingFlow(activity, productDetails, BillingClient.ProductType.INAPP)
            } else {
                _purchaseState.value = PurchaseState.Error("Failed to load product details")
            }
        }
    }
    
    fun purchaseSubscription(activity: Activity) {
        val productList = listOf(
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(PREMIUM_SUBSCRIPTION)
                .setProductType(BillingClient.ProductType.SUBS)
                .build()
        )
        
        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(productList)
            .build()
        
        billingClient?.queryProductDetailsAsync(params) { billingResult, productDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && productDetailsList.isNotEmpty()) {
                val productDetails = productDetailsList[0]
                launchBillingFlow(activity, productDetails, BillingClient.ProductType.SUBS)
            } else {
                _purchaseState.value = PurchaseState.Error("Failed to load subscription details")
            }
        }
    }
    
    private fun launchBillingFlow(
        activity: Activity,
        productDetails: ProductDetails,
        productType: String
    ) {
        val productDetailsParamsList = if (productType == BillingClient.ProductType.SUBS) {
            val offerToken = productDetails.subscriptionOfferDetails?.get(0)?.offerToken ?: ""
            listOf(
                BillingFlowParams.ProductDetailsParams.newBuilder()
                    .setProductDetails(productDetails)
                    .setOfferToken(offerToken)
                    .build()
            )
        } else {
            listOf(
                BillingFlowParams.ProductDetailsParams.newBuilder()
                    .setProductDetails(productDetails)
                    .build()
            )
        }
        
        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(productDetailsParamsList)
            .build()
        
        billingClient?.launchBillingFlow(activity, billingFlowParams)
    }
    
    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: List<Purchase>?) {
        when (billingResult.responseCode) {
            BillingClient.BillingResponseCode.OK -> {
                purchases?.let {
                    for (purchase in it) {
                        handlePurchase(purchase)
                    }
                }
            }
            BillingClient.BillingResponseCode.USER_CANCELED -> {
                _purchaseState.value = PurchaseState.Cancelled
            }
            else -> {
                _purchaseState.value = PurchaseState.Error(billingResult.debugMessage)
            }
        }
    }
    
    private fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            // For consumable items, consume the purchase
            if (purchase.products.contains(PREMIUM_TEMPLATE_CONSUMABLE)) {
                consumePurchase(purchase)
            } else {
                // For subscriptions, just acknowledge
                if (!purchase.isAcknowledged) {
                    acknowledgePurchase(purchase)
                }
                _purchaseState.value = PurchaseState.Success(purchase.products)
            }
            
            queryPurchases()
        }
    }
    
    private fun consumePurchase(purchase: Purchase) {
        val consumeParams = ConsumeParams.newBuilder()
            .setPurchaseToken(purchase.purchaseToken)
            .build()
        
        billingClient?.consumeAsync(consumeParams) { billingResult, _ ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                _purchaseState.value = PurchaseState.Success(purchase.products)
                Log.d(TAG, "Purchase consumed successfully")
            } else {
                _purchaseState.value = PurchaseState.Error("Failed to consume purchase")
            }
        }
    }
    
    fun resetPurchaseState() {
        _purchaseState.value = PurchaseState.Idle
    }
    
    fun destroy() {
        billingClient?.endConnection()
    }
}

sealed class PurchaseState {
    object Idle : PurchaseState()
    data class Success(val products: List<String>) : PurchaseState()
    object Cancelled : PurchaseState()
    data class Error(val message: String) : PurchaseState()
}
