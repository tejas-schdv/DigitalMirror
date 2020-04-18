package com.example.digitalmirror;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

public class Calendar extends AppCompatActivity implements CalendarEventAdapter.ItemClicked{

    private static final String TAG = "TAG";
    CompactCalendarView compactCalendar;
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM - yyyy", Locale.getDefault());
    //private SimpleDateFormat dateFormatMonth2 = new SimpleDateFormat("MMMM", Locale.getDefault());
    Button btnAddEvent;
    String date;
    TextView tvHeader;

    DatabaseReference database;

    RecyclerView recyclerView;
    RecyclerView.Adapter myAdapter;
    RecyclerView.LayoutManager layoutManager;
    Button btnBack, btnDeleteEvent;

    ArrayList<CalendarEvent> calendarEvents;

    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        context = this;

        final ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle(null);

        tvHeader = findViewById(R.id.tvHeader);
        btnAddEvent = findViewById(R.id.btnAddEvent);
        btnBack = findViewById(R.id.btnBackCalendar);
        compactCalendar = findViewById(R.id.compactcalendar_view);
        compactCalendar.setUseThreeLetterAbbreviation(true);
        compactCalendar.setFirstDayOfWeek(java.util.Calendar.SUNDAY);
        database = FirebaseDatabase.getInstance().getReference().child("modules").child("calendar").child("events");



        String initialMonth = dateFormatMonth.format(compactCalendar.getFirstDayOfCurrentMonth());
        tvHeader.setText(initialMonth);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                calendarEvents.clear();
                compactCalendar.removeAllEvents();
                getMonthEvents(compactCalendar.getFirstDayOfCurrentMonth());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //api examples
        /*//Set an event for Teachers' Professional Day 2016 which is 21st of October
        Event ev1 = new Event(Color.RED, 1583628161000L, "Teachers' Professional Day");
        compactCalendar.addEvent(ev1);

        // Added event 2 GMT: Sun, 07 Jun 2015 19:10:51 GMT
        Event ev2 = new Event(Color.GREEN, 1583628161000L);
        compactCalendar.addEvent(ev2);

        // Query for events on Sun, 07 Jun 2015 GMT.
        // Time is not relevant when querying for events, since events are returned by day.
        // So you can pass in any arbitary DateTime and you will receive all events for that day.
        List<Event> events = compactCalendar.getEvents(1583628161000L); // can also take a Date object*/


        //adding events to bottom of calendar
        recyclerView = findViewById(R.id.listEvents);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        calendarEvents = new ArrayList<CalendarEvent>();


        // define a listener to receive callbacks when certain events happen.
        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(java.util.Date dateClicked) {
                List<Event> events = compactCalendar.getEvents(dateClicked);
                Log.d(TAG, "Day was clicked: " + dateClicked + " with events " + events);

                date = new SimpleDateFormat("E, MMM dd, yyyy").format(dateClicked);
            }

            @Override
            public void onMonthScroll(java.util.Date firstDayOfNewMonth) {
                String month = dateFormatMonth.format(firstDayOfNewMonth);
                tvHeader.setText(month);

                //create new method and call it to return list of events
                calendarEvents.clear();
                compactCalendar.removeAllEvents();
                getMonthEvents(firstDayOfNewMonth);
            }
        });

        btnAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Calendar.this, com.example.digitalmirror.AddCalendarEvent.class);
                Bundle bundleNew = new Bundle();
                bundleNew.putString("bundle", date);
                bundleNew.putString("new", "new");
                intent.putExtras(bundleNew);
                startActivity(intent);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Calendar.this, com.example.digitalmirror.AddWidgets.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onItemClicked(int index) {
        Intent intent = new Intent(Calendar.this, com.example.digitalmirror.AddCalendarEvent.class);
        Bundle bundle = new Bundle();
        //pass date of event
        String eventDate = calendarEvents.get(index).getEventDate();
        String eventTitle = calendarEvents.get(index).getEventName();
        String eventColor = calendarEvents.get(index).getDotColor();
        String eventDescription = calendarEvents.get(index).getEventDescription();
        String id = calendarEvents.get(index).getID();

        bundle.putString("edit", "edit");
        bundle.putString("eventDate", eventDate);
        bundle.putString("eventTitle", eventTitle);
        bundle.putString("eventColor", eventColor);
        bundle.putString("eventDescription", eventDescription);
        bundle.putString("eventID", id);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public ArrayList<CalendarEvent> getMonthEvents(java.util.Date dateGiven){
        /*String sDate4 = "Wed, May 6, 2020";
        Date date4 = null;
        SimpleDateFormat formatter4=new SimpleDateFormat("E, MMM dd, yyyy");
        try {
            date4=formatter4.parse(sDate4);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dateGiven = date4;*/

        ArrayList<CalendarEvent> monthEvents = new ArrayList<>();

        database = FirebaseDatabase.getInstance().getReference().child("modules").child("calendar").child("events");

        String month = new SimpleDateFormat("MM").format(dateGiven);
        int intMonth = Integer.parseInt(month);
        String year = new SimpleDateFormat("yyyy").format(dateGiven);
        int intYear = Integer.parseInt(year);

        java.util.Calendar calendar = java.util.Calendar.getInstance();

        int dateDay = 1;
        calendar.set(intYear, intMonth-1, dateDay);
        int numOfDaysInMonth = calendar.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);
        Date firstDay = calendar.getTime();
        calendar.add(java.util.Calendar.DAY_OF_MONTH, numOfDaysInMonth-1);
        Date lastDay = calendar.getTime();

        long millisFirstDay = firstDay.getTime();
        long millisLastDay = lastDay.getTime();

        String stringMillisFirstDay = Long.toString(millisFirstDay);
        String stringMillisLastDay = Long.toString(millisLastDay);





        //String currentMonth = tvHeader.getText().toString();
        Query query = database.orderByChild("timestamp").startAt(stringMillisFirstDay).endAt(stringMillisLastDay);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "event" node with all children within date range
                    for (DataSnapshot event : dataSnapshot.getChildren()) {
                        // do something with the individual "events"
                        String eventTitle = event.child("title").getValue().toString();
                        String eventTimestamp = event.child("timestamp").getValue().toString();
                        String eventColor = event.child("color").getValue().toString();
                        String eventDescription = event.child("description").getValue().toString();

                        long l = Long.parseLong(eventTimestamp);
                        String date1 = new java.text.SimpleDateFormat("E, MMM dd, yyyy").format(new java.util.Date(l));

                        //add to claendar event
                        String id = event.getKey();
                        calendarEvents.add(new CalendarEvent(id, eventTitle, eventColor, date1, eventDescription, btnDeleteEvent));

                        //add to calendarview
                        int color = getColorfromString(eventColor);
                        Event eve = new Event(color, Long.parseLong(eventTimestamp), eventTitle);
                        compactCalendar.addEvent(eve);
                    }
                    myAdapter = new CalendarEventAdapter(context, calendarEvents);
                    recyclerView.setAdapter(myAdapter);
                }
                else{
                    calendarEvents.clear();
                    myAdapter = new CalendarEventAdapter(context, calendarEvents);
                    recyclerView.setAdapter(myAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return monthEvents;
    }

    public int getColorfromString(String color){
        int returnColor;

        switch (color){
            case "red":
                returnColor = Color.RED;
                break;
            case "green":
                returnColor = Color.GREEN;
                break;
            case "blue":
                returnColor = Color.BLUE;
                break;
            case "purple":
                returnColor = Color.rgb(176, 66, 245);
                break;
            case "yellow":
                returnColor = Color.YELLOW;
                break;
            case "orange":
                returnColor = Color.rgb(245, 144, 66);
                break;
            default:
                returnColor = Color.GRAY;
                break;
        }
        return returnColor;
    }

    public ArrayList<String> getTodaysEvents(){
        ArrayList<String> todaysEvents = new ArrayList<>();

        for(int i = 0; i < calendarEvents.size(); i++){
            todaysEvents.add(calendarEvents.get(i).getEventName());
        }

        return todaysEvents;
    }
}
