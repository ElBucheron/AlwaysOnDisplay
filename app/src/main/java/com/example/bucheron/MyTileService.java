package com.example.bucheron;

import android.service.quicksettings.TileService;
import android.service.quicksettings.Tile;

public class MyTileService extends TileService {

    @Override
    public void onTileAdded() {
        super.onTileAdded();

        Tile tile = getQsTile();
        // Update state
        tile.setState(Tile.STATE_INACTIVE);

        // Update looks
        tile.updateTile();
    }

    @Override
    public void onStartListening() {
        Tile tile = getQsTile();
    }

    @Override
    public void onClick() {
        super.onClick();
        Tile tile = getQsTile();
        if(tile.getState() == Tile.STATE_INACTIVE) {
            showDialog();
        }
    }

    private void showDialog() {

        showDialog(TileDialog.getDialog(this));
    }

}
