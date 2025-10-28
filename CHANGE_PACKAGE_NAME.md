# How to Change Package Name from com.example.logme

## The Problem

Google Play Console restricts package names that start with "com.example" because they're meant for sample/tutorial apps only. You need to use a proper package name for production apps.

## Recommended Package Name Format

Choose one of these patterns:

1. **Your domain (reversed):**
   - If you own `yourname.com` → `com.yourname.logme`
   - If you own `mybusiness.id` → `id.mybusiness.logme`

2. **Your name:**
   - `com.yourname.logme`
   - `com.firstname.logme`

3. **Your brand/company:**
   - `com.yourbrand.logme`
   - `com.companyname.logme`

**Examples:**
- `com.john.logme`
- `com.mycompany.logme`
- `id.myname.logme`
- `com.yourname.activitylogger`

⚠️ **Important**: Once published to Play Store, you CANNOT change the package name. Choose wisely!

## Method 1: Using Android Studio Refactor (Recommended)

### Step 1: Change applicationId in build.gradle.kts

1. **Open** `app/build.gradle.kts`

2. **Find** the `defaultConfig` section:
   ```kotlin
   defaultConfig {
       applicationId = "com.example.logme"  // ← Change this
       minSdk = 24
       targetSdk = 36
       ...
   }
   ```

3. **Change to your new package name:**
   ```kotlin
   defaultConfig {
       applicationId = "com.yourname.logme"  // ← Your new name
       minSdk = 24
       targetSdk = 36
       ...
   }
   ```

4. **Click "Sync Now"** when prompted

### Step 2: Refactor Package Name in Code

1. **In Android Studio's Project view**, switch to **Project** view (not Android)
   - Click the dropdown at top that says "Android"
   - Select "Project"

2. **Navigate to:**
   ```
   app/src/main/java/com/example/logme/
   ```

3. **Right-click on the `logme` folder**

4. **Select**: Refactor → Rename

5. **In the popup:**
   - Uncheck "Search in comments and strings"
   - Uncheck "Search for text occurrences"
   - Click "Refactor"

6. **Now right-click on `example` folder**

7. **Select**: Refactor → Rename

8. **Change `example` to your name** (e.g., `yourname`)

9. **Click "Refactor"**

10. **Android Studio will update all imports automatically!**

### Step 3: Update Manifest

The `AndroidManifest.xml` should update automatically, but verify:

1. **Open** `app/src/main/AndroidManifest.xml`

2. **Check the package attribute:**
   ```xml
   <manifest xmlns:android="http://schemas.android.com/apk/res/android"
       package="com.yourname.logme">  <!-- Should be your new package -->
   ```

### Step 4: Clean and Rebuild

1. **Clean project:**
   ```
   Build → Clean Project
   ```

2. **Rebuild:**
   ```
   Build → Rebuild Project
   ```

3. **If you see any import errors**, Android Studio should offer to fix them automatically

## Method 2: Manual Method (If Refactor Doesn't Work)

### Step 1: Update build.gradle.kts

```kotlin
// app/build.gradle.kts
defaultConfig {
    applicationId = "com.yourname.logme"  // New package
    ...
}
```

### Step 2: Create New Package Structure

1. **In Project view**, navigate to: `app/src/main/java/`

2. **Create new folders:**
   - Right-click `java` → New → Package
   - Enter: `com.yourname.logme`

3. **This creates the folder structure:**
   ```
   com/yourname/logme/
   ```

### Step 3: Move All Files

1. **Select all Kotlin files** in the old package (`com/example/logme/`)
   - MainActivity.kt
   - All files in billing/, data/, ui/, viewmodel/ folders

2. **Drag and drop** to the new package folder

3. **When Android Studio asks**, choose:
   - "Refactor" (not "Move")
   - Click "OK"

### Step 4: Delete Old Package

1. **Delete the old** `com/example/` folder structure
2. **It should now be empty**

### Step 5: Update Package Declarations

Android Studio should have updated these automatically, but check each file:

```kotlin
// OLD:
package com.example.logme

// NEW:
package com.yourname.logme
```

And imports:
```kotlin
// OLD:
import com.example.logme.data.ActivityLog

// NEW:
import com.yourname.logme.data.ActivityLog
```

### Step 6: Update AndroidManifest.xml

```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.yourname.logme">  <!-- Update this -->
```

### Step 7: Clean and Rebuild

```
Build → Clean Project
Build → Rebuild Project
```

## Verification Checklist

After changing the package name, verify:

- [ ] `app/build.gradle.kts` → `applicationId` is updated
- [ ] All `.kt` files → `package` declaration is updated
- [ ] All imports → Using new package name
- [ ] `AndroidManifest.xml` → `package` attribute is updated
- [ ] Project builds without errors
- [ ] App runs on device/emulator

## Quick Command Line Method

If you prefer command line:

```bash
# Replace 'yourname' with your actual name
NEW_PACKAGE="com.yourname.logme"

# Update applicationId in build.gradle.kts
# (You'll need to edit this file manually)

# Then rebuild
./gradlew clean build
```

## Common Issues & Solutions

### Issue: "Cannot resolve symbol" errors

**Solution:**
1. File → Invalidate Caches → Invalidate and Restart
2. After restart: Build → Clean Project → Rebuild Project

### Issue: Old package name still showing

**Solution:**
- Check all import statements manually
- Search entire project (Ctrl+Shift+F / Cmd+Shift+F) for "com.example"
- Replace all occurrences with your new package name

### Issue: App won't build

**Solution:**
```bash
# Delete build folders
rm -rf app/build
rm -rf build

# Or on Windows:
rmdir /s /q app\build
rmdir /s /q build

# Rebuild
./gradlew clean build
```

## Recommended Package Names for Logme

If you don't have a domain, here are some suggestions:

**Format: com.[youridentifier].logme**

Examples:
- `com.johnsmith.logme`
- `com.jsmith.logme`
- `com.logmeapp.app`
- `com.myapps.logme`
- `id.developer.logme` (if you're in Indonesia)

**Avoid:**
- `com.example.*` ❌
- `com.test.*` ❌
- `com.sample.*` ❌
- `com.google.*` ❌
- `com.android.*` ❌

## After Changing Package Name

1. **Build signed APK/AAB** with new package name
2. **Upload to Google Play Console**
3. **Package name should now be accepted** ✅

## Important Notes

⚠️ **Before publishing:**
- Choose your package name carefully
- You CANNOT change it after publishing to Play Store
- Make sure it's unique and professional
- Consider using your domain name if you have one

⚠️ **For testing:**
- You'll need to uninstall the old version from your phone
- The new package name is treated as a different app

## Example
