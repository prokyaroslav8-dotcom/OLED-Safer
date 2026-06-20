package com.example.oledsafer

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.Gravity
import android.view.View
import android.view.WindowManager

class OverlayService : Service() {

    private lateinit var windowManager: WindowManager
    private var topOverlayView: View? = null
    private var bottomOverlayView: View? = null

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        showOverlays()
    }

    private fun showOverlays() {
        val statusBarHeightId = resources.getIdentifier("status_bar_height", "dimen", "android")
        val statusBarHeight = if (statusBarHeightId > 0) resources.getDimensionPixelSize(statusBarHeightId) else 0
        
        val safeGap = (4 * resources.displayMetrics.density).toInt()

        val topBarHeightPx = (200 * resources.displayMetrics.density).toInt()
        val bottomBarHeightPx = (97 * resources.displayMetrics.density).toInt()

        val bottomParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            bottomBarHeightPx,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.BOTTOM or Gravity.FILL_HORIZONTAL
            x = 0
            y = 0
        }

        bottomOverlayView = View(this).apply { setBackgroundColor(Color.BLACK) }
        windowManager.addView(bottomOverlayView, bottomParams)

        val topParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            topBarHeightPx,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.FILL_HORIZONTAL
            x = 0
            y = statusBarHeight + safeGap
        }

        topOverlayView = View(this).apply { setBackgroundColor(Color.BLACK) }
        windowManager.addView(topOverlayView, topParams)
    }

    override fun onDestroy() {
        super.onDestroy()
        topOverlayView?.let { windowManager.removeView(it) }
        bottomOverlayView?.let { windowManager.removeView(it) }
    }
}
