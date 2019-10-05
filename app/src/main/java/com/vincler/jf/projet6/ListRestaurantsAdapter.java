package com.vincler.jf.projet6;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListRestaurantsAdapter extends RecyclerView.Adapter<ListRestaurantsAdapter.ViewHolder> {

    List<String> name;
    List<String> address;
    List<String> latitude;
    List<String> longitude;
    List<String> photo;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public ListRestaurantsAdapter(List name, List address, List photo, List latitude, List longitude) {
        this.name = name;
        this.address = address;
        this.photo = photo;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_restaurant, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        TextView name_tv = holder.itemView.findViewById(R.id.item_restaurant_name_tv);
        TextView address_tv = holder.itemView.findViewById(R.id.item_restaurant_address_tv);
        TextView distance_tv = holder.itemView.findViewById(R.id.item_restaurant_distance_tv);

        name_tv.setText(name.get(position));

        String addr = address.get(position);
        address_tv.setText(addr.substring(0, addr.indexOf(",")));

        String dist = "999m";
        distance_tv.setText(dist);

    }

    @Override
    public int getItemCount() {
        return name.size();
    }
}