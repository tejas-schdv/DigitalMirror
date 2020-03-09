package com.example.digitalmirror;

import android.widget.Button;
import android.widget.CheckBox;

public class CalendarEvent {
    private String eventName;
    private String dotColor;
    private String eventDate;
    private Button btnDeleteEvent;
    private String id;

    public CalendarEvent(String id, String eventName, String dotColor, String eventDate, Button btnDeleteEvent) {
        this.id = id;
        this.eventName = eventName;
        this.dotColor = dotColor;
        this.eventDate = eventDate;
        this.btnDeleteEvent = btnDeleteEvent;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getDotColor() {
        return dotColor;
    }

    public void setDotColor(String dotColor) {
        this.dotColor = dotColor;
    }

    public void setEventDate(String eventDate){
        this.eventDate = eventDate;
    }

    public String getEventDate(){
        return this.eventDate;
    }

    public void setID(String id){
        this.id = id;
    }

    public String getID(){
        return this.id;
    }

}
