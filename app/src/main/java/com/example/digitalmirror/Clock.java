package com.example.digitalmirror;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Clock extends AppCompatActivity {

    Button btnSet, btnSetCurrent;
    EditText etTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);

        btnSet = findViewById(R.id.btnSet);
        btnSetCurrent = findViewById(R.id.btnSetCurrent);
        etTime = findViewById(R.id.etTime);

    }
}
