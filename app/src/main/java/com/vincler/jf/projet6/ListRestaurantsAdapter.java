package com.vincler.jf.projet6;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ListRestaurantsAdapter extends RecyclerView.Adapter<ListRestaurantsAdapter.ViewHolder> {

    private String[] name;
    private String[] address;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public ListRestaurantsAdapter(String[] name, String[] address) {
        this.name = name;
        this.address = address;
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

        name_tv.setText(name[position]);
        address_tv.setText(address[position]);

    }

    @Override
    public int getItemCount() {
        return name.length;
    }
}