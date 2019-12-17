package com.durukanYukselKaragoz.EasyReminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;

public class RestartAlarmsReceiver extends BroadcastReceiver {

    DatabaseHelper dbHelper;
    ArrayList<Event> events;
    @Override
    public void onReceive(Context context, Intent intent) {

        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            dbHelper = new DatabaseHelper(context);
            events = (ArrayList) EventDB.getAllEvent(dbHelper);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent notificationIntent = new Intent(context, AlarmReceiver.class);

            for(int i = 0; i<events.size(); i++){
                LocalDateTime dummy = LocalDateTime.of(events.get(i).getYear(), events.get(i).getMonth()+1, events.get(i).getDay(), events.get(i).getHour(), events.get(i).getMinute(), 0);
                if(dummy.isAfter(LocalDateTime.now())) {
                    int requestCode = (int) System.currentTimeMillis(); //RequestCode
                    PendingIntent broadcast = PendingIntent.getBroadcast(context, requestCode, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    Calendar calendarToSchedule = Calendar.getInstance();
                    calendarToSchedule.setTimeInMillis(System.currentTimeMillis());
                    calendarToSchedule.clear();
                    calendarToSchedule.set(events.get(i).getYear(), events.get(i).getMonth(), events.get(i).getDay(), events.get(i).getHour(), events.get(i).getMinute(), 0);
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendarToSchedule.getTimeInMillis(), broadcast);
                }
            }

        }
    }
}
