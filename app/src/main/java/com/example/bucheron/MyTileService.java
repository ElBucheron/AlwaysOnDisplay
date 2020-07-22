package com.example.bucheron;

import android.service.quicksettings.TileService;
import android.service.quicksettings.Tile;
import android.util.Log;

public class MyTileService extends TileService {

    public static final String TAG = MyTileService.class.getSimpleName();

    @Override
    public void onTileAdded() {
        Log.d(TAG, "onTileAdded: ");
    }

    @Override
    public void onStartListening() {

        Tile tile = getQsTile();
        Log.d(TAG, "onStartListening: "+tile.getLabel());
    }

    @Override
    public void onClick() {
        Log.d(TAG, "onClick: ");

        if (!isSecure()) {

            showDialog();

        } else {

            unlockAndRun(new Runnable() {
                @Override
                public void run() {

                    showDialog();
                }
            });
        }
    }

    private void showDialog() {

        showDialog(TileDialog.getDialog(this));
    }

}
