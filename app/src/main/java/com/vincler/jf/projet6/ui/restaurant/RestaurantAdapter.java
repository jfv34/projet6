package com.vincler.jf.projet6.ui.restaurant;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vincler.jf.projet6.R;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> {

    private String[] name;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public RestaurantAdapter(String[] name) {
        this.name = name;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_workmates_in_this_restaurant, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        TextView name_tv = holder.itemView.findViewById(R.id.item_workmates_in_this_restaurant_name_tv);
        name_tv.setText(name[position]);
    }

    @Override
    public int getItemCount() {
        return name.length;
    }
}