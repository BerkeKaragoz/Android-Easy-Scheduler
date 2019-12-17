package com.durukanYukselKaragoz.EasyReminder;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class addTask extends AppCompatActivity {

    private TextView textATDate, textATHourmin, textATDetail, textATName;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    DatabaseHelper dbHelper;
    String thisName, thisDetail;
    int thisYear, thisMonth, thisDay, thisHour, thisMin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        dbHelper = new DatabaseHelper(addTask.this);
        textATDate = findViewById(R.id.textATDate);
        textATHourmin = findViewById(R.id.textAThourmin);
        textATDetail = findViewById(R.id.editTextATDetails);
        textATName = findViewById(R.id.editTextATName);
        textATDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int     year    = cal.get(Calendar.YEAR),
                        month   = cal.get(Calendar.MONTH),
                        dayOfMonth     = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        addTask.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateSetListener,
                        year, month, dayOfMonth);

                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //TODO
                String date = month+1 + "/" + dayOfMonth + "/" + year;
                thisMonth = month;
                thisYear = year;
                thisDay = dayOfMonth;
                textATDate.setText(date);
            }
        };

        textATHourmin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(addTask.this, new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        textATHourmin.setText(hourOfDay + ":" + String.format("%02d", minute));
                        thisHour = hourOfDay;
                        thisMin = minute;
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

    }

    public void finish(View view) {
        thisName = textATName.getText().toString();
        thisDetail = textATDetail.getText().toString();
        EventDB.insertEvent(dbHelper, thisName, thisDetail, thisYear, thisMonth, thisDay, thisHour, thisMin);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent notificationIntent = new Intent(this, AlarmReceiver.class);
        int requestCode = (int) System.currentTimeMillis(); //RequestCode
        PendingIntent broadcast = PendingIntent.getBroadcast(this, requestCode, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar calendarToSchedule = Calendar.getInstance();
        calendarToSchedule.setTimeInMillis(System.currentTimeMillis());
        calendarToSchedule.clear();
        calendarToSchedule.set(thisYear, thisMonth, thisDay, thisHour, thisMin, 00);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendarToSchedule.getTimeInMillis(), broadcast);
    }
}
