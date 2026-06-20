package com.example.oledsafer

import android.os.Build
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.N)
class QSTileService : TileService() {

    override fun onStartListening() {
        super.onStartListening()
        updateTileState()
    }

    override fun onClick() {
        super.onClick()
        val prefs = PreferencesManager(this)
        prefs.setActive(!prefs.isActive())
        updateTileState()
        
        if (!prefs.isActive()) {
            OverlayService.stop(this)
        }
    }

    private fun updateTileState() {
        val prefs = PreferencesManager(this)
        val tile = qsTile
        if (tile != null) {
            tile.state = if (prefs.isActive()) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
            tile.updateTile()
        }
    }
}
