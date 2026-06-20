package com.example.oledsafer

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.view.accessibility.AccessibilityEvent

class AppAccessibilityService : AccessibilityService() {

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            val currentPackage = event.packageName?.toString()
            val intent = Intent(this, OverlayService::class.java)
            
            if (currentPackage == "com.zhiliaoapp.musically" || currentPackage == "com.ss.android.ugc.trill") {
                startService(intent)
            } else {
                stopService(intent)
            }
        }
    }

    override fun onInterrupt() {}
}
