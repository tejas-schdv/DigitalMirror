package com.example.digitalmirror;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class AddCalendarEvent extends AppCompatActivity {

    Button btnExit, btnSave;
    TextView tvNewEvent, tvEventDate, tvEventDateSet;
    EditText etEventTitle;

    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_calendar_event);

        btnExit = findViewById(R.id.btnExit);
        btnSave = findViewById(R.id.btnSave);
        tvNewEvent = findViewById(R.id.tvNewEvent);
        tvEventDate = findViewById(R.id.tvEventDate);
        tvEventDateSet = findViewById(R.id.tvEventDateSet);
        etEventTitle = findViewById(R.id.etEventTitle);

        database = FirebaseDatabase.getInstance().getReference().child("modules").child("calendar");

        Bundle bundle = getIntent().getExtras();
        String stringDate = bundle.getString("bundle");

        tvEventDateSet.setText(stringDate);


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eventTitle = etEventTitle.getText().toString();


                Intent intent = new Intent(AddCalendarEvent.this, com.example.digitalmirror.Calendar.class);
                startActivity(intent);
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddCalendarEvent.this, com.example.digitalmirror.Calendar.class);
                startActivity(intent);
            }
        });
    }
}
