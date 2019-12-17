package com.durukanYukselKaragoz.EasyReminder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class SecondActivity extends AppCompatActivity {
    ArrayList<Event> events;
    DatabaseHelper dbHelper;
    ArrayList<LocalDateTime> times;
    TextView tvName, tvDetail, tvDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        dbHelper = new DatabaseHelper(this);
        events = (ArrayList) EventDB.getAllEvent(dbHelper);
        times = new ArrayList<>();
        tvDate = findViewById(R.id.tvDate);
        for(int i = 0; i<events.size(); i++){
            Event event = events.get(i);
            LocalDateTime aDatetime = LocalDateTime.of(event.getYear(), event.getMonth()+1, event.getDay(), event.getHour(), event.getMinute(), 0);
            times.add(aDatetime);
        }
        Collections.sort(times);
        LocalDateTime finalDate = findDate(times);
        if(finalDate != null)
         tvDate.setText(finalDate.toString());

    }
    public LocalDateTime findDate(ArrayList<LocalDateTime> list){
        for(int i = 0; i<list.size(); i++){
            if(list.get(i).isBefore(LocalDateTime.now())){

            } else {
                return list.get(i-1);
            }
        }
        return list.get(list.size()-1);
    }
}
