package com.example.digitalmirror;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class AddWidgets extends AppCompatActivity implements ModuleAdapter.ItemClicked {
    RecyclerView recyclerView;
    RecyclerView.Adapter myAdapter;
    RecyclerView.LayoutManager layoutManager;
    Button btnBack, btnSignOut;

    ArrayList<Module> modules;
    CheckBox checkBoxClock, checkBoxWeather, checkBoxDate, checkBoxCalendar;

    int RC_SIGN_IN = 0;
    SignInButton signInButton;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_widgets);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //google stuff
        signInButton = findViewById(R.id.btnGoogle);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        setGooglePlusButtonText(signInButton, "Sign in with Google");




        //checkBox = findViewById(R.id.checkBox);
        recyclerView = findViewById(R.id.list);
        btnBack = findViewById(R.id.btnBackAddWidgets);
        btnSignOut = findViewById(R.id.btnSignOut);
        btnSignOut.setVisibility(View.GONE);
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

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddWidgets.this, com.example.digitalmirror.MainActivity.class);
                startActivity(intent);
            }
        });

        signInButton.setVisibility(View.GONE);
        btnSignOut.setVisibility(View.GONE);

        if(isSignedIn()) {
            btnSignOut.setVisibility(View.VISIBLE);
        }
        else {
            signInButton.setVisibility(View.VISIBLE);
        }

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btnGoogle:
                        signIn();
                        break;
                }
            }
        });

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
                btnSignOut.setVisibility(View.GONE);
                signInButton.setVisibility(View.VISIBLE);

                Intent intent = new Intent(AddWidgets.this, MainActivity.class);
                startActivity(intent);
            }
        });
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

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        //intent
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
            signInButton.setVisibility(View.GONE);
            btnSignOut.setVisibility(View.VISIBLE);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            final String uid = account.getId();
            //firebaseAuthWithGoogle(account);



            // Signed in successfully, show authenticated UI.
            final GoogleSignInAccount accountG = GoogleSignIn.getLastSignedInAccount(this);



            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
            rootRef.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.hasChild(uid)) {
                        Intent intent = new Intent(AddWidgets.this, MainActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");
                        HashMap<String, Object> user_data = new HashMap<>();
                        user_data.put("email", accountG.getEmail());
                        user_data.put("name", accountG.getDisplayName());

                        HashMap<String, Object> modules = new HashMap<>();


                        HashMap<String, Object> clock = new HashMap<>();
                        clock.put("enabled", "false");
                        modules.put("clock", clock);

                        HashMap<String, Object> date = new HashMap<>();
                        date.put("enabled", "false");
                        modules.put("date", date);

                        HashMap<String, Object> weather = new HashMap<>();
                        weather.put("enabled", "false");
                        weather.put("address", "camarillo,CA,us");
                        modules.put("weather", weather);

                        HashMap<String, Object> calendar = new HashMap<>();
                        calendar.put("enabled", "false");

                        HashMap<String, Object> events = new HashMap<>();
                        HashMap<String, Object> defaultEvent = new HashMap<>();
                        defaultEvent.put("color", "red");
                        defaultEvent.put("description", "default");
                        defaultEvent.put("timestamp", "00000");
                        defaultEvent.put("title", "default");

                        events.put("defaultEvent", defaultEvent);
                        calendar.put("events", events);

                        modules.put("calendar", calendar);

                        user_data.put("modules", modules);
                        usersRef.child(uid).setValue(user_data);

                        Intent intent = new Intent(AddWidgets.this, MainActivity.class);
                        startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Error", "signInResult:failed code=" + e.getStatusCode());
        }


    }

    protected void setGooglePlusButtonText(SignInButton signInButton, String buttonText) {
        // Find the TextView that is inside of the SignInButton and set its text
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText(buttonText);
                return;
            }
        }
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(AddWidgets.this, "Signed Out Successfully", Toast.LENGTH_LONG).show();
                        finish();
                    }
                });


    }

    public boolean isSignedIn() {
        return GoogleSignIn.getLastSignedInAccount(this) != null;
    }


}
