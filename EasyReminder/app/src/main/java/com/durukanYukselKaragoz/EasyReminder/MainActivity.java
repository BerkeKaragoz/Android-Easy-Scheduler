package com.durukanYukselKaragoz.EasyReminder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
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
