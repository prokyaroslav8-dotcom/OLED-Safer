package com.example.oledsafer

import android.content.Intent
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService

class QSTileService : TileService() {

    override fun onClick() {
        super.onClick()
        val intent = Intent(this, OverlayService::class.java)
        
        if (qsTile.state == Tile.STATE_ACTIVE) {
            stopService(intent)
            qsTile.state = Tile.STATE_INACTIVE
        } else {
            startService(intent)
            qsTile.state = Tile.STATE_ACTIVE
        }
        qsTile.updateTile()
    }
}
