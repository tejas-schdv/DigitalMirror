package com.example.digitalmirror;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ModuleAdapter extends RecyclerView.Adapter<ModuleAdapter.ViewHolder> {

    private ArrayList<Module> modules;
    ItemClicked activity;

    public interface ItemClicked{
        void onItemClicked(int index);
    }

    public ModuleAdapter(Context context, ArrayList<Module>list){
        modules = list;
        activity = (ItemClicked) context;
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
    public void onBindViewHolder(@NonNull ModuleAdapter.ViewHolder holder, int position) {
        holder.itemView.setTag(modules.get(position));
        holder.tvName.setText(modules.get(position).getName());

        if(modules.get(position).getLogo().equals("clock")){
            holder.ivLogo.setImageResource(R.drawable.clock);
        }
    }

    @Override
    public int getItemCount() {
        return modules.size();
    }
}
