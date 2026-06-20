package com.example.oledsafer

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent

class AppAccessibilityService : AccessibilityService() {

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event?.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            val packageName = event.packageName?.toString() ?: return
            val prefs = PreferencesManager(this)
            
            if (prefs.isActive() && prefs.getTargetApps().contains(packageName)) {
                OverlayService.start(this)
            } else {
                OverlayService.stop(this)
            }
        }
    }

    override fun onInterrupt() {}
}
