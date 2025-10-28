# Logme - Quick Setup Guide

## Step-by-Step Setup for Development

### 1. Sync the Project

Open the project in Android Studio and click "Sync Now" when prompted to sync Gradle files.

### 2. Build the Application

Run the following command in the terminal or use Android Studio's Build menu:
```bash
./gradlew build
```

### 3. Run on Emulator or Device

1. Start an Android emulator (API 24+) or connect a physical device
2. Click the "Run" button in Android Studio
3. The app will install and launch automatically

### 4. Test Basic Features (Without In-App Purchases)

At this stage, you can test:
- Creating activity logs with the basic template
- Viewing logs on the home screen
- Deleting logs
- Navigating through the app

**Note**: Premium templates and in-app purchases will NOT work yet until you configure Google Play Console.

## Setting Up In-App Purchases (For Testing & Production)

### Overview
To test in-app purchases, you need to:
1. Create a Google Play Developer account ($25 one-time fee)
2. Create an app in Google Play Console
3. Set up products (consumable and subscription)
4. Create a test track
5. Add test accounts
6. Generate a signed APK/AAB
7. Upload to the test track

### Detailed Steps

#### 1. Create a Google Play Developer Account
- Go to https://play.google.com/console
- Register and pay the $25 fee
- Complete account verification

#### 2. Create Your App
- In Play Console, click "Create app"
- Fill in app details:
  - App name: Logme
  - Language: English (or your language)
  - App/Game: App
  - Free/Paid: Free
- Complete all required sections in the dashboard

#### 3. Generate a Signed APK/AAB

In Android Studio:

**Option A: Using Build Menu**
1. Build → Generate Signed Bundle/APK
2. Select "Android App Bundle"
3. Create a new keystore (or use existing)
4. Fill in keystore details and remember them!
5. Select "release" build type
6. Click "Finish"

**Option B: Using Command Line**
```bash
# Create keystore (first time only)
keytool -genkey -v -keystore my-release-key.keystore -alias my-key-alias -keyalg RSA -keysize 2048 -validity 10000

# Build signed AAB
./gradlew bundleRelease
```

#### 4. Upload to Play Console
1. In Play Console, go to Testing → Internal testing
2. Create a new release
3. Upload your signed AAB file
4. Complete the release notes
5. Review and roll out to internal testing

#### 5. Create In-App Products

**Consumable Product:**
1. Go to Monetize → Products → In-app products
2. Click "Create product"
3. Product ID: `premium_template_unlock`
4. Name: "Premium Template Unlock"
5. Description: "Unlock one premium activity log template permanently"
6. Price: $2.99 (or your preferred price)
7. Click "Save" and then "Activate"

**Subscription:**
1. Go to Monetize → Products → Subscriptions
2. Click "Create subscription"
3. Product ID: `premium_subscription_monthly`
4. Name: "Premium Monthly Subscription"
5. Description: "Access all premium activity log templates"
6. Create a base plan:
   - Billing period: Monthly
   - Price: $4.99/month (or your preferred price)
7. Click "Save" and then "Activate"

#### 6. Add Test Accounts
1. Go to Setup → License testing
2. Add Gmail accounts that you'll use for testing
3. Choose "License response": Licensed
4. These accounts can make test purchases without being charged

#### 7. Install and Test
1. Join the internal testing program using one of your test accounts
2. Download the app from the Play Store test link
3. Test the in-app purchase flows

### Important Notes

- **Signing Key**: Keep your keystore file and passwords safe! You'll need them for all future updates.
- **Application ID**: Ensure it matches between code and Play Console
- **Test Accounts**: Must be added to License testing before they can test purchases
- **Product Activation**: Products must be "Activated" in Play Console to work
- **Propagation Time**: After creating/activating products, wait 2-4 hours before testing

## Development Without Google Play Setup

If you want to develop without setting up Google Play:

1. The app will work for all non-premium features
2. Premium templates will show as locked
3. Purchase buttons will fail (expected behavior)
4. You can mock the billing responses in BillingManager.kt for UI testing

## Troubleshooting

### "Unable to connect to Billing Service"
- Ensure the app is installed from the Play Store test track
- Verify the app is signed with the same key as uploaded to Play Console
- Check that billing permission is in AndroidManifest.xml

### "Product not found"
- Verify product IDs match exactly between code and Play Console
- Ensure products are activated in Play Console
- Wait a few hours after creating products

### App won't install from Play Store
- Ensure you're logged in with a test account
- Verify the test account is added to the internal testing track
- Check that you've accepted the testing invitation

## Next Steps

After setup:
1. Test all features thoroughly
2. Add app screenshots and description in Play Console
3. Complete the content rating questionnaire
4. Set up target audience and content settings
5. When ready, move from internal testing to production

## Resources

- [Google Play Billing Documentation](https://developer.android.com/google/play/billing)
- [Play Console Help](https://support.google.com/googleplay/android-developer)
- [Android Developer Guide](https://developer.android.com/guide)

## Need Help?

Check the main README.md file for more detailed information about the app architecture and features.
