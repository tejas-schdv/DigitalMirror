package com.example.digitalmirror;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.androdocs.httprequest.HttpRequest;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    //weather variables
    DatabaseReference database, databaseClock, databaseWeather, databaseDate, databaseCalendar;
    String CITY, STATE, ADDRESS; //= "santa paula,ca,us";
    String API = "ebe86c447a46a73e12d63b0def7ba170";
    ImageView ivWeather, ivWind;
    TextView tvStatus, tvTemp, tvTempMin, tvTempMax, tvWind, clock, tvDate, tvEventsToday, tvEventsTodayDivider, tvEventsTodayList;

    Button btnSettings, btnSetAddress;
    String uid = "default";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //START GOOGLE STUFF
        boolean check = false;


        if(isSignedIn()) {
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
            uid = account.getId();

        }
        //END GOOGLE STUFF

        //START WEATHER MODULE
        databaseWeather = FirebaseDatabase.getInstance().getReference().child("users").child("current").child("modules").child("weather");
        ivWeather = findViewById(R.id.ivWeather);
        ivWind = findViewById(R.id.ivWind);

        tvStatus = findViewById(R.id.tvStatus);
        tvTemp = findViewById(R.id.tvTemp);
        tvTempMax = findViewById(R.id.tvTempMax);
        tvTempMin = findViewById(R.id.tvTempMin);
        tvWind = findViewById(R.id.tvWind);

        btnSettings = findViewById(R.id.btnSettings);

        DatabaseReference dbWeatherAddress = databaseWeather.child("address");
        dbWeatherAddress.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ADDRESS = dataSnapshot.getValue().toString();
                new weatherTask().execute();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference dbWeatherEnabled = databaseWeather.child("enabled");
        dbWeatherEnabled.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue().toString().equals("false"))
                {
                    tvStatus.setVisibility(View.GONE);
                    tvTemp.setVisibility(View.GONE);
                    tvTempMax.setVisibility(View.GONE);
                    tvTempMin.setVisibility(View.GONE);
                    tvWind.setVisibility(View.GONE);
                    ivWind.setVisibility(View.GONE);
                    ivWeather.setVisibility(View.GONE);
                }
                else{
                    tvStatus.setVisibility(View.VISIBLE);
                    tvTemp.setVisibility(View.VISIBLE);
                    tvTempMax.setVisibility(View.VISIBLE);
                    tvTempMin.setVisibility(View.VISIBLE);
                    tvWind.setVisibility(View.VISIBLE);
                    ivWind.setVisibility(View.VISIBLE);
                    ivWeather.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //END WEATHER MODULE
        

        //START CLOCK MODULE
        final String currentTime = java.text.DateFormat.getTimeInstance().format(new Date());
        clock = findViewById(R.id.tvClock);
        clock.setText(currentTime);

        databaseClock = FirebaseDatabase.getInstance().getReference().child("users").child("current").child("modules").child("clock").child("enabled");
        databaseClock.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue().toString().equals("false")) {
                    clock.setVisibility(View.GONE);
                }
                else{
                    clock.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //might delete this vvvv
        /*database = FirebaseDatabase.getInstance().getReference().child("modules").child("clock").child("time");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!currentTime.equals(dataSnapshot.getValue().toString()))
                {
                    database.setValue(currentTime);
                    //clock.setText(currentTime);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
        //END CLOCK MODULE

        //START DATE MODULE
        tvDate = findViewById(R.id.tvDate);
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        String dayOfWeek = "null";

        switch (day) {
            case Calendar.SUNDAY:
                dayOfWeek = "Sunday";
                break;
            case Calendar.MONDAY:
                dayOfWeek = "Monday";
                break;
            case Calendar.TUESDAY:
                dayOfWeek = "Tuesday";
                break;
            case Calendar.WEDNESDAY:
                dayOfWeek = "Wednesday";
                break;
            case Calendar.THURSDAY:
                dayOfWeek = "Thursday";
                break;
            case Calendar.FRIDAY:
                dayOfWeek = "Friday";
                break;
            case Calendar.SATURDAY:
                dayOfWeek = "Saturday";
                break;
        }

        SimpleDateFormat monthFormatter = new SimpleDateFormat("MM");
        Date dateMonth = new Date();
        String monthNumber = monthFormatter.format(dateMonth);
        String month;
        switch (monthNumber) {
            case "01":
                month = "January";
                break;
            case "02":
                month = "February";
                break;
            case "03":
                month = "March";
                break;
            case "04":
                month = "April";
                break;
            case "05":
                month = "May";
                break;
            case "06":
                month = "June";
                break;
            case "07":
                month = "July";
                break;
            case "08":
                month = "August";
                break;
            case "09":
                month = "September";
                break;
            case "10":
                month = "October";
                break;
            case "11":
                month = "November";
                break;
            case "12":
                month = "December";
                break;
            default:
                month = "ERROR";
                break;
        }

        SimpleDateFormat dayFormatter = new SimpleDateFormat("dd");
        Date dateDay = new Date();
        String dayNumber = dayFormatter.format(dateDay);

        SimpleDateFormat yearFormatter = new SimpleDateFormat("yyyy");
        Date dateYear = new Date();
        String yearNumber = yearFormatter.format(dateYear);

        String dateForm = dayOfWeek + ", " + month + " " + dayNumber + ", " + yearNumber;
        tvDate.setText(dateForm);

        databaseDate = FirebaseDatabase.getInstance().getReference().child("users").child("current").child("modules").child("date");
        DatabaseReference dbDateEnabled = databaseDate.child("enabled");
        dbDateEnabled.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue().toString().equals("false"))
                {
                    tvDate.setVisibility(View.GONE);
                }
                else{
                    tvDate.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //END DATE MODULE

        //START CALENDAR MODULE
        databaseCalendar = FirebaseDatabase.getInstance().getReference().child("users").child("current").child("modules").child("calendar");
        tvEventsToday = findViewById(R.id.tvEventsToday);
        tvEventsTodayDivider = findViewById(R.id.tvEventsTodayDivider);
        tvEventsTodayList = findViewById(R.id.tvEventsTodayList);
        Date todaysDate = new Date();
        final ArrayList<String> todaysEvents = new ArrayList<>();

        DatabaseReference databaseCalendarEvents = FirebaseDatabase.getInstance().getReference().child("users").child("current").child("modules").child("calendar").child("events");

        String month2 = new SimpleDateFormat("MM").format(todaysDate);
        int intMonth = Integer.parseInt(month2);
        String year2 = new SimpleDateFormat("yyyy").format(todaysDate);
        int intYear = Integer.parseInt(year2);
        String day2 = new SimpleDateFormat("dd").format(todaysDate);
        int intDay = Integer.parseInt(day2);

        java.util.Calendar calendar2 = java.util.Calendar.getInstance();


        calendar2.set(intYear, intMonth-1, intDay-1);
        int numOfDaysInMonth = calendar2.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);
        Date firstDay = calendar2.getTime();
        calendar2.add(java.util.Calendar.DAY_OF_MONTH, numOfDaysInMonth-(numOfDaysInMonth-2));
        Date lastDay = calendar2.getTime();

        long millisFirstDay = firstDay.getTime();
        long millisLastDay = lastDay.getTime();

        String stringMillisFirstDay = Long.toString(millisFirstDay);
        String stringMillisLastDay = Long.toString(millisLastDay);





        //querey for given date
        Query query = databaseCalendarEvents.orderByChild("timestamp").startAt(stringMillisFirstDay).endAt(stringMillisLastDay);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "event" node with all children within date range
                    for (DataSnapshot event : dataSnapshot.getChildren()) {
                        // do something with the individual "events"
                        String eventTitle = event.child("title").getValue().toString();
                        String eventDescription = event.child("description").getValue().toString();
                        String eventTotal = eventTitle + ": " + eventDescription;
                        todaysEvents.add(eventTotal);
                    }
                }

                String events = "";
                if(!todaysEvents.isEmpty()){
                    for(int i = 0; i < todaysEvents.size(); i++){
                        events = events + (todaysEvents.get(i) + "\n\n");
                    }
                    tvEventsTodayList.setText(events);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        DatabaseReference dbCalendarEnabled = databaseCalendar.child("enabled");
        dbCalendarEnabled.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue().toString().equals("false"))
                {
                    tvEventsToday.setVisibility(View.GONE);
                    tvEventsTodayDivider.setVisibility(View.GONE);
                    tvEventsTodayList.setVisibility(View.GONE);
                }
                else{
                    tvEventsToday.setVisibility(View.VISIBLE);
                    tvEventsTodayDivider.setVisibility(View.VISIBLE);
                    tvEventsTodayList.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //END CALENDAR MODULE

        //START TWITTER MODULE (Hashtags)
        //END TWITTER MODULE



        //START EMAIL MODULE
        //END EMAIL MODULE


        //The above done by tuesday

        //START NEWS MODULE
        //END NEWS MODULE

        //START NOTIFICATION MODULE
        //END NOTIFICATION MODULE

        //SETTING BUTTON
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, com.example.digitalmirror.AddWidgets.class);
                startActivity(intent);
            }
        });
    }

    class weatherTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            /* Showing the ProgressBar, Making the main design GONE */
                /*findViewById(R.id.loader).setVisibility(View.VISIBLE);
                findViewById(R.id.mainContainer).setVisibility(View.GONE);
                findViewById(R.id.errorText).setVisibility(View.GONE);*/
        }

        protected String doInBackground(String... args) {
            String response = HttpRequest.excuteGet("https://api.openweathermap.org/data/2.5/weather?q=" + ADDRESS + "&units=metric&appid=" + API);
            return response;
        }

        @Override
        protected void onPostExecute(String result) {


            try {
                JSONObject jsonObj = new JSONObject(result);
                JSONObject main = jsonObj.getJSONObject("main");
                JSONObject sys = jsonObj.getJSONObject("sys");
                JSONObject wind = jsonObj.getJSONObject("wind");
                JSONObject weather = jsonObj.getJSONArray("weather").getJSONObject(0);

                Long updatedAt = jsonObj.getLong("dt");
                String updatedAtText = "Updated at: " + new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(new Date(updatedAt * 1000));
                String temp = main.getString("temp") + "°C";
                String tempMin = "Hi: " + main.getString("temp_min") + "°C";
                String tempMax = "Lo: " + main.getString("temp_max") + "°C";
                String pressure = main.getString("pressure");
                String humidity = main.getString("humidity");

                Long sunrise = sys.getLong("sunrise");
                Long sunset = sys.getLong("sunset");
                String windSpeed = wind.getString("speed") + " MPH";
                String weatherDescription = weather.getString("description");

                String address = jsonObj.getString("name") + ", " + sys.getString("country");


                /* Populating extracted data into our views */
                tvStatus.setText(weatherDescription.toUpperCase());
                tvTemp.setText(temp);
                tvTempMin.setText(tempMin);
                tvTempMax.setText(tempMax);
                tvWind.setText(windSpeed);
                //addressTxt.setText(address);
                //updated_atTxt.setText(updatedAtText);
                //sunriseTxt.setText(new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sunrise * 1000)));
                //sunsetTxt.setText(new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sunset * 1000)));
                //pressureTxt.setText(pressure);
                //humidityTxt.setText(humidity);

                /* Views populated, Hiding the loader, Showing the main design */
                //findViewById(R.id.loader).setVisibility(View.GONE);
                //findViewById(R.id.mainContainer).setVisibility(View.VISIBLE);


            } catch (JSONException e) {
                Toast toast=Toast.makeText(getApplicationContext(),"ERROR: Unable to retrieve weather info",Toast.LENGTH_SHORT);
                toast.show();
                //findViewById(R.id.loader).setVisibility(View.GONE);
                //findViewById(R.id.errorText).setVisibility(View.VISIBLE);
            }

        }
    }

    public ArrayList<String> getTodaysEvents(java.util.Date dateGiven){
        final ArrayList<String> todaysEvents = new ArrayList<>();

        database = FirebaseDatabase.getInstance().getReference().child("users").child("current").child("modules").child("calendar").child("events");

        String month = new SimpleDateFormat("MM").format(dateGiven);
        int intMonth = Integer.parseInt(month);
        String year = new SimpleDateFormat("yyyy").format(dateGiven);
        int intYear = Integer.parseInt(year);
        String day = new SimpleDateFormat("dd").format(dateGiven);
        int intDay = Integer.parseInt(day);

        java.util.Calendar calendar = java.util.Calendar.getInstance();


        calendar.set(intYear, intMonth-1, intDay);
        Date firstDay = calendar.getTime();

        long millisFirstDay = firstDay.getTime();

        String stringMillisFirstDay = Long.toString(millisFirstDay);





        //querey for goven date
        Query query = database.orderByChild("timestamp").startAt(stringMillisFirstDay).endAt(stringMillisFirstDay);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "event" node with all children within date range
                    for (DataSnapshot event : dataSnapshot.getChildren()) {
                        // do something with the individual "events"
                        String eventTitle = event.child("title").getValue().toString();
                        todaysEvents.add(eventTitle);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return todaysEvents;
    }

    public boolean isSignedIn() {
        return GoogleSignIn.getLastSignedInAccount(this) != null;
    }
}
