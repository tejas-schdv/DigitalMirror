package com.example.digitalmirror;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ModuleAdapter extends RecyclerView.Adapter<ModuleAdapter.ViewHolder> {
    DatabaseReference database;

    private ArrayList<Module> modules;
    ItemClicked activity;
    String uid = "default";



    public interface ItemClicked{
        void onItemClicked(int index);
    }


    public ModuleAdapter(Context context, ArrayList<Module>list){
        modules = list;
        activity = (ItemClicked) context;
        if(GoogleSignIn.getLastSignedInAccount(context) != null) {
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(context);
            uid = account.getId();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView ivLogo;
        TextView tvName;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivLogo = itemView.findViewById(R.id.ivLogo);
            tvName = itemView.findViewById(R.id.tvName);
            checkBox = itemView.findViewById(R.id.checkBox);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.onItemClicked(modules.indexOf((Module) v.getTag()));
                }
            });
        }
    }
    @NonNull
    @Override
    public ModuleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.module_items, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ModuleAdapter.ViewHolder holder, final int position) {
        holder.itemView.setTag(modules.get(position));
        holder.tvName.setText(modules.get(position).getName());


        if(modules.get(position).getLogo().equals("clock")){
            holder.ivLogo.setImageResource(R.drawable.clock);

            //write checkbox value to database
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    database = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("modules").child("clock").child("enabled");
                    if(isChecked){
                        database.setValue("true");
                    }
                    else{
                        database.setValue("false");
                    }
                }
            });

            //read checkbox value from database
            database = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("modules").child("clock").child("enabled");
            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue().toString().equals("true")) {
                        holder.checkBox.setChecked(true);
                    } else {
                        holder.checkBox.setChecked(false);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
        else if(modules.get(position).getLogo().equals("weather")){
            holder.ivLogo.setImageResource(R.drawable.weather);

            //write checkbox value to database
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    database = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("modules").child("weather").child("enabled");
                    if(isChecked){
                        database.setValue("true");
                    }
                    else{
                        database.setValue("false");
                    }
                }
            });

            //read checkbox value from database
            database = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("modules").child("weather").child("enabled");
            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue().toString().equals("true")) {
                        holder.checkBox.setChecked(true);
                    } else {
                        holder.checkBox.setChecked(false);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
        else if(modules.get(position).getLogo().equals("date")){
            holder.ivLogo.setImageResource(R.drawable.date);

            //write checkbox value to database
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    database = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("modules").child("date").child("enabled");
                    if(isChecked){
                        database.setValue("true");
                    }
                    else{
                        database.setValue("false");
                    }
                }
            });

            //read checkbox value from database
            database = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("modules").child("date").child("enabled");
            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue().toString().equals("true")) {
                        holder.checkBox.setChecked(true);
                    } else {
                        holder.checkBox.setChecked(false);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
        else if(modules.get(position).getLogo().equals("calendar")){
            holder.ivLogo.setImageResource(R.drawable.date);

            //write checkbox value to database
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    database = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("modules").child("calendar").child("enabled");
                    if(isChecked){
                        database.setValue("true");
                    }
                    else{
                        database.setValue("false");
                    }
                }
            });

            //read checkbox value from database
            database = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("modules").child("calendar").child("enabled");
            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue().toString().equals("true")) {
                        holder.checkBox.setChecked(true);
                    } else {
                        holder.checkBox.setChecked(false);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
        else if(modules.get(position).getLogo().equals("reddit")){
            holder.ivLogo.setImageResource(R.drawable.chat);

            //write checkbox value to database
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    database = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("modules").child("reddit").child("enabled");
                    if(isChecked){
                        database.setValue("true");
                    }
                    else{
                        database.setValue("false");
                    }
                }
            });

            //read checkbox value from database
            database = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("modules").child("reddit").child("enabled");
            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue().toString().equals("true")) {
                        holder.checkBox.setChecked(true);
                    } else {
                        holder.checkBox.setChecked(false);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }




    }

    @Override
    public int getItemCount() {
        return modules.size();
    }
}
