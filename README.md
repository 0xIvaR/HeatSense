# HeatSense

**Keep your Android device cool and healthy!** 🌡️

A real-time Android application built with Kotlin that monitors and displays both battery temperature and CPU temperature of your Android device. Perfect for gamers, power users, and anyone who wants to keep their device running optimally.

## 🎯 Why Use This App?

- **Prevent Overheating**: Monitor your device's temperature in real-time
- **Optimize Performance**: Keep your phone running smoothly by tracking thermal conditions
- **Battery Health**: Monitor battery temperature to extend its lifespan
- **Gaming & Heavy Usage**: Perfect for monitoring during intensive tasks
- **Clean Interface**: Beautiful Material Design with intuitive controls

## Features

🔋 **Battery Temperature Monitoring**
- Real-time battery temperature readings
- Color-coded temperature indicators (Cool/Normal/Warm/Hot)
- Uses Android's built-in BatteryManager for accurate readings

🖥️ **CPU Temperature Monitoring**
- Real-time CPU temperature from thermal sensors
- Monitors multiple thermal zones for comprehensive coverage
- Color-coded indicators (Cool/Normal/Warm/Hot/Very Hot)

📱 **Modern UI**
- Material Design with beautiful card layouts
- Real-time updates every second
- Intuitive start/stop monitoring controls
- Responsive design that works on all screen sizes

## Screenshots

The app displays two main temperature cards:
- **Battery Temperature Card**: Shows current battery temp with status
- **CPU Temperature Card**: Shows CPU thermal sensor readings with status

## Technical Details

### Battery Temperature
- Uses Android's `BatteryManager.EXTRA_TEMPERATURE`
- Temperature is provided in tenths of degrees Celsius
- Automatically updates when battery status changes

### CPU Temperature
The app checks multiple thermal sensor paths:
- `/sys/class/thermal/thermal_zone*/temp`
- `/sys/devices/system/cpu/cpu0/cpufreq/cpu_temp`
- `/sys/class/hwmon/hwmon*/temp1_input`
- And several other common thermal sensor locations

### Temperature Ranges
**Battery:**
- Cool: < 25°C
- Normal: 25-35°C
- Warm: 35-40°C
- Hot: > 40°C

**CPU:**
- Cool: < 30°C
- Normal: 30-50°C
- Warm: 50-60°C
- Hot: 60-70°C
- Very Hot: > 70°C

## Setup Instructions

### Prerequisites
- Android Studio (latest version recommended)
- Android SDK with minimum API level 21 (Android 5.0)
- Physical Android device or emulator

### Installation Steps

1. **Open in Android Studio**
   ```bash
   # Navigate to your project directory
   cd "Phone temp"
   # Open Android Studio and select "Open an existing project"
   # Select this directory
   ```

2. **Sync Project**
   - Android Studio will automatically detect the project
   - Click "Sync Now" when prompted to sync Gradle files
   - Wait for the sync to complete

3. **Build the Project**
   - Go to `Build > Make Project` or press `Ctrl+F9` (Windows) / `Cmd+F9` (Mac)
   - Ensure there are no build errors

4. **Run the App**
   - Connect your Android device via USB with Developer Options enabled
   - Or start an Android emulator
   - Click the "Run" button or press `Shift+F10`
   - Select your device/emulator from the list

### Important Notes

⚠️ **Permissions**: The app requests system-level permissions to read temperature data. Some permissions may not be granted on non-rooted devices, which is normal.

⚠️ **CPU Temperature**: CPU temperature readings depend on your device's hardware and may not be available on all devices. The app will show "N/A" if CPU temperature cannot be read.

⚠️ **Battery Temperature**: Battery temperature should work on most Android devices as it uses standard Android APIs.

## Troubleshooting

### Common Issues

1. **CPU Temperature shows "N/A"**
   - This is normal on many devices as thermal sensors are protected
   - The app tries multiple sensor paths but may not have access on non-rooted devices

2. **Build Errors**
   - Ensure you have the latest Android Studio and SDK
   - Try `Build > Clean Project` then `Build > Rebuild Project`

3. **App Crashes**
   - Check that your device runs Android 5.0 (API 21) or higher
   - Check the logcat for specific error messages

### Testing

- Battery temperature monitoring should work immediately
- CPU temperature may require specific device models or root access
- Test on a physical device for best results (emulators may not provide accurate readings)

## Project Structure

```
app/
├── src/main/
│   ├── java/com/heatsense/app/
│   │   └── MainActivity.kt              # Main activity with temperature logic
│   ├── res/
│   │   ├── layout/
│   │   │   └── activity_main.xml        # Main UI layout
│   │   ├── values/
│   │   │   ├── colors.xml               # App colors
│   │   │   ├── strings.xml              # String resources
│   │   │   └── themes.xml               # App themes
│   │   ├── drawable/                    # Icons and backgrounds
│   │   └── xml/                         # Backup and data extraction rules
│   └── AndroidManifest.xml              # App configuration
├── build.gradle                         # App-level build configuration
├── proguard-rules.pro                   # ProGuard rules
build.gradle                             # Project-level build configuration
settings.gradle                          # Project settings
```

## Future Enhancements

- [ ] Temperature history graphs
- [ ] Temperature alerts and notifications
- [ ] Export temperature data
- [ ] Widget for home screen
- [ ] Different temperature units (Fahrenheit, Kelvin)
- [ ] Background monitoring service

## License

This project is open source and available under the MIT License.

---

**Note**: This app is designed for monitoring purposes. Extreme temperatures may indicate hardware issues that should be addressed by a professional. 