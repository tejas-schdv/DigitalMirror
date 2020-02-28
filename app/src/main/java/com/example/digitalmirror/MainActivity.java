package com.example.digitalmirror;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextClock;
import android.widget.TextView;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String currentTime = java.text.DateFormat.getTimeInstance().format(new Date());
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        Button btnSettings;


        btnSettings = findViewById(R.id.btnSettings);

        //if() //if clock checkbox is checked
        TextClock clock;
        clock = findViewById(R.id.tvClock);
        clock.setText(currentTime);



        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, com.example.digitalmirror.AddWidgets.class);
                startActivity(intent);
            }
        });
    }
}
