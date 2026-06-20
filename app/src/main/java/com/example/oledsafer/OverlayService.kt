package com.example.oledsafer

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout

class OverlayService : Service() {

    private lateinit var windowManager: WindowManager
    private var topView: View? = null
    private var bottomView: View? = null
    private lateinit var prefs: PreferencesManager
    private val handler = Handler(Looper.getMainLooper())

    companion object {
        private var isRunning = false

        fun start(context: Context) {
            if (!isRunning) {
                context.startService(Intent(context, OverlayService::class.java))
            } else {
                context.sendBroadcast(Intent("UPDATE_OLED_OVERLAY"))
            }
        }

        fun stop(context: Context) {
            context.stopService(Intent(context, OverlayService::class.java))
        }
    }

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        prefs = PreferencesManager(this)
        isRunning = true
        showOverlays()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        updateOverlays()
        return START_STICKY
    }

    private fun getLayoutParams(gravity: Int, heightDp: Int): WindowManager.LayoutParams {
        val heightPx = (heightDp * resources.displayMetrics.density).toInt()
        val type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_PHONE
        }
        
        return WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            heightPx,
            type,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            PixelFormat.TRANSLUCENT
        ).apply {
            this.gravity = gravity
        }
    }

    private fun setupInteraction(view: View) {
        view.setOnClickListener {
            val duration = prefs.getHideDuration()
            if (duration > 0) {
                view.alpha = 0f
                handler.postDelayed({
                    view.alpha = prefs.getOpacity()
                }, duration * 1000L)
            }
        }
    }

    private fun showOverlays() {
        if (topView == null) {
            topView = FrameLayout(this).apply { setBackgroundColor(Color.BLACK) }
            setupInteraction(topView!!)
            windowManager.addView(topView, getLayoutParams(Gravity.TOP, prefs.getTopHeight()))
        }
        if (bottomView == null) {
            bottomView = FrameLayout(this).apply { setBackgroundColor(Color.BLACK) }
            setupInteraction(bottomView!!)
            windowManager.addView(bottomView, getLayoutParams(Gravity.BOTTOM, prefs.getBottomHeight()))
        }
        updateOverlays()
    }

    private fun updateOverlays() {
        topView?.let {
            it.alpha = prefs.getOpacity()
            windowManager.updateViewLayout(it, getLayoutParams(Gravity.TOP, prefs.getTopHeight()))
        }
        bottomView?.let {
            it.alpha = prefs.getOpacity()
            windowManager.updateViewLayout(it, getLayoutParams(Gravity.BOTTOM, prefs.getBottomHeight()))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        topView?.let { windowManager.removeView(it) }
        bottomView?.let { windowManager.removeView(it) }
        topView = null
        bottomView = null
        isRunning = false
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
