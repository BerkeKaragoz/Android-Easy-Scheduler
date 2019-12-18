package com.durukanYukselKaragoz.EasyReminder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;


public class MainActivity extends AppCompatActivity {
    //Variables
    DatabaseHelper dbHelper;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DatabaseHelper(this);
    }

    public void onClick(View view) {
        intent = new Intent(this, addTask.class);
        startActivity(intent);
    }

}
