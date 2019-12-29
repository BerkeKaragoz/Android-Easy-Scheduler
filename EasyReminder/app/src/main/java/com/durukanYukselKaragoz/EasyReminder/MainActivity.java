package com.durukanYukselKaragoz.EasyReminder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.jakewharton.threetenabp.AndroidThreeTen;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    //Variables
    DatabaseHelper dbHelper;
    Intent intent;
    RecyclerView recyclerAll, recyclerToday;
    MyRecyclerViewAdapter adapter;
    MyRecyclerViewAdapter2 adapter2;
    ArrayList<Event> events, allEvents, todaysEvents;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AndroidThreeTen.init(this);
        dbHelper = new DatabaseHelper(this);

        recyclerAll = findViewById(R.id.recyclerAll);
        recyclerToday = findViewById(R.id.recyclerToday);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
        recyclerAll.setLayoutManager(layoutManager);
        recyclerToday.setLayoutManager(layoutManager2);

        events = (ArrayList<Event>) EventDB.getAllEvent(dbHelper);
        allEvents = new ArrayList<>();
        allEvents = sortEvents(events);
        adapter = new MyRecyclerViewAdapter(this, allEvents);
        recyclerAll.setAdapter(adapter);

        todaysEvents = new ArrayList<>();
        todaysEvents = getTodaysTasks(events);
        adapter2 = new MyRecyclerViewAdapter2(this, todaysEvents);
        recyclerToday.setAdapter(adapter2);

    }

    public void onClick(View view) {
        intent = new Intent(this, addTask.class);
        startActivity(intent);
    }
    public void callRefresh(){
        adapter = new MyRecyclerViewAdapter(this, sortEvents((ArrayList)EventDB.getAllEvent(dbHelper)));
        recyclerAll.setAdapter(adapter);

        adapter2 = new MyRecyclerViewAdapter2(this, getTodaysTasks((ArrayList)EventDB.getAllEvent(dbHelper)));
        recyclerToday.setAdapter(adapter2);

    }

    public ArrayList<Event> sortEvents(ArrayList<Event> list){
        ArrayList<Event> holder = new ArrayList<>();
        for(int i = 0; i<list.size(); i++){
            Event event = list.get(i);
            LocalDateTime aDatetime = LocalDateTime.of(event.getYear(), event.getMonth()+1, event.getDay(), event.getHour(), event.getMinute(), 0);
            if(aDatetime.isAfter(LocalDateTime.now())){
                holder.add(event);
            }
        }
        return holder;
    }

    @Override
    protected void onResume() {
        super.onResume();
        callRefresh();
    }
    public ArrayList<Event> getTodaysTasks(ArrayList<Event> list){
        ArrayList<Event> holder = new ArrayList<>();
        LocalDateTime today = LocalDateTime.now();
        int year = today.getYear();
        int month = today.getMonthValue();
        int day = today.getDayOfMonth();

        for(int i = 0; i<list.size(); i++){
            Event event = list.get(i);
            LocalDateTime aDatetime = LocalDateTime.of(event.getYear(), event.getMonth()+1, event.getDay(), event.getHour(), event.getMinute(), 0);
            if(aDatetime.getYear() == year && aDatetime.getMonthValue() == month && aDatetime.getDayOfMonth() == day){
                holder.add(event);
            }
        }
        return holder;
    }
}
