package com.example.bucheron;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmAction extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        MusicControl.play(context);
        Toast.makeText(context, "Alarm time, playing music then !", Toast.LENGTH_LONG).show();
    }
}
