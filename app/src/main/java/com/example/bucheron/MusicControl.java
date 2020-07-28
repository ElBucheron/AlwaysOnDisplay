package com.example.bucheron;

import android.content.Context;
import android.media.AudioManager;
import android.view.KeyEvent;

public class MusicControl {
    public static void play(Context context) {
        sendButton(context, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE);
    }

    public static void skipNext(Context context) {
        sendButton(context, KeyEvent.KEYCODE_MEDIA_NEXT);
    }

    public static void skipPrevious(Context context) {
        sendButton(context, KeyEvent.KEYCODE_MEDIA_PREVIOUS);
    }

    private static void sendButton(Context context, int keycode) {
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        KeyEvent downEvent = new KeyEvent(KeyEvent.ACTION_DOWN, keycode);
        am.dispatchMediaKeyEvent(downEvent);
        KeyEvent upEvent = new KeyEvent(KeyEvent.ACTION_UP, keycode);
        am.dispatchMediaKeyEvent(upEvent);
    }
}
