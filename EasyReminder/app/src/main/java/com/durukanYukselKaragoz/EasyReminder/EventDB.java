package com.durukanYukselKaragoz.EasyReminder;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

public class EventDB {

    public static final String TABLE_NAME ="events";
    public static final String FIELD_ID = "_id";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_DETAIL = "detail";
    public static final String FIELD_TYPE = "type";
    public static final String FIELD_YEAR = "year";
    public static final String FIELD_MONTH = "month";
    public static final String FIELD_DAY = "day";
    public static final String FIELD_HOUR = "hour";
    public static final String FIELD_MINUTE = "minute";

    public static final String CREATE_TABLE_SQL = "CREATE TABLE "+TABLE_NAME+" ("+FIELD_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+FIELD_NAME+" text, "+FIELD_DETAIL+" text, "+FIELD_TYPE+ " text, " +FIELD_YEAR+" number, " + FIELD_MONTH + " number,"
            + FIELD_DAY +" number, " + FIELD_HOUR + " number, " + FIELD_MINUTE + " number);";
    public static final String DROP_TABLE_SQL = "DROP TABLE if exists "+TABLE_NAME;


    public static List<Event> getAllEvent(DatabaseHelper db){

        Cursor cursor = db.getAllRecords(TABLE_NAME, null);
        //Cursor cursor  db.getAllRecordsMethod2("SELECT * FROM "+TABLE_NAME, null)
        List<Event> data=new ArrayList<>();
        Event event = null;
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String detail = cursor.getString(2);
            String type = cursor.getString(3);
            int year = cursor.getInt(4);
            int month = cursor.getInt(5);
            int day = cursor.getInt(6);
            int hour = cursor.getInt(7);
            int minute = cursor.getInt(8);

            event = new Event(id, name, detail, type, year, month, day, hour, minute);
            data.add(event);
        }
        return data;
    }
    public static List<Event> findEvent(DatabaseHelper db, String key){

        String where = FIELD_NAME+" like '%"+key+"%'";
        Cursor cursor = db.getAllRecords(TABLE_NAME, null);
        //Cursor cursor  db.getAllRecordsMethod2("SELECT * FROM "+TABLE_NAME, null)
        List<Event> data=new ArrayList<>();
        Event event = null;
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String detail = cursor.getString(2);
            String type = cursor.getString(3);
            int year = cursor.getInt(4);
            int month = cursor.getInt(5);
            int day = cursor.getInt(6);
            int hour = cursor.getInt(7);
            int minute = cursor.getInt(8);

            event = new Event(id, name, detail, type, year, month, day, hour, minute);
            data.add(event);
        }
        return data;
    }
    public static long insertEvent(DatabaseHelper db, String name, String detail, String type, int year, int month, int day, int hour, int minute){
        ContentValues contentValues = new ContentValues( );
        contentValues.put(FIELD_NAME, name);
        contentValues.put(FIELD_DETAIL, detail);
        contentValues.put(FIELD_TYPE, type);
        contentValues.put(FIELD_YEAR, year);
        contentValues.put(FIELD_MONTH, month);
        contentValues.put(FIELD_DAY, day);
        contentValues.put(FIELD_HOUR, hour);
        contentValues.put(FIELD_MINUTE, minute);

        long res = db.insert(TABLE_NAME,contentValues);
        return res;
    }
    public static boolean updateEvent(DatabaseHelper db, int id, String name, String detail, String type, int year, int month, int day, int hour, int minute){
        ContentValues contentValues = new ContentValues( );
        contentValues.put(FIELD_ID, id);
        contentValues.put(FIELD_NAME, name);
        contentValues.put(FIELD_DETAIL, detail);
        contentValues.put(FIELD_TYPE, type);
        contentValues.put(FIELD_YEAR, year);
        contentValues.put(FIELD_MONTH, month);
        contentValues.put(FIELD_DAY, day);
        contentValues.put(FIELD_HOUR, hour);
        contentValues.put(FIELD_MINUTE, minute);
        String where = FIELD_ID +" = "+id;

        boolean res = db.update(TABLE_NAME,contentValues,where);

        return res;
    }
    public static boolean deleteEvent(DatabaseHelper db, int id){
        String where = FIELD_ID +" = "+ id;
        boolean res = db.delete(TABLE_NAME,where);
        return res;
    }
}

