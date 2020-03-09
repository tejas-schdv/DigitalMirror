package com.example.digitalmirror;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddCalendarEvent extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Button btnExit, btnSave;
    TextView tvNewEvent, tvEventDate, tvEventDateSet;
    EditText etEventTitle;
    Spinner spinner;
    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_calendar_event);



        btnExit = findViewById(R.id.btnExit);
        btnSave = findViewById(R.id.btnSave);
        tvNewEvent = findViewById(R.id.tvNewEvent);
        tvEventDate = findViewById(R.id.tvEventDate);
        tvEventDateSet = findViewById(R.id.tvEventDateSet);
        etEventTitle = findViewById(R.id.etEventTitle);

        database = FirebaseDatabase.getInstance().getReference().child("modules").child("calendar").child("events");

        Bundle bundle = getIntent().getExtras();
        final String stringDate = bundle.getString("bundle");

        tvEventDateSet.setText(stringDate);

        spinner = findViewById(R.id.spinnerColor);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.colors_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);



        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eventTitle = etEventTitle.getText().toString();

                String color = String.valueOf(spinner.getSelectedItem());
                String colorLower = color.toLowerCase();

                DateFormat dateFormat = new SimpleDateFormat("E, MMM dd, yyyy");
                Date date = null;
                try {
                    date = dateFormat.parse(stringDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long time = date.getTime();

                //write event to database
                Map<String, String> eventData = new HashMap<String, String>();
                eventData.put("timestamp", Long.toString(time));
                eventData.put("title", eventTitle);
                eventData.put("color", colorLower);
                database.push().setValue(eventData);


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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }
}
