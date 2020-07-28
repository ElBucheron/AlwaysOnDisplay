package com.example.bucheron;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

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

                        Intent intent = new Intent(context, AlarmAction.class);
                        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, futurDate.getTimeInMillis(), sender);

                        Toast.makeText(context, "Alarm set for " + hourOfDay + ":" + minute, Toast.LENGTH_LONG).show();
                    }
                }, mHour, mMinute, false);

        return timePickerDialog;

//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setTitle("Alarm");
//        Random random = new Random();
//        builder.setMessage(Constants.facts[random.nextInt(6)]);
//        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//                dialogInterface.dismiss();
//            }
//        });
//
//        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                dialogInterface.dismiss();
//            }
//        });
//
//        return builder.create();
    }
}

