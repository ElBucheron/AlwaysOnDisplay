package com.example.bucheron;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private long startTime = 0L;

    private Handler customHandler = new Handler();

    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;

    boolean running = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        final TextView timerView = (TextView)findViewById(R.id.timerView);
        TextView dateView = (TextView)findViewById(R.id.dateView);
        LinearLayout songBox = (LinearLayout)findViewById(R.id.songBox);

        String currentDateTime = java.text.DateFormat.getDateInstance().format(new Date());

        dateView.setText(currentDateTime);

        findViewById(R.id.switchTimerClock).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchTimerClock();
            }
        });

        findViewById(R.id.prevView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicControl.skipPrevious(v.getContext());
            }
        });

        songBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicControl.play(v.getContext());
            }
        });
        findViewById(R.id.nextView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicControl.skipNext(v.getContext());
            }
        });

        timerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (running){
                    timeSwapBuff += timeInMilliseconds;
                    customHandler.removeCallbacks(updateTimerThread);

                    timerView.setTextColor(Color.RED);
                    running = false;
                } else {
                    startTime = SystemClock.uptimeMillis();
                    customHandler.postDelayed(updateTimerThread, 0);

                    timerView.setTextColor(Color.WHITE);
                    running = true;
                }
            }
        });

        Thread myThread = null;

        Runnable runnable = new CountDownRunner();
        myThread= new Thread(runnable);
        myThread.start();

        IntentFilter iF = new IntentFilter();
        iF.addAction("com.android.music.metachanged");
        iF.addAction("com.android.music.playstatechanged");
        iF.addAction("com.htc.music.metachanged");
        iF.addAction("fm.last.android.metachanged");
        iF.addAction("com.sec.android.app.music.metachanged");
        iF.addAction("com.nullsoft.winamp.metachanged");
        iF.addAction("com.amazon.mp3.metachanged");
        iF.addAction("com.miui.player.metachanged");
        iF.addAction("com.real.IMP.metachanged");
        iF.addAction("com.sonyericsson.music.metachanged");
        iF.addAction("com.rdio.android.metachanged");
        iF.addAction("com.samsung.sec.android.MusicPlayer.metachanged");
        iF.addAction("com.andrew.apollo.metachanged");

        registerReceiver(this.mReceiver, iF);
//        try {
//            AudioManager manager = (AudioManager) this.getBaseContext().getSystemService(Context.AUDIO_SERVICE);
//            if (manager.isMusicActive()) {
//
//            } else {
//
//            }
//        } catch (UnsupportedOperationException e) {
//            songView.setText("Error: " + e.getMessage());
//        }

        registerReceiver(this.mBatteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    @Override
    protected void onPause() {
        // call the superclass method first
        super.onPause();

        finishAndRemoveTask();
    }

    private void switchTimerClock() {
        ImageButton switchTimerClock = (ImageButton)findViewById(R.id.switchTimerClock);
        if (findViewById(R.id.timerView).isShown()){
            findViewById(R.id.timerView).setVisibility(View.GONE);
            findViewById(R.id.hourView).setVisibility(View.VISIBLE);
            findViewById(R.id.minutesView).setVisibility(View.VISIBLE);
            switchTimerClock.setImageResource(R.mipmap.icon_timer_foreground);
        } else {
            findViewById(R.id.timerView).setVisibility(View.VISIBLE);
            findViewById(R.id.hourView).setVisibility(View.GONE);
            findViewById(R.id.minutesView).setVisibility(View.GONE);
            switchTimerClock.setImageResource(R.mipmap.icon_clock_foreground);
        }
    }

    private Runnable updateTimerThread = new Runnable() {

        public void run() {
            TextView timerView = (TextView)findViewById(R.id.timerView);

            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;

            updatedTime = timeSwapBuff + timeInMilliseconds;

            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            int milliseconds = (int) (updatedTime % 1000);
            timerView.setText("" + mins + ":"
                    + String.format("%02d", secs) + ":"
                    + String.format("%03d", milliseconds));
            customHandler.postDelayed(this, 0);
        }

    };

    public void doWork() {
        runOnUiThread(new Runnable() {
            public void run() {
                try{
                    TextView hourCurrentTime = (TextView)findViewById(R.id.hourView);
                    TextView minutesCurrentTime = (TextView)findViewById(R.id.minutesView);
                    Date dt = new Date();
                    String hours, minutes;
                    int h = dt.getHours();
                    int m = dt.getMinutes();
//                    int seconds = dt.getSeconds();
                    if (h < 10){
                        hours = "0" + h;
                    } else {
                        hours = "" + h;
                    }
                    if (m < 10){
                        minutes = "0" + m;
                    } else {
                        minutes = "" + m;
                    }
                    hourCurrentTime.setText(hours);
                    minutesCurrentTime.setText(minutes);
                }catch (Exception e) {}
            }
        });
    }

    class CountDownRunner implements Runnable{
        // @Override
        public void run() {
            while(!Thread.currentThread().isInterrupted()){
                try {
                    doWork();
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }catch(Exception e){
                }
            }
        }
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String artist = intent.getStringExtra("artist");
//            String album = intent.getStringExtra("album");
            String track = intent.getStringExtra("track");

            TextView songView = (TextView)findViewById(R.id.songView);
            songView.setText(track);
            TextView artistView = (TextView)findViewById(R.id.artistView);
            artistView.setText(artist);
        }
    };

    private BroadcastReceiver mBatteryReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            TextView batteryView = (TextView)findViewById(R.id.batteryView);
            batteryView.setText(String.valueOf(level) + "%");

            if (level < 20) {
                batteryView.setTextColor(Color.RED);
            } else {
                batteryView.setTextColor(Color.WHITE);
            }
        }
    };
}