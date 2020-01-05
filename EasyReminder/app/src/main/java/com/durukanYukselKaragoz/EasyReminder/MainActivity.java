package com.durukanYukselKaragoz.EasyReminder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.jakewharton.threetenabp.AndroidThreeTen;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {
    //Variables
    DatabaseHelper dbHelper;
    Intent intent;
    private ImageView volleyImage;
    RecyclerView recyclerAll, recyclerToday;
    MyRecyclerViewAdapter adapter;
    MyRecyclerViewAdapter2 adapter2;
    ArrayList<Event> events, allEvents, todaysEvents;
    //Gesture Related
    private GestureDetectorCompat mDetector;
    //Volley Related
    OurVolley ourVolley;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getSupportActionBar().hide();
        //Hide notification bar
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

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

        MyGestureListener mgl = new MyGestureListener();
        mDetector = new GestureDetectorCompat(this, mgl);
        mDetector.setOnDoubleTapListener(mgl); // add OnDoubleTapListener to gesture detector object to catch doubletab gesture!!!!!

        ourVolley = new OurVolley(this);
        volleyImage = findViewById(R.id.doubleClickImage);

        volleyImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //return MainActivity.this.mDetector.onTouchEvent(motionEvent);
                return mDetector.onTouchEvent(motionEvent);
            }
        });
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
        ArrayList<LocalDateTime> dates = new ArrayList<>();
        for(int i = 0; i<list.size(); i++){
            Event event = list.get(i);
            LocalDateTime aDatetime = LocalDateTime.of(event.getYear(), event.getMonth()+1, event.getDay(), event.getHour(), event.getMinute(), 0);
            if(aDatetime.isAfter(LocalDateTime.now())){
                holder.add(event);
                dates.add(aDatetime);
            }
        }
        return bubbleSort(dates, holder);
    }

    @Override
    protected void onResume() {
        super.onResume();
        callRefresh();
    }
    public ArrayList<Event> getTodaysTasks(ArrayList<Event> list){
        ArrayList<Event> holder = new ArrayList<>();
        ArrayList<LocalDateTime> dates = new ArrayList<>();
        LocalDateTime today = LocalDateTime.now();
        int year = today.getYear();
        int month = today.getMonthValue();
        int day = today.getDayOfMonth();

        for(int i = 0; i<list.size(); i++){
            Event event = list.get(i);
            LocalDateTime aDatetime = LocalDateTime.of(event.getYear(), event.getMonth()+1, event.getDay(), event.getHour(), event.getMinute(), 0);
            if(aDatetime.getYear() == year && aDatetime.getMonthValue() == month && aDatetime.getDayOfMonth() == day){
                holder.add(event);
                dates.add(aDatetime);
            }
        }
        return bubbleSort(dates, holder);
    }
    public ArrayList<Event> bubbleSort(ArrayList<LocalDateTime> dates, ArrayList<Event> eventSorted) {
        int i, j;
        for (i = 0; i < dates.size(); i++)

            // Last i elements are already in place
            for (j = 0; j < dates.size()-i-1; j++)
                if (dates.get(j).isAfter(dates.get(j+1))){
                    Collections.swap(dates, j, j+1);
                    Collections.swap(eventSorted, j, j+1);
                }
        return eventSorted;
    }

    public void setBitmapImage(Bitmap response) {
        volleyImage.setImageBitmap(response);
        Toast.makeText(MainActivity.this, "IMAGE LOADED",
                Toast.LENGTH_SHORT).show();

    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent event) {
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent event) {
            return true;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent event) {
            ourVolley.requestForBinaryData("https://worshiphousemedia.s3.amazonaws.com/images/main/s/mo/ora/mo/classynewyearwelcomemotion.jpg");
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);

        }
    }

}
