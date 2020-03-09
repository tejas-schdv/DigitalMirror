package com.example.digitalmirror;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CalendarEventAdapter extends RecyclerView.Adapter<CalendarEventAdapter.ViewHolder> {
    DatabaseReference database;


    private ArrayList<CalendarEvent> calendarEvents;
    ItemClicked activity;


    public interface ItemClicked{
        void onItemClicked(int index);
    }


    public CalendarEventAdapter(Context context, ArrayList<CalendarEvent>list){
        calendarEvents = list;
        activity = (ItemClicked) context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView ivDot;
        TextView tvEventName, tvEventDateCard;
        Button btnDeleteEvent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivDot = itemView.findViewById(R.id.ivDot);
            tvEventName = itemView.findViewById(R.id.tvEventName);
            tvEventDateCard = itemView.findViewById(R.id.tvEventDateCard);
            btnDeleteEvent = itemView.findViewById(R.id.btnDeleteEvent);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.onItemClicked(calendarEvents.indexOf( v.getTag()));
                }
            });
        }
    }
    @NonNull
    @Override
    public CalendarEventAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_items, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final CalendarEventAdapter.ViewHolder holder, final int position) {
        holder.itemView.setTag(calendarEvents.get(position));
        holder.tvEventName.setText(calendarEvents.get(position).getEventName());
        holder.tvEventDateCard.setText(calendarEvents.get(position).getEventDate());

        if(calendarEvents.get(position).getDotColor().equals("red")){
            holder.ivDot.setImageResource(R.drawable.red);
        }
        else if(calendarEvents.get(position).getDotColor().equals("blue")){
            holder.ivDot.setImageResource(R.drawable.blue);
        }
        else if(calendarEvents.get(position).getDotColor().equals("yellow")){
            holder.ivDot.setImageResource(R.drawable.yellow);
        }
        else if(calendarEvents.get(position).getDotColor().equals("green")){
            holder.ivDot.setImageResource(R.drawable.green);
        }
        else if(calendarEvents.get(position).getDotColor().equals("purple")){
            holder.ivDot.setImageResource(R.drawable.purple);
        }
        else if(calendarEvents.get(position).getDotColor().equals("orange")){
            holder.ivDot.setImageResource(R.drawable.orange);
        }

        database = FirebaseDatabase.getInstance().getReference().child("modules").child("calendar").child("events");
        holder.btnDeleteEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = calendarEvents.get(position).getID();
                database.child(id).removeValue();
            }
        });

    }

    @Override
    public int getItemCount() {
        return calendarEvents.size();
    }
}
