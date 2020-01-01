package com.durukanYukselKaragoz.EasyReminder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.threetenabp.AndroidThreeTen;

import org.threeten.bp.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

public class SecondActivity extends AppCompatActivity {
    ArrayList<Event> events;
    DatabaseHelper dbHelper;
    ArrayList<LocalDateTime> times;
    TextView tvName, tvDetail, tvDate, tvTaskType;
    Event foundEvent;
    ImageFragment imageFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        AndroidThreeTen.init(this);
        dbHelper = new DatabaseHelper(this);
        events = (ArrayList) EventDB.getAllEvent(dbHelper);
        times = new ArrayList<>();
        tvDate = findViewById(R.id.tvDate);
        tvTaskType = findViewById(R.id.tvTaskType);
        tvName = findViewById(R.id.tvName);
        tvDetail = findViewById(R.id.tvDetail);

        for(int i = 0; i<events.size(); i++){
            Event event = events.get(i);
            LocalDateTime aDatetime = LocalDateTime.of(event.getYear(), event.getMonth()+1, event.getDay(), event.getHour(), event.getMinute(), 0);
            times.add(aDatetime);
        }

        Collections.sort(times);
        LocalDateTime finalDate = findDate(times);
        if(finalDate != null) {
            foundEvent = findEventByTime(finalDate, events);
            tvDetail.setText(foundEvent.getEventDetail());
            tvName.setText(foundEvent.getEventName());
            tvTaskType.setText(foundEvent.getEventType());
            String date = foundEvent.getYear() + "/" + foundEvent.getMonth() + "/" + foundEvent.getDay() + "    " + String.format("%02d", foundEvent.getHour()) + ":" + String.format("%02d", foundEvent.getMinute());
            tvDate.setText(date);
        }
        if(imageFragment == null){
            imageFragment = (ImageFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        }

        imageFragment.changeImage(foundEvent.getEventType());
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
    public Event findEventByTime(LocalDateTime time, ArrayList<Event> events){
        for(int i = 0; i<events.size(); i++){
            Event event = events.get(i);
            LocalDateTime dummy = LocalDateTime.of(event.getYear(), event.getMonth()+1, event.getDay(), event.getHour(), event.getMinute(), 0);
            if(dummy.equals(time)){
                return events.get(i);
            }
        }
        return null;
    }
}
