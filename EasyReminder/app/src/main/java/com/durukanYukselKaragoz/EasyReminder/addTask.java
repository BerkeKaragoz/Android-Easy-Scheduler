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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class addTask extends AppCompatActivity {

    private TextView textATDate, textATHourmin, textATDetail, textATName;
    private Spinner textATType;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    DatabaseHelper dbHelper;
    String thisName, thisDetail, thisType;
    int thisYear, thisMonth, thisDay, thisHour, thisMin;

    //JsonRelated variables
    String jsonStr;
    JSONObject eventJSONObject;
    JSONArray eventTypes;

    //Tag variables
    public static final String TAG_EVENTTYPES = "eventTypes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        dbHelper = new DatabaseHelper(addTask.this);
        textATDate = findViewById(R.id.textATDate);
        textATHourmin = findViewById(R.id.textAThourmin);
        textATDetail = findViewById(R.id.editTextATDetails);
        textATType = findViewById(R.id.spinnerEventType);
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

        //Spinner on item selected
        textATType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                thisType = textATType.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
        readEventTypesFromAssets();
    }

    //Reads Json Objects from a previously created Json File
    public void readEventTypesFromAssets(){
        jsonStr = loadFileFromAssets("eventTypes.json");
        if (jsonStr != null) {
            try {
                eventJSONObject = new JSONObject(jsonStr);
                eventTypes = eventJSONObject.getJSONArray(TAG_EVENTTYPES);
                List<String> listist = new ArrayList<String>();

                // looping through all eventTypes
                for (int i = 0; i < eventTypes.length(); i++) {
                    listist.add("" + eventTypes.get(i));
                }

                //put Strings in the spinner
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listist);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                textATType.setAdapter(adapter);

            } catch (JSONException ee) {
                ee.printStackTrace();
            }
        }
    }
    //Retrieves the file from the assets folder
    private String loadFileFromAssets(String fileName) {
        String file = null;
        try {

            InputStream is = getBaseContext().getAssets().open(fileName);

            int size = is.available();
            byte[] buffer = new byte[size];

            is.read(buffer);
            is.close();

            file = new String(buffer, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return file;
    }

    public void finish(View view) {
        if(checkEmptyFields()) {
            thisName = textATName.getText().toString();
            thisDetail = textATDetail.getText().toString();
            EventDB.insertEvent(dbHelper, thisName, thisDetail, thisType, thisYear, thisMonth, thisDay, thisHour, thisMin);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent notificationIntent = new Intent(this, AlarmReceiver.class);
            int requestCode = (int) System.currentTimeMillis(); //RequestCode
            PendingIntent broadcast = PendingIntent.getBroadcast(this, requestCode, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            Calendar calendarToSchedule = Calendar.getInstance();
            calendarToSchedule.setTimeInMillis(System.currentTimeMillis());
            calendarToSchedule.clear();
            calendarToSchedule.set(thisYear, thisMonth, thisDay, thisHour, thisMin, 00);
            if(isTimeValid(calendarToSchedule)) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendarToSchedule.getTimeInMillis(), broadcast);
                Toast.makeText(this, "Task successfully added!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Date entered is invalid", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Every field must be filled!", Toast.LENGTH_LONG).show();
        }
    }

    public boolean checkEmptyFields(){
        if(textATName.getText().toString().isEmpty() && textATDetail.getText().toString().isEmpty() && textATDate.toString().isEmpty() && textATHourmin.toString().isEmpty()){
            return false;
        }
        return true;
    }
    public boolean isTimeValid(Calendar calendar){
        Calendar now = Calendar.getInstance();
        if(calendar.before(now) || calendar.equals(now)){
            return false;
        }
        return true;
    }
}
