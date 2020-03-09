package com.example.digitalmirror;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddWidgets extends AppCompatActivity implements ModuleAdapter.ItemClicked {
    RecyclerView recyclerView;
    RecyclerView.Adapter myAdapter;
    RecyclerView.LayoutManager layoutManager;
    Button btnBack;

    ArrayList<Module> modules;
    CheckBox checkBoxClock, checkBoxWeather, checkBoxDate, checkBoxCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_widgets);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //checkBox = findViewById(R.id.checkBox);
        recyclerView = findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        modules = new ArrayList<Module>();

        modules.add(new Module("Date", "date", checkBoxDate));
        modules.add(new Module("Clock", "clock", checkBoxClock));
        modules.add(new Module("Weather", "weather", checkBoxWeather));
        modules.add(new Module("Calendar", "calendar", checkBoxCalendar));

        myAdapter = new ModuleAdapter(this, modules);

        recyclerView.setAdapter(myAdapter);
    }

    @Override
    public void onItemClicked(int index) {
        Class newclass = null;
        String module = modules.get(index).getName();
        String newActivity = "com.example.digitalmirror." + module;
        Intent intent = null;
        try {
            newclass = Class.forName(newActivity);
        } catch (ClassNotFoundException c) {
            c.printStackTrace();
        }

        if(newclass != null) {
            intent = new Intent(AddWidgets.this, newclass);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(),"ERROR: Class not found.",Toast.LENGTH_SHORT).show();
        }
    }


}
