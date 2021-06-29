package com.example.bucheron;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Objects;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class TileDialog {

    public static TimePickerDialog getDialog(final Context context) {

        // Get Current Time
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                        Calendar futurDate = Calendar.getInstance();
                        futurDate.setTimeInMillis(System.currentTimeMillis());
                        futurDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        futurDate.set(Calendar.MINUTE, minute);
                        futurDate.set(Calendar.SECOND, 0);

                        // For a time to set for the next day
                        long timeUntilTrigger;
                        if (System.currentTimeMillis() > futurDate.getTimeInMillis()){
                            timeUntilTrigger = futurDate.getTimeInMillis() + 86400000;
                        }else{
                            timeUntilTrigger = futurDate.getTimeInMillis();
                        }

                        Intent intent = new Intent(context, AlarmAction.class);
                        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeUntilTrigger, sender);

                        createNotificationChannel(context);
                        showNotification(context, hourOfDay, minute);

                        Toast.makeText(context, "Alarm set for " + hourOfDay + ":" + minute, Toast.LENGTH_LONG).show();
                    }
                }, mHour, mMinute, false);

        return timePickerDialog;
    }

    private static void createNotificationChannel(Context context) {
        // Créer le NotificationChannel, seulement pour API 26+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notification channel name";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("CHANNEL_ID", name, importance);
            channel.setDescription("Notification channel description");
            // Enregister le canal sur le système : attention de ne plus rien modifier après
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            Objects.requireNonNull(notificationManager).createNotificationChannel(channel);
        }
    }

    public static void showNotification(Context context, int hour, int minute) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(context, "CHANNEL_ID")
                .setSmallIcon(R.drawable.ic_baseline_add_alarm_24)
                .setContentTitle("Alarm")
                .setContentText("Set for " + hour + ":" + minute)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setOngoing(true);

        // notificationId est un identificateur unique par notification qu'il vous faut définir
        notificationManager.notify(1, notifBuilder.build());
    }
}

