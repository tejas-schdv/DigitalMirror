package com.example.digitalmirror;

import android.widget.Button;
import android.widget.CheckBox;

public class CalendarEvent {
    private String eventName;
    private String dotColor;
    private String eventDate;
    private String eventDescription;
    private Button btnDeleteEvent;
    private String id;

    public CalendarEvent(String id, String eventName, String dotColor, String eventDate, String eventDescription, Button btnDeleteEvent) {
        this.id = id;
        this.eventName = eventName;
        this.dotColor = dotColor;
        this.eventDate = eventDate;
        this.eventDescription = eventDescription;
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

    public void setEventDescription(String eventDescription){
        this.eventDescription = eventDescription;
    }

    public String getEventDescription(){
        return this.eventDescription;
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
