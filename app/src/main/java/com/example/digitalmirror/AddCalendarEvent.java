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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
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
    EditText etEventTitle, etEventDescription;
    Spinner spinner;
    DatabaseReference database, databaseEdit;

    String uid = "default";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_calendar_event);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        btnExit = findViewById(R.id.btnExit);
        btnSave = findViewById(R.id.btnSave);
        tvNewEvent = findViewById(R.id.tvNewEvent);
        tvEventDate = findViewById(R.id.tvEventDate);
        tvEventDateSet = findViewById(R.id.tvEventDateSet);
        etEventTitle = findViewById(R.id.etEventTitle);
        etEventDescription = findViewById(R.id.etEventDescription);

        if(isSignedIn()) {
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
            uid = account.getId();

        }

        spinner = findViewById(R.id.spinnerColor);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.colors_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        database = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("modules").child("calendar").child("events");

        //editing existing event
        Bundle bundle = getIntent().getExtras();
        final String isEdit = bundle.getString("edit");
        final String stringID = bundle.getString("eventID");
        if(isEdit != null) {
            final String stringDate = bundle.getString("eventDate");
            final String stringTitle = bundle.getString("eventTitle");
            final String stringColor = bundle.getString("eventColor");
            final String stringDescription = bundle.getString("eventDescription");

            tvEventDateSet.setText(stringDate);
            etEventTitle.setText(stringTitle);
            etEventDescription.setText(stringDescription);
            spinner.setPrompt(stringColor);
        }

        //adding new event
        Bundle bundleNew = getIntent().getExtras();
        final String isNew = bundleNew.getString("new");
        if(isNew != null) {
            final String strDate = bundleNew.getString("bundle");
            tvEventDateSet.setText(strDate);
        }


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEdit != null){
                    String editEventTitle = etEventTitle.getText().toString();
                    String editEventDescription = etEventDescription.getText().toString();

                    String editID = stringID;
                    databaseEdit = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("modules").child("calendar").child("events").child(editID);

                    String editColor = String.valueOf(spinner.getSelectedItem());
                    String editColorLower = editColor.toLowerCase();

                    DateFormat editDateFormat = new SimpleDateFormat("E, MMM dd, yyyy");
                    Date editDate = null;
                    try {
                        editDate = editDateFormat.parse(tvEventDateSet.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    long editTime = editDate.getTime();

                    //write event to database
                    databaseEdit.child("timestamp").setValue(Long.toString(editTime));
                    databaseEdit.child("title").setValue(editEventTitle);
                    databaseEdit.child("color").setValue(editColorLower);
                    databaseEdit.child("description").setValue(editEventDescription);
                }
                else{
                    String eventTitle = etEventTitle.getText().toString();
                    String eventDescription = etEventDescription.getText().toString();
                    //if(eventDescription.equals(""))
                    //eventDescription = " ";

                    String color = String.valueOf(spinner.getSelectedItem());
                    String colorLower = color.toLowerCase();

                    DateFormat dateFormat = new SimpleDateFormat("E, MMM dd, yyyy");
                    Date date = null;
                    try {
                        date = dateFormat.parse(tvEventDateSet.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    long time = date.getTime();

                    //write event to database
                    Map<String, String> eventData = new HashMap<String, String>();
                    eventData.put("timestamp", Long.toString(time));
                    eventData.put("title", eventTitle);
                    eventData.put("color", colorLower);
                    eventData.put("description", eventDescription);
                    database.push().setValue(eventData);
                }

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

    public boolean isSignedIn() {
        return GoogleSignIn.getLastSignedInAccount(this) != null;
    }
}
