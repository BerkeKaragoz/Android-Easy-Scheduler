package com.durukanYukselKaragoz.EasyReminder;

public class Event {
    private int id;
    private String eventName, eventDetail, eventType;
    private int year, month, day, hour, minute;

    public Event(int id, String eventName, String eventDetail, String eventType, int year, int month, int day, int hour, int minute) {
        this.id = id;
        this.eventName = eventName;
        this.eventDetail = eventDetail;
        this.eventType = eventType;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDetail() {
        return eventDetail;
    }

    public void setEventDetail(String eventDetail) {
        this.eventDetail = eventDetail;
    }

    public String getEventType() { return eventType; }

    public void setEventType(String eventType) { this.eventType = eventType; }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }
}
