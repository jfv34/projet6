package com.vincler.jf.projet6.ui.restaurant;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vincler.jf.projet6.R;
import com.vincler.jf.projet6.models.User;

import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> {

    private List<User> users;
    private String[] name;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public RestaurantAdapter(List users, String[] name) {
        this.users = users;
        this.name = name;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_restaurantworkmate, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ImageView photo_iv = holder.itemView.findViewById(R.id.item_workmates_photo_iv);
        TextView name_tv = holder.itemView.findViewById(R.id.item_workmates_in_this_restaurant_name_tv);


        //photo_iv.setImageDrawable();
        name_tv.setText(name[position]);
    }

    @Override
    public int getItemCount() {
        return name.length;
    }
}