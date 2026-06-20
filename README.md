# 📱 OLED Safer (English)
> **Ultimate screen burn-in protection with a native AOSP feel.**

![Build Status](https://img.shields.io/github/actions/workflow/status/YourName/OLEDSafer/build.yml?style=for-the-badge)
![Android Version](https://img.shields.io/badge/Android-6.0%2B-3DDC84?style=for-the-badge&logo=android)
![Material You](https://img.shields.io/badge/Monet-Supported-F4A261?style=for-the-badge)
![License](https://img.shields.io/badge/License-MIT-blue.svg?style=for-the-badge)

**OLED Safer** is an open-source utility designed to prevent permanent screen burn-in on Android devices. It dynamically overlays customizable black bars on the top and bottom of your screen. Built with a minimalist, terminal-clean aesthetic and full Material You (Monet) support, it looks and feels like it belongs directly in custom ROMs like LineageOS or Evolution X.

---

## ✨ Key Features

- **🎯 Smart Auto-Activation**: Automatically turns on the protection bars only when specific, user-selected apps are running in the foreground.
- **🎨 AOSP & Monet Design**: A beautiful, minimalist interface that dynamically adapts to your system colors.
- **⚙️ Deep Customization**:
  - Adjust top and bottom bar height independently (in `dp`).
  - Set custom opacity levels.
- **👆 Smart Touch Handling**: Tap the bars to temporarily hide them for 3, 5, or 10 seconds.
- **⚡ Quick Settings (QS) Tile**: Toggle global protection right from your notification panel.
- **🔋 Battery Friendly**: Pure black pixels mean OLED pixels are turned off, saving battery.

---

## 🛠️ How It Works

| Feature | Mechanism |
| :--- | :--- |
| **Overlays** | Uses `SYSTEM_ALERT_WINDOW` to draw pure black shapes over UI elements. |
| **App Detection** | Leverages `AccessibilityService` to efficiently detect foreground package changes without polling. |
| **Persistence** | Lightweight background service ensures the bars stay active when needed. |

---

## 🚀 Installation

We use **GitHub Actions** to automatically build and provide the latest APKs.

1. Go to the [Releases](../../releases) tab.
2. Download the latest `app-release.apk`.
3. Install the APK on your Android device. *(You may need to allow installation from unknown sources).*

> **Note:** On first launch, the app will prompt you to grant **Display over other apps** and **Accessibility** permissions. These are strictly required for the app to function.

---

## 💻 Build it yourself

Want to compile the code on your own machine? It's easy!

```bash
# Clone the repository
git clone [https://github.com/YourName/OLEDSafer.git](https://github.com/YourName/OLEDSafer.git)

# Navigate into the directory
cd OLEDSafer

# Build the release APK using Gradle
./gradlew assembleRelease
