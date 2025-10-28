# How to Install APK on Samsung Galaxy A73

## Common Issue: Cannot Install APK

If you're getting an error like "App not installed", "Installation blocked", or "For security, your phone is not allowed to install apps from this source", follow these steps:

## Solution 1: Enable Installation from Unknown Sources (Samsung One UI)

### For Android 12+ (One UI 4.0+) - Most Likely Your Case

1. **Locate your APK file**
   - Usually in: Downloads folder or File Manager

2. **Try to install the APK**
   - Tap on `app-debug.apk`
   - You'll see a popup saying installation is blocked

3. **Enable installation for that app**
   - In the popup, tap **Settings** or **Install anyway**
   - You'll be taken to settings
   - Toggle ON: "Allow from this source"
   - Common sources:
     - "My Files" (Samsung's file manager)
     - "Files" (Google Files)
     - "Downloads"
     - Your web browser (if downloaded)

4. **Go back and try installing again**

### Alternative Method (Settings)

1. Open **Settings**
2. Go to **Security and Privacy** (or just **Security**)
3. Tap **Install unknown apps** or **Unknown sources**
4. Find the app you're using to install (e.g., "My Files", "Chrome", "Files")
5. Toggle ON: "Allow from this source"
6. Try installing the APK again

## Solution 2: Disable Play Protect (Temporarily)

Google Play Protect might be blocking the installation:

1. Open **Google Play Store**
2. Tap your **profile icon** (top right)
3. Go to **Play Protect**
4. Tap the **gear icon** (⚙️) or three dots
5. Turn OFF: "Scan apps with Play Protect"
6. Try installing your APK
7. **Remember to turn it back ON after installation**

## Solution 3: Using Different File Manager

Sometimes the file manager matters:

### Try Google Files (Recommended)

1. Install **Files by Google** from Play Store (if not already installed)
2. Open Files app
3. Go to **Downloads** or **Browse** → **Downloads**
4. Find `app-debug.apk`
5. Tap to install
6. Allow installation when prompted

### Try Samsung My Files

1. Open **My Files** app
2. Go to **Internal Storage** → **Download**
3. Find `app-debug.apk`
4. Tap to install

## Solution 4: Check APK Integrity

Make sure the APK was built correctly:

### Verify APK Location

The debug APK should be at:
```
YourProject/app/build/outputs/apk/debug/app-debug.apk
```

### Rebuild the APK

If the APK might be corrupted:

1. **In Android Studio:**
   ```
   Build → Clean Project
   Build → Rebuild Project
   Build → Build Bundle(s) / APK(s) → Build APK(s)
   ```

2. **Or via command line:**
   ```bash
   ./gradlew clean
   ./gradlew assembleDebug
   ```

3. **Find the new APK:**
   - Check the build output path shown in Android Studio
   - Or navigate to: `app/build/outputs/apk/debug/`

4. **Transfer to phone again**

## Solution 5: Install via ADB (Most Reliable)

If nothing else works, use ADB (Android Debug Bridge):

### Prerequisites

1. **Enable Developer Options on your phone:**
   - Go to Settings → About phone
   - Tap "Build number" 7 times
   - Developer mode activated!

2. **Enable USB Debugging:**
   - Go to Settings → Developer options
   - Turn ON: "USB debugging"

### Install via ADB

1. **Connect phone to computer via USB**

2. **When prompted on phone:**
   - Tap "Allow USB debugging"
   - Check "Always allow from this computer"

3. **In your project terminal:**
   ```bash
   # Check if device is connected
   adb devices
   
   # Install the APK
   adb install app/build/outputs/apk/debug/app-debug.apk
   
   # Or if already installed, reinstall
   adb install -r app/build/outputs/apk/debug/app-debug.apk
   ```

4. **Check your phone** - app should now be installed!

### Or Install Directly from Android Studio

1. Connect phone via USB
2. Enable USB debugging (as above)
3. In Android Studio, select your device from the dropdown (top toolbar)
4. Click the green **Run** button ▶️
5. App installs automatically!

## Solution 6: Check Minimum SDK Version

Your Samsung Galaxy A73 runs Android 12 (One UI 4.1), so it should work fine since our app supports:
- **Minimum SDK: 24** (Android 7.0)
- **Target SDK: 36**

But let's verify:

```kotlin
// Check app/build.gradle.kts
defaultConfig {
    minSdk = 24  // Should be 24 or lower
    targetSdk = 36
}
```

## Common Error Messages & Solutions

### "App not installed"
- **Cause**: Installation blocked by security settings
- **Solution**: Enable "Install unknown apps" (Solution 1)

### "Installation blocked by Play Protect"
- **Cause**: Google Play Protect scanning
- **Solution**: Disable Play Protect temporarily (Solution 2)

### "For security, your phone is not allowed to install..."
- **Cause**: Unknown sources blocked for that app
- **Solution**: Allow installation for your file manager (Solution 1)

### "There was a problem parsing the package"
- **Cause**: Corrupted APK or wrong architecture
- **Solution**: Rebuild APK (Solution 4)

### "App not installed as package appears to be invalid"
- **Cause**: APK corrupted during transfer
- **Solution**: Rebuild and transfer again

## Recommended Installation Method for Galaxy A73

**Best method for your device:**

1. Build APK in Android Studio
2. Connect phone via USB
3. Enable USB debugging
4. Click Run button in Android Studio
5. Done! ✅

**Alternative (Manual):**

1. Build APK
2. Transfer to phone (Google Drive, USB, etc.)
3. Open with Google Files app
4. Allow installation from Files app
5. Install

## Transfer Methods

### Method 1: USB Cable
```bash
# Copy APK to phone
adb push app/build/outputs/apk/debug/app-debug.apk /sdcard/Download/
```

### Method 2: Google Drive
1. Upload APK to Google Drive
2. Download on phone
3. Install from Downloads

### Method 3: Direct Share
1. Right-click APK in File Explorer
2. Share → Nearby Share / Quick Share
3. Send to your phone
4. Install

## After Installation

Once installed successfully:

1. Find "Logme" app in your app drawer
2. Open the app
3. Test all features
4. Premium purchases won't work in debug build until you set up Google Play Console

## Still Having Issues?

### Diagnostic Steps

1. **Check Android version:**
   - Settings → About phone → Software information
   - Android version should be 7.0 or higher

2. **Check storage space:**
   - Settings → Battery and device care → Storage
   - Need at least 100MB free

3. **Check if APK exists:**
   - Verify file size (should be 5-15 MB typically)
   - Check file isn't 0 bytes

4. **Check build logs:**
   - In Android Studio, check Build output
   - Look for any errors

### Try This Quick Fix

```bash
# In your project directory
./gradlew clean assembleDebug

# Then install via ADB
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

## Development
