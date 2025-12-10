# HeatSense 🔥

**A Cyberpunk-Themed Thermal Monitoring Dashboard for Android** 

HeatSense is a real-time thermal monitoring application built with Kotlin that transforms your Android device into a high-tech command center. Monitor CPU and battery temperatures with a stunning cyberpunk aesthetic featuring circular gauges, live timeline graphs, and an immersive dark interface.

---

## 🎨 UI Experience

### Cyberpunk Core Design
HeatSense features a **fully immersive cyberpunk dashboard** with:

- **Pure Black Background**: Deep, dark aesthetic that's easy on the eyes and battery-friendly for OLED screens
- **Sticky Header Bar**: "HEATSENSE" branding stays fixed at the top for easy identification
- **Twin Circular Gauges**: Animated reactor-style temperature displays for CPU and Battery
- **Glassmorphic Cards**: Frosted glass effect with semi-transparent backgrounds and subtle borders
- **Live Temperature Timeline**: Real-time 5-minute scrolling graph with smooth Bézier curves
- **Gradient Progress Indicators**: Vibrant cyan-to-amber gradients that respond to temperature changes
- **SF Pro Typography**: Apple's SF Pro font family for a clean, modern, professional look

### Visual Elements

#### 🎯 The Cockpit (Main Dashboard)
- **CPU Core Gauge**: Large circular gauge (left) displaying real-time CPU temperature
  - Bold 50sp temperature display
  - Color-coded from cool cyan to hot amber
  - Smooth animated transitions
  
- **Battery Pack Gauge**: Large circular gauge (right) showing battery temperature
  - Matching design with CPU gauge
  - Real-time updates with smooth animations
  - Visual feedback for temperature changes

#### 📊 Secondary Stats Bar (Glassmorphic Card)
- **Battery Status Section**:
  - Current battery percentage (22sp, semibold)
  - Charging/Discharging status (16sp, medium)
  - Dynamic charging icon when plugged in
  - Amber color for charging, white for discharging
  
- **Peak Session Temperature**:
  - Highest temperature recorded in current session
  - Color-coded based on severity (cyan → amber → red)
  - Automatically tracks and updates

#### 📈 Live Temperature Timeline
- **5-Minute Real-Time Graph**:
  - Smooth cubic Bézier curves for elegant line rendering
  - Dual-line chart: CPU (cyan) and Battery (amber)
  - Gradient fills under curves for depth
  - Transparent background integrated with app theme
  - Auto-scrolling timeline with 1-second data points
  - Clean axis labels with SF Pro Regular font

### Typography Hierarchy
- **Header**: SF Pro Bold, 18sp, letter-spaced
- **Gauge Temperatures**: SF Pro Bold, 50sp
- **Gauge Labels**: SF Pro Medium, 12sp, letter-spaced
- **Status Numbers**: SF Pro Semibold, 22sp
- **Status Labels**: SF Pro Medium, 11-16sp
- **Graph Title**: SF Pro Semibold, 12sp, all caps
- **Graph Axes**: SF Pro Regular, 10sp

---

## 🚀 Features

### Real-Time Monitoring
- ⚡ **CPU Temperature Tracking**: Reads from multiple thermal zones (`/sys/class/thermal/thermal_zone*/temp`)
- 🔋 **Battery Temperature Tracking**: Uses Android's `BatteryManager` API for accurate readings
- 📊 **Live Timeline Graph**: 5-minute scrolling history with smooth curve interpolation
- 🎯 **Peak Session Tracking**: Records highest temperature during app session
- 🔄 **Auto-Updates**: Temperature data refreshes every second

### Battery Intelligence
- 📱 **Battery Level Display**: Real-time percentage monitoring
- ⚡ **Charging Status Detection**: Shows "Charging" or "Discharging" with visual indicators
- 🔌 **Charging Icon**: Dynamic lightning bolt appears when device is plugged in
- 🌡️ **Temperature Safety**: Color-coded warnings for battery health

### Visual Feedback
- 🎨 **Dynamic Color Coding**:
  - **Cool**: Cyan (< 35°C)
  - **Normal**: Cyan-Green (35-45°C)
  - **Warm**: Yellow-Amber (45-55°C)
  - **Hot**: Orange-Red (> 55°C)
- ✨ **Smooth Animations**: Gauge transitions and graph updates are fluid and responsive
- 🌙 **Dark Mode Optimized**: Pure black background for OLED battery savings

### User Experience
- 📱 **Responsive Layout**: Adapts to all screen sizes and orientations
- 🎯 **Sticky Header**: App name stays visible while scrolling
- 💨 **Smooth Scrolling**: Content flows naturally with optimized performance
- 🎭 **Transparent Status Bar**: Seamless integration with Android system UI
- 🚫 **No Clutter**: Clean, focused interface with only essential information

---

## 📱 Technical Details

### Architecture
- **Language**: Kotlin
- **UI Framework**: Android Views with Custom Views
- **Charting Library**: MPAndroidChart (via JitPack)
- **Minimum SDK**: API 21 (Android 5.0 Lollipop)
- **Target SDK**: API 34 (Android 14)

### Custom Views
1. **CircularGaugeView**: 
   - Custom canvas drawing with Paint and Path APIs
   - Animated arc progress with ValueAnimator
   - Gradient rendering with SweepGradient
   - SF Pro Bold typography for temperature display

2. **TemperatureChartView**:
   - Wraps MPAndroidChart LineChart
   - Cubic Bézier curve interpolation (intensity: 0.25f)
   - Custom styling with gradient fills
   - Dual-dataset management for CPU and Battery
   - Auto-scrolling X-axis with time-based updates

3. **GlassmorphicView**:
   - Translucent background effect
   - Semi-transparent overlay simulation
   - Compatible with API 21+

### Temperature Sources

#### Battery Temperature
- **API**: `BatteryManager.EXTRA_TEMPERATURE`
- **Unit**: Celsius (converted from tenths)
- **Update**: Automatic via `BroadcastReceiver`
- **Availability**: Works on all Android devices

#### CPU Temperature
The app attempts to read from multiple thermal sensor paths:
```
/sys/class/thermal/thermal_zone0/temp
/sys/class/thermal/thermal_zone1/temp
/sys/devices/system/cpu/cpu0/cpufreq/cpu_temp
/sys/class/hwmon/hwmon0/temp1_input
/sys/devices/virtual/thermal/thermal_zone*/temp
```
- **Unit**: Celsius (converted from millidegrees)
- **Update**: Polled every second via coroutine
- **Availability**: Varies by device and permissions

### Data Management
- **Coroutines**: `lifecycleScope.launch` for background temperature reading
- **Handler**: Removed in favor of chart's built-in update mechanism
- **Chart Data**: Maintains last 300 data points (5 minutes at 1-second intervals)
- **Memory Efficient**: Auto-removes old data points to prevent memory bloat

---

## 🛠️ Setup Instructions

### Prerequisites
- **Android Studio**: Hedgehog (2023.1.1) or later
- **JDK**: Version 17 or higher
- **Android SDK**: API 21+ (with API 34 recommended)
- **Device**: Physical Android device or emulator

### Installation

1. **Clone or Download the Project**
   ```bash
   cd "Phone temp"
   ```

2. **Open in Android Studio**
   - Launch Android Studio
   - Select `File > Open`
   - Navigate to the project directory
   - Click `OK`

3. **Sync Dependencies**
   - Android Studio will prompt to sync Gradle
   - Click `Sync Now`
   - Wait for dependencies to download (MPAndroidChart from JitPack)

4. **Add SF Pro Fonts** (Already included)
   - SF Pro Regular, Medium, Semibold, and Bold are in `app/src/main/res/font/`
   - Font family is defined in `sf_pro.xml`

5. **Build the Project**
   - Select `Build > Make Project` or press `Ctrl+F9` (Windows/Linux) / `Cmd+F9` (Mac)
   - Ensure no build errors appear

6. **Run on Device**
   - Connect Android device via USB with **Developer Options** and **USB Debugging** enabled
   - Or start an Android emulator (API 21+)
   - Click the green `Run` button or press `Shift+F10`
   - Select your target device

---

## 📂 Project Structure

```
Phone temp/
├── app/
│   ├── src/main/
│   │   ├── java/com/heatsense/app/
│   │   │   ├── MainActivity.kt                    # Main activity & temperature logic
│   │   │   └── views/
│   │   │       ├── CircularGaugeView.kt          # Custom circular gauge
│   │   │       ├── TemperatureChartView.kt       # Timeline chart wrapper
│   │   │       └── GlassmorphicView.kt           # Glassmorphic effect view
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   │   └── activity_main.xml             # Main UI layout
│   │   │   ├── drawable/
│   │   │   │   ├── cyberpunk_background.xml      # Pure black background
│   │   │   │   ├── glassmorphic_card.xml         # Glassmorphic card style
│   │   │   │   └── charging_bolt.xml             # Charging icon
│   │   │   ├── font/
│   │   │   │   ├── sf_pro.xml                    # Font family definition
│   │   │   │   ├── sf_pro_regular.otf            # SF Pro Regular
│   │   │   │   ├── sf_pro_medium.otf             # SF Pro Medium
│   │   │   │   ├── sf_pro_semibold.otf           # SF Pro Semibold
│   │   │   │   └── sf_pro_bold.otf               # SF Pro Bold
│   │   │   ├── values/
│   │   │   │   ├── colors.xml                    # Cyberpunk color palette
│   │   │   │   ├── strings.xml                   # String resources
│   │   │   │   └── themes.xml                    # App theme (transparent status bar)
│   │   │   └── xml/
│   │   │       ├── backup_rules.xml
│   │   │       └── data_extraction_rules.xml
│   │   └── AndroidManifest.xml                   # App configuration
│   ├── build.gradle                              # App-level Gradle (MPAndroidChart)
│   └── proguard-rules.pro                        # ProGuard configuration
├── build.gradle                                  # Project-level Gradle
├── settings.gradle                               # Gradle settings (JitPack repo)
└── README.md                                     # This file
```

---

## 🎨 Color Palette

The app uses a carefully crafted cyberpunk color scheme:

| Color Name | Hex Code | Usage |
|------------|----------|-------|
| Pure Black | `#000000` | Background |
| Deep Obsidian | `#0A0E14` | Card backgrounds |
| Cyber Cyan | `#00D9FF` | Cool temperatures, accents |
| Neon Amber | `#FFB800` | Warm temperatures, warnings |
| Crimson Red | `#FF3366` | Hot temperatures, alerts |
| Off-White | `#E8E8E8` | Primary text |
| Silver Gray | `#B0B0B0` | Secondary text |
| Charcoal | `#1A1F2E` | Tertiary elements |

---

## ⚠️ Important Notes

### Permissions
- The app reads system files in `/sys/class/thermal/` for CPU temperature
- No special permissions are requested (uses standard file I/O)
- On non-rooted devices, some thermal zones may not be accessible

### Device Compatibility

✅ **Battery Temperature**: Works on **all Android devices** (uses official Android API)

⚠️ **CPU Temperature**: 
- Depends on device manufacturer and thermal sensor exposure
- May show "N/A" or "0°C" on devices with restricted thermal access
- Works best on devices with accessible thermal zones
- Root access is **not required** but may improve sensor access

### Performance
- **Lightweight**: Minimal CPU and memory usage
- **Battery Efficient**: Pure black OLED-optimized UI
- **Smooth**: 60 FPS animations and graph updates
- **Stable**: Simplified rendering for crash-free operation

---

## 🐛 Troubleshooting

### CPU Temperature Shows "N/A" or "0°C"
- **Normal behavior** on many devices due to restricted thermal sensor access
- The app tries multiple sensor paths but may not have permission
- Try different Android devices or custom ROMs with exposed thermal APIs

### App Crashes on Startup
- Ensure minimum Android 5.0 (API 21)
- Check Logcat for specific error messages
- Try `Build > Clean Project` then `Build > Rebuild Project`

### Graph Not Updating
- Ensure the app is in the foreground (background monitoring not implemented)
- Check if CPU temperature is being read (may affect graph if no data)

### Font Not Displaying Correctly
- Ensure all SF Pro font files are in `app/src/main/res/font/`
- Rebuild the project to refresh resources

### Build Errors
- Sync Gradle files: `File > Sync Project with Gradle Files`
- Check internet connection (MPAndroidChart downloads from JitPack)
- Update Android Studio to the latest version
- Invalidate caches: `File > Invalidate Caches / Restart`

---

## 🔮 Future Enhancements

- [ ] **Gamer Mode Overlay**: Floating widget for in-game monitoring
- [ ] **Temperature Alerts**: Notifications when thresholds are exceeded
- [ ] **Historical Data**: Save and view temperature history over days/weeks
- [ ] **Export Data**: CSV export for analysis
- [ ] **Multiple Temperature Units**: Fahrenheit and Kelvin support
- [ ] **Background Service**: Monitor temperatures while app is minimized
- [ ] **Home Screen Widget**: Quick glance temperature display
- [ ] **Customizable Themes**: Additional color schemes (Neon Purple, Matrix Green, etc.)
- [ ] **Advanced Graphs**: Zoom, pan, and detailed timeline analysis
- [ ] **Thermal Throttling Detection**: Identify when device is throttling performance

---

## 📄 License

This project is open source and available under the **MIT License**.

```
MIT License

Copyright (c) 2025 HeatSense

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

---

## 🙏 Acknowledgments

- **MPAndroidChart**: Powerful charting library by Philipp Jahoda
- **SF Pro Font**: Apple's San Francisco typeface
- **Cyberpunk Aesthetic**: Inspired by futuristic UI/UX design trends
- **Android Community**: For thermal sensor documentation and best practices

---

## 📞 Support

If you encounter issues or have suggestions:
1. Check the **Troubleshooting** section above
2. Review the **Technical Details** for implementation specifics
3. Open an issue on the project repository (if applicable)

---

**⚠️ Disclaimer**: This app is designed for monitoring purposes only. Extreme temperatures may indicate hardware issues that should be addressed by a professional. HeatSense is not responsible for any damage to devices.

---

**Built with ❤️ and Kotlin | Powered by Cyberpunk Aesthetics**
