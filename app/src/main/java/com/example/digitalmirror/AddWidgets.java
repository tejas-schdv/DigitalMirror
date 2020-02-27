package com.example.digitalmirror;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class AddWidgets extends AppCompatActivity implements ModuleAdapter.ItemClicked {
    RecyclerView recyclerView;
    RecyclerView.Adapter myAdapter;
    RecyclerView.LayoutManager layoutManager;
    Button btnBack;

    ArrayList<Module> modules;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_widgets);


        recyclerView = findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        modules = new ArrayList<Module>();

        modules.add(new Module("Clock", "clock", "true"));

        myAdapter = new ModuleAdapter(this, modules);

        recyclerView.setAdapter(myAdapter);



    }

    @Override
    public void onItemClicked(int index) {
        String module = modules.get(index).getName();
        String newActivity = "com.example.digitalmirror." + module + ".class";
        Intent intent = null;
        try {
            intent = new Intent(AddWidgets.this, Class.forName(newActivity));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        startActivity(intent);
    }


}
