# HeatSense | Thermal Monitor

![Android](https://img.shields.io/badge/Platform-Android-3DDC84?style=flat&logo=android)
![Kotlin](https://img.shields.io/badge/Language-Kotlin-7F52FF?style=flat&logo=kotlin)
![UI](https://img.shields.io/badge/UI-Android_Views-3DDC84?style=flat&logo=android)
![License](https://img.shields.io/badge/License-MIT-blue.svg)
[![GitHub Release](https://img.shields.io/github/v/release/0xIvaR/HeatSense?style=flat&logo=github)](https://github.com/0xIvaR/HeatSense/releases/latest)

## 📥 Download

<a href="https://github.com/0xIvaR/HeatSense/releases/latest/download/HeatSense-v1.0.apk">
  <img src="https://img.shields.io/badge/Download-APK-green?style=for-the-badge&logo=android" alt="Download APK">
</a>

> **Note**: Enable "Install from unknown sources" in your Android settings to install the APK.

---

**HeatSense** is a high-performance thermal monitoring dashboard for Android that provides real-time tracking of CPU and battery temperatures. Built for gamers and power users who need precise thermal data, it features a stunning cyberpunk-themed interface powered by Kotlin and custom Android Views.

---

## 📱 Screenshots

| Portrait View | Landscape View |
| :---: | :---: |
| <img src="https://github.com/user-attachments/assets/bd37caf5-5f5f-4417-bed3-ab318b462db7" height="400"> | <img src="https://github.com/user-attachments/assets/032ee6be-ead0-48ae-8643-fd82e8e20ecb" height="400"> |

---

## ✨ Key Features

- **Dual Real-Time Gauges**: Instant, animated visual readouts for **CPU Core** and **Battery Pack** temperatures with vibrant color-coded gradients.
- **Live Timeline Graph**: A dynamic, auto-updating chart visualizing thermal history over the last 5 minutes with smooth Bézier curves.
- **Battery Stats & Peak Session**: Comprehensive power monitoring showing current battery level, charging status, and the single highest **Peak Temperature** recorded during your session.
- **Modern Dark UI**: A professional, immersive dark theme featuring glassmorphism, neon accents, and Apple's SF Pro typography for maximum readability and comfort.

---

## 🛠️ Tech Stack

- **Language**: [Kotlin](https://kotlinlang.org/)
- **UI Framework**: Android Views (XML Layouts) & Custom Views
- **Architecture**: MVVM (Model-View-ViewModel) pattern
- **Concurrency**: Kotlin Coroutines & Flow for asynchronous sensor reading
- **Charting**: [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart) for data visualization

---

## 🚀 Getting Started

Follow these instructions to get a local copy of the project up and running.

### Prerequisites
- **Android Studio**: Hedgehog (2023.1.1) or newer
- **JDK**: Version 17
- **Android SDK**: API 21+ (Android 5.0 Lollipop or higher)

### Installation Steps

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/heatsense.git
   ```

2. **Open in Android Studio**
   - Launch Android Studio
   - Select **File > Open**
   - Navigate to the cloned directory and select it

3. **Build and Run**
   - Wait for Gradle sync to complete
   - Connect your Android device (USB debugging enabled) or start an emulator
   - Press **Shift + F10** or click the green **Run** button

---

## 📦 Building Release APK

To build a signed release APK:

1. Open **Build > Generate Signed Bundle / APK** in Android Studio
2. Select **APK** and click Next
3. Create a new keystore or use an existing one
4. Fill in the key details and click Next
5. Select **release** build variant
6. Click **Create** to generate the APK

The signed APK will be in `app/release/` folder.

---

## 📖 Usage Guide

The main dashboard is divided into three intuitive sections:

1. **Top Gauges**: 
   - The **Left Gauge** displays the current CPU Core temperature.
   - The **Right Gauge** shows the current Battery Pack temperature.
   - Colors shift from Cyan (Cool) to Amber/Red (Hot) as temperatures rise.

2. **Status Card**:
   - Located below the gauges, this glassmorphic card displays your current **Battery Level** and **Charging Status**.
   - The **Peak Session** value tracks the single highest temperature spike detected since you opened the app.

3. **Timeline Graph**:
   - The bottom chart plots a continuous **5-minute history** of your thermal performance.
   - Updates every second to help you identify heating trends during gaming or heavy workloads.

---

## 📄 License

This project is licensed under the **MIT License** - see the [LICENSE](LICENSE) file for details.
