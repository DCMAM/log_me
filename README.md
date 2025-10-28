# Logme - Activity Logger with In-App Purchases

A modern Android application for tracking daily activities with premium features unlockable through Google Play in-app purchases.

## Features

### Core Functionality
- **Activity Logging**: Create, view, edit, and delete activity logs
- **Categories**: Organize logs by Personal, Work, Health, Study, Social, or Other
- **Room Database**: Local storage for all your activity logs
- **Material Design 3**: Beautiful, modern UI with Jetpack Compose

### Templates
#### Basic Template (Free)
- Simple activity logging
- Title, description, and category
- Date and time tracking

#### Premium Templates (Requires Purchase/Subscription)
1. **Detailed Journal**
   - Mood tracking
   - Location logging
   - Perfect for personal journaling

2. **Professional Log**
   - Custom tags for organization
   - Ideal for work activities
   - Enhanced categorization

3. **Health Tracker**
   - Health status monitoring
   - Wellness journey tracking
   - Health-focused fields

### In-App Purchases

#### Consumable Item
- **Product ID**: `premium_template_unlock`
- **Type**: Consumable (can be purchased multiple times)
- **Purpose**: Unlock ONE premium template permanently
- **Use Case**: Users who want specific templates without subscribing

#### Subscription
- **Product ID**: `premium_subscription_monthly`
- **Type**: Auto-renewable subscription
- **Purpose**: Unlock ALL premium templates while subscription is active
- **Use Case**: Users who want full access to all features

## Setup Instructions

### 1. Prerequisites
- Android Studio (latest version recommended)
- Android SDK 24 or higher
- Google Play Console account (for publishing and in-app purchases)

### 2. Clone and Build
```bash
git clone <repository-url>
cd Logme
./gradlew build
```

### 3. Configure Google Play In-App Purchases

#### A. Create Products in Google Play Console

1. **Sign in to Google Play Console**
   - Go to https://play.google.com/console
   - Select your app (or create a new app)

2. **Create Consumable Product**
   - Navigate to: Monetize → Products → In-app products
   - Click "Create product"
   - Product ID: `premium_template_unlock`
   - Name: "Premium Template Unlock"
   - Description: "Unlock one premium activity log template"
   - Set your price (e.g., $2.99)
   - Click "Activate"

3. **Create Subscription**
   - Navigate to: Monetize → Products → Subscriptions
   - Click "Create subscription"
   - Product ID: `premium_subscription_monthly`
   - Name: "Premium Subscription"
   - Description: "Access all premium templates"
   - Base plans:
     - Add a monthly base plan
     - Set your price (e.g., $4.99/month)
   - Click "Activate"

#### B. Update Product IDs (if needed)

If you want to use different product IDs, update them in:

```kotlin
// app/src/main/java/com/example/logme/billing/BillingManager.kt

companion object {
    const val PREMIUM_TEMPLATE_CONSUMABLE = "your_consumable_id"
    const val PREMIUM_SUBSCRIPTION = "your_subscription_id"
}
```

#### C. Testing In-App Purchases

1. **Add Test Accounts**
   - In Google Play Console: Settings → License testing
   - Add Gmail accounts for testing
   - These accounts can make test purchases without being charged

2. **Create a Closed/Internal Test Track**
   - Go to Testing → Internal testing (or Closed testing)
   - Upload your APK/AAB
   - Add testers using the Gmail accounts from step 1

3. **Install and Test**
   - Install the app from the test track
   - Test purchases will work without real charges

### 4. Database Configuration

The app uses Room Database with the following structure:

```kotlin
@Entity(tableName = "activity_logs")
data class ActivityLog(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val timestamp: Long,
    val category: String,
    val templateId: String,
    val mood: String?,
    val location: String?,
    val tags: String?
)
```

No additional database configuration is required - Room handles everything automatically.

## Project Structure

```
app/src/main/java/com/example/logme/
├── MainActivity.kt                 # Main entry point with navigation
├── billing/
│   └── BillingManager.kt          # Google Play Billing integration
├── data/
│   ├── ActivityLog.kt             # Data model
│   ├── ActivityLogDao.kt          # Database queries
│   └── ActivityLogDatabase.kt     # Room database
├── ui/
│   ├── screens/
│   │   ├── HomeScreen.kt          # Main activity list screen
│   │   ├── AddLogScreen.kt        # Create/edit logs
│   │   └── PremiumScreen.kt       # Purchase screen
│   └── theme/                     # Material Design theme
└── viewmodel/
    └── ActivityLogViewModel.kt    # Business logic & state management
```

## Usage Guide

### For Users

#### Creating an Activity Log
1. Tap the **+** button on the home screen
2. Select a template (Basic is free, Premium requires purchase)
3. Fill in the title and description
4. Choose a category
5. Add optional fields based on the template
6. Tap "Save Activity Log"

#### Unlocking Premium Templates

**Option 1: Subscription**
1. Tap the ⭐ icon in the top bar
2. Tap "Subscribe Now" under Monthly Subscription
3. Complete the purchase through Google Play
4. All premium templates are now unlocked

**Option 2: One-Time Purchase**
1. Tap the ⭐ icon in the top bar
2. Tap "Unlock Template" under One-Time Purchase
3. Complete the purchase through Google Play
4. Select a premium template when creating a log
5. The first locked template you try to use will be unlocked

#### Managing Your Logs
- **View**: Scroll through your activity logs on the home screen
- **Delete**: Tap the trash icon on any log card
- **Filter**: Logs are sorted by most recent first

### For Developers

#### Adding New Premium Templates

1. **Update the template list** in `AddLogScreen.kt`:
```kotlin
val availableTemplates = listOf(
    // ... existing templates
    LogTemplate("premium_4", "New Template", "Description", true, Color(0xFF...))
)
```

2. **Add template-specific fields** in the AddLogScreen composable

3. **Update unlocking logic** in `ActivityLogViewModel.kt` if needed

#### Customizing Purchase Flow

Modify `BillingManager.kt` to:
- Handle purchase verification
- Implement server-side validation
- Add purchase restoration logic
- Customize error handling

#### Extending the Database

To add new fields to ActivityLog:
1. Update the `ActivityLog` data class
2. Increment the database version in `ActivityLogDatabase.kt`
3. Implement a migration strategy

## Important Notes

### Before Publishing

1. **Update Product IDs**: Ensure product IDs in code match Google Play Console
2. **Test Thoroughly**: Test all purchase flows with test accounts
3. **Handle Edge Cases**: 
   - Network failures
   - Purchase interruptions
   - Subscription renewals/cancellations
4. **Add Server-Side Verification**: For production, implement server-side purchase verification
5. **Privacy Policy**: Required for apps with subscriptions
6. **Terms of Service**: Recommended for apps with in-app purchases

### Security Considerations

- Never store sensitive payment information in the app
- Implement server-side receipt verification for production
- Use ProGuard/R8 for code obfuscation in release builds
- Keep the Google Play Billing Library updated

### Testing Checklist

- [ ] Test consumable purchase flow
- [ ] Test subscription purchase flow
- [ ] Test purchase restoration
- [ ] Test with no internet connection
- [ ] Test canceling a purchase
- [ ] Test subscription expiration
- [ ] Test multiple consumable purchases
- [ ] Test UI with/without premium access
- [ ] Test database persistence
- [ ] Test with different Android versions

## Troubleshooting

### Billing Client Not Connecting
- Ensure the app is signed with the same key as the Play Console version
- Verify the application ID matches the one in Play Console
- Check that products are activated in Play Console

### Purchases Not Working
- Confirm you're using a test account from License testing
- Ensure the test account is added to the internal/closed test track
- Verify product IDs match exactly between code and Play Console
- Check that BILLING permission is in AndroidManifest.xml

### Database Issues
- Clear app data if schema changes without migration
- Check Room database version number
- Verify DAO queries are correct

## License

This project is created as a demonstration of Android in-app purchases implementation.

## Support

For issues with:
- **Google Play Billing**: Check [Google Play Billing Library documentation](https://developer.android.com/google/play/billing)
- **Room Database**: Check [Room documentation](https://developer.android.com/training/data-storage/room)
