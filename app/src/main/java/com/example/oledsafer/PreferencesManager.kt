package com.example.oledsafer

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("oled_safer_prefs", Context.MODE_PRIVATE)

    fun isActive(): Boolean = prefs.getBoolean("is_active", false)
    fun setActive(active: Boolean) = prefs.edit().putBoolean("is_active", active).apply()

    fun getTopHeight(): Int = prefs.getInt("top_height", 80)
    fun setTopHeight(height: Int) = prefs.edit().putInt("top_height", height).apply()

    fun getBottomHeight(): Int = prefs.getInt("bottom_height", 120)
    fun setBottomHeight(height: Int) = prefs.edit().putInt("bottom_height", height).apply()

    fun getOpacity(): Float = prefs.getFloat("opacity", 1.0f)
    fun setOpacity(opacity: Float) = prefs.edit().putFloat("opacity", opacity).apply()

    fun getHideDuration(): Int = prefs.getInt("hide_duration", 5)
    fun setHideDuration(seconds: Int) = prefs.edit().putInt("hide_duration", seconds).apply()

    fun getTargetApps(): Set<String> = prefs.getStringSet("target_apps", emptySet()) ?: emptySet()
    fun addTargetApp(packageName: String) {
        val apps = getTargetApps().toMutableSet()
        apps.add(packageName)
        prefs.edit().putStringSet("target_apps", apps).apply()
    }
    fun removeTargetApp(packageName: String) {
        val apps = getTargetApps().toMutableSet()
        apps.remove(packageName)
        prefs.edit().putStringSet("target_apps", apps).apply()
    }
}
