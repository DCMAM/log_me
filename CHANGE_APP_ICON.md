# How to Change the App Icon for Logme

## Method 1: Using Android Studio (Easiest & Recommended)

### Step-by-Step Instructions

1. **Open your project in Android Studio**

2. **Right-click on the `res` folder**
   - Navigate to: `app/src/main/res`
   - Right-click on `res`

3. **Select New → Image Asset**
   - Go to: New → Image Asset
   - The "Asset Studio" window will open

4. **Configure your icon**
   
   **Foreground Layer:**
   - Icon Type: Select "Launcher Icons (Adaptive and Legacy)"
   - Name: Keep as `ic_launcher`
   - Asset Type: Choose one:
     - **Image**: Upload your logo/icon image (PNG recommended)
     - **Clip Art**: Use built-in Android icons
     - **Text**: Create text-based icon
   
   **For Image:**
   - Click the folder icon next to "Path"
   - Select your icon image (PNG, JPG, etc.)
   - Adjust the size with the "Resize" slider
   - Trim whitespace if needed
   
   **Background Layer:**
   - You can set a solid color or image
   - Recommended: Simple solid color that matches your brand
   - Example: #2196F3 (blue) for Logme

5. **Preview your icon**
   - Check the preview on different shapes (circle, square, rounded square)
   - Ensure it looks good on all variants

6. **Click "Next"**
   - Review the files that will be generated
   - It will create icons for all densities (mdpi, hdpi, xhdpi, xxhdpi, xxxhdpi)

7. **Click "Finish"**
   - Android Studio will automatically generate all icon sizes
   - Old icons will be replaced

8. **Clean and Rebuild**
   ```bash
   Build → Clean Project
   Build → Rebuild Project
   ```

9. **Run the app**
   - Your new icon should now appear!

## Method 2: Manual Icon Replacement

If you have pre-made icon files for each density:

### Required Icon Sizes

Create PNG images at these exact sizes:

| Density | Size | Folder |
|---------|------|--------|
| mdpi    | 48x48 px | `mipmap-mdpi` |
| hdpi    | 72x72 px | `mipmap-hdpi` |
| xhdpi   | 96x96 px | `mipmap-xhdpi` |
| xxhdpi  | 144x144 px | `mipmap-xxhdpi` |
| xxxhdpi | 192x192 px | `mipmap-xxxhdpi` |

### Steps

1. **Create your icons** at the sizes listed above
   - Name them: `ic_launcher.png` and `ic_launcher_round.png`

2. **Replace the files** in these folders:
   ```
   app/src/main/res/mipmap-mdpi/
   app/src/main/res/mipmap-hdpi/
   app/src/main/res/mipmap-xhdpi/
   app/src/main/res/mipmap-xxhdpi/
   app/src/main/res/mipmap-xxxhdpi/
   ```

3. **Update adaptive icon XML** (optional)
   - Edit: `app/src/main/res/mipmap-anydpi-v26/ic_launcher.xml`
   - Edit: `app/src/main/res/mipmap-anydpi-v26/ic_launcher_round.xml`

4. **Clean and rebuild**

## Method 3: Using Online Icon Generator

### Recommended Tools

1. **Android Asset Studio** (Official)
   - Website: https://romannurik.github.io/AndroidAssetStudio/
   - Go to "Launcher icon generator"
   - Upload your image
   - Download the generated zip file
   - Extract and copy to your project

2. **AppIcon.co**
   - Website: https://appicon.co/
   - Upload 1024x1024 image
   - Generate icons for Android
   - Download and replace

3. **Icon Kitchen**
   - Website: https://icon.kitchen/
   - More customization options
   - Generate adaptive icons

### Steps for Online Tools

1. Upload your icon (usually 512x512 or 1024x1024)
2. Customize settings
3. Download the generated files
4. Extract the zip file
5. Copy all files to your project:
   - Copy contents to `app/src/main/res/`
   - Overwrite existing files

## Icon Design Guidelines

### Best Practices

1. **Size**: Start with 1024x1024 px image
2. **Format**: PNG with transparent background
3. **Padding**: Leave 25% padding around the icon
4. **Simplicity**: Keep it simple and recognizable
5. **Colors**: Use your brand colors

### Logme Icon Suggestions

For the Logme app, consider:
- 📝 Notebook or journal icon
- ✏️ Pencil or pen icon
- 📊 Chart or graph icon
- 📋 Clipboard icon
- 🔖 Bookmark icon
- Combine "L" letter with logging symbol

### Adaptive Icon Tips

Adaptive icons have two layers:
- **Foreground**: Your main icon/logo (transparent background)
- **Background**: Solid color or simple pattern

Android will automatically mask your icon into different shapes based on device manufacturer.

## Quick Design in Figma/Canva

### Using Canva (Free)

1. Create 1024x1024 px design
2. Add your logo/text: "Logme" or "L"
3. Use these colors (from your app):
   - Primary: #2196F3 (blue)
   - Gold: #FFD700 (for premium feel)
4. Export as PNG
5. Use Method 1 (Android Studio) to import

### Simple Text Icon

If you don't have a logo yet:
1. Open Android Studio Asset Studio
2. Choose "Text" asset type
3. Type: "L" or "Log"
4. Choose a nice font
5. Pick your brand color
6. Generate!

## Verify Your Icon

After changing the icon:

1. **Uninstall old app** from device/emulator (optional but recommended)
2. **Clean project**: Build → Clean Project
3. **Rebuild**: Build → Rebuild Project
4. **Run app**
5. **Check home screen** - new icon should appear
6. **Test on different launchers** if possible

## Common Issues

### Icon Not Changing

**Solution:**
```bash
# In Android Studio Terminal
./gradlew clean
# Or
Build → Clean Project → Rebuild Project

# Then uninstall app from device and reinstall
```

### Icon Looks Blurry

**Cause:** Image resolution too low

**Solution:** Use higher resolution source image (at least 512x512)

### Icon Gets Cropped

**Cause:** No padding in the image

**Solution:** Add 15-25% transparent padding around your icon

### Different Icon Shapes

**This is normal!** Android 8.0+ uses adaptive icons that change shape based on:
- Device manufacturer (Samsung, Google, etc.)
- User's launcher choice

Make sure your icon looks good in all previewed shapes.

## File Locations Reference

```
app/src/main/res/
├── mipmap-anydpi-v26/
│   ├── ic_launcher.xml          # Adaptive icon config
│   └── ic_launcher_round.xml
├── mipmap-mdpi/
│   ├── ic_launcher.webp         # 48x48
│   └── ic_launcher_round.webp
├── mipmap-hdpi/
│   ├── ic_launcher.webp         # 72x72
│   └── ic_launcher_round.webp
├── mipmap-xhdpi/
│   ├── ic_launcher.webp         # 96x96
│   └── ic_launcher_round.webp
├── mipmap-xxhdpi/
│   ├── ic_launcher.webp         # 144x144
│   └── ic_launcher_round.webp
└── mipmap-xxxhdpi/
    ├── ic_launcher.webp         # 192x192
    └── ic_launcher_round.webp
```

## Recommended Workflow

1. ✅ Design your icon in 1024x1024 (or use Icon Kitchen)
2. ✅ Use Android Studio's Image Asset tool
3. ✅ Preview on all shapes
4. ✅ Clean and rebuild
5. ✅ Test on device
6. ✅ Done!

## Need Icon Design Help?

If you need help creating a custom icon for Logme:
- I can help design a simple text-based icon
- Consider hiring a designer on Fiverr ($5-20)
- Use free icon resources like:
  - Flaticon.com
  - Icons8.com
  - Material Icons

Would you like me to help you create a custom icon using the Image Asset tool in Android Studio?
