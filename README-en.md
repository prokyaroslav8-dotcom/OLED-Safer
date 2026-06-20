# 📱 OLED Safer

<p align="center">
  <a href="https://github.com/prokyaroslav8-dotcom/oled-safer/blob/main/readme.md">Русский</a> | <b>English</b>
</p>

> **Ultimate screen burn-in protection in the true spirit of AOSP.**

![Build Status](https://img.shields.io/github/actions/workflow/status/prokyaroslav8-dotcom/oled-safer/build.yml?style=for-the-badge)
![Android Version](https://img.shields.io/badge/Android-6.0%2B-3DDC84?style=for-the-badge&logo=android)
![Material You](https://img.shields.io/badge/Monet-Supported-F4A261?style=for-the-badge)
![License](https://img.shields.io/badge/License-MIT-blue.svg?style=for-the-badge)

**OLED Safer** is an advanced open-source utility designed to prevent permanent screen burn-in on Android devices. It dynamically overlays customizable black bars on the top and bottom of your screen. Built with clean code, a minimalist aesthetic, and full **Monet** (Material You) color library support, it looks and feels like it belongs directly in custom ROMs.

Way cooler than the standard AMOLED Protect.

---

## ✨ Implemented Features

Development was strictly guided by our technical specifications. The following numbering logic is used: `.1` means a required feature (MUST), and `.2*` means an optional/desirable feature (DESIRABLE).

- **[#1.1] Smart Auto-Activation:** The app automatically enables the black bars when a specific, user-selected app is open in the foreground (precisely matching the reference layout).
  
- **[#2.2*] Customizable Geometry:** Users can adjust the thickness of the bars directly in the app. The height of the top and bottom bars can be customized independently.

- **[#3.2*] Opacity Control:** Users can easily adjust the transparency level of the bars using a built-in slider.

- **[#4.1] Smart Hide on Tap:** When tapping a bar, it can either stay visible, or temporarily hide for 3, 5, or 10 seconds (configurable by the user, defaults to 5 seconds).

- **[#5.1] System Integration:** Users can toggle the protection bars on and off globally straight from the Quick Settings (QS) panel on their Android smartphone or tablet.

---

## 🛠️ Under the Hood

| Technology | Description |
| :--- | :--- |
| **Overlays (Draw Over Apps)** | Uses `SYSTEM_ALERT_WINDOW` to draw pitch-black boxes over the system UI and games. True black physically turns off OLED pixels, saving battery. |
| **Accessibility Service** | Energy-efficient foreground window monitoring allows the app to instantly react to specific apps launching without draining the battery in the background. |
| **GitHub Actions CI/CD** | A configured `build.yml` workflow automatically compiles release APKs on every push to the `main` branch. |

---

## 🚀 Installation

This project is set up for automatic building. You don't need to compile anything manually just to test it out!

1. Go to the [Releases](../../releases) tab.
2. Download the latest `app-release-unsigned.apk`.
3. Install the APK on your device. *(Minimum requirement: Android 6.0, supports up to Android 14+).*

> **⚠️ Important:** On first launch, the app will request **Display over other apps** and **Accessibility** permissions. Without these, Android will not allow the app to track targeted foreground apps or draw the protective bars.
