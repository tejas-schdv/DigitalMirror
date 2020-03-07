package com.example.digitalmirror;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androdocs.httprequest.HttpRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Weather extends AppCompatActivity {

    private String ADDRESS;
    EditText etCity, etState;
    Button btnSetAddress;
    DatabaseReference database;
    String API = "ebe86c447a46a73e12d63b0def7ba170";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        etCity = findViewById(R.id.etCity);
        etState = findViewById(R.id.etState);

        btnSetAddress = findViewById(R.id.btnSetAddress);

        btnSetAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database = FirebaseDatabase.getInstance().getReference().child("modules").child("weather").child("address");

                String city = etCity.getText().toString();
                String state = etState.getText().toString();
                ADDRESS = city + "," + state + ",us";

                if(!city.equals("") && !state.equals("")) {
                    new weatherTask().execute();
                }
                else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Enter a value into both fields", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

    }

    class weatherTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... args) {
            String response = HttpRequest.excuteGet("https://api.openweathermap.org/data/2.5/weather?q=" + ADDRESS + "&units=metric&appid=" + API);
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jsonObj = new JSONObject(result);

                String cod = jsonObj.getString("cod");
                if(cod.equals("404")){
                    Toast toast=Toast.makeText(getApplicationContext(),"ERROR: Location not found",Toast.LENGTH_SHORT);
                    toast.show();
                }
                else{
                    database.setValue(ADDRESS);
                    Toast toast = Toast.makeText(getApplicationContext(), "Address set to " + etCity.getText().toString() + ", " + etState.getText().toString(), Toast.LENGTH_SHORT);
                    toast.show();
                }


            } catch (JSONException e) {
                Toast toast=Toast.makeText(getApplicationContext(),"ERROR",Toast.LENGTH_SHORT);
                toast.show();
            }

        }
    }
}
