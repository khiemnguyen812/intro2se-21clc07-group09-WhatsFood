package com.example.whatsfood.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsfood.Model.Food;
import com.example.whatsfood.R;

import java.util.ArrayList;

public class AdapterFood extends RecyclerView.Adapter<AdapterFood.MyViewHolder> {
    ArrayList <Food> list;
    public AdapterFood(ArrayList <Food> list) {
        this.list = list;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_holder_buyer,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
           holder.id.setText(list.get(i).getFoodId());
           holder.id.setText(list.get(i).getDescription());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView id,desc;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.food_name);
            desc = itemView.findViewById(R.id.description);
        }
    }
}
