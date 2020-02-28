package com.example.digitalmirror;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class Clock extends AppCompatActivity {

    Button btnSet, btnSetCurrent;
    private Calendar calendar;
    TimePicker timePicker1;
    private String format = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);

        btnSet = findViewById(R.id.btnSet);
        btnSetCurrent = findViewById(R.id.btnSetCurrent);

        timePicker1 = (TimePicker) findViewById(R.id.timePicker1);



        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                Intent intent = new Intent(Clock.this,MainActivity.class);

                TextClock clock;
                int hour = timePicker1.getCurrentHour();
                int min = timePicker1.getCurrentMinute();
                if (hour == 0) {
                    hour += 12;
                    format = "AM";
                } else if (hour == 12) {
                    format = "PM";
                } else if (hour > 12) {
                    hour -= 12;
                    format = "PM";
                } else {
                    format = "AM";
                }

                clock = findViewById(R.id.tvClock);
                clock.setText(new StringBuilder().append(hour).append(" : ").append(min)
                        .append(" ").append(format));
            }



        });

    }


}
