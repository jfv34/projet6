package com.vincler.jf.projet6;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
    List<Double> rating;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public ListRestaurantsAdapter(List name, List address, List photo, List rating, List latitude,
                                  List longitude) {
        this.name = name;
        this.address = address;
        this.photo = photo;
        this.rating = rating;
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

        ImageView star1_iv = holder.itemView.findViewById(R.id.item_restaurant_star1_iv);
        ImageView star2_iv = holder.itemView.findViewById(R.id.item_restaurant_star2_iv);
        ImageView star3_iv = holder.itemView.findViewById(R.id.item_restaurant_star3_iv);
        ImageView star4_iv = holder.itemView.findViewById(R.id.item_restaurant_star4_iv);
        ImageView star5_iv = holder.itemView.findViewById(R.id.item_restaurant_star5_iv);

        display_name(name_tv, position);
        display_address(address_tv, position);
        display_distance(distance_tv, position);
        display_rating(star1_iv, star2_iv, star3_iv, star4_iv, star5_iv, position);
    }

    private void display_rating(ImageView star1_iv, ImageView star2_iv, ImageView star3_iv,
                                ImageView star4_iv, ImageView star5_iv, int position) {

        byte ra = rating.get(position).byteValue();

        switch (ra) {
            case (0):
                star1_iv.setVisibility(View.INVISIBLE);
            case (1):
                star2_iv.setVisibility(View.INVISIBLE);
            case (2):
                star3_iv.setVisibility(View.INVISIBLE);
            case (3):
                star4_iv.setVisibility(View.INVISIBLE);
            case (4):
                star5_iv.setVisibility(View.INVISIBLE);
        }
    }

    private void display_distance(TextView distance_tv, int position) {
        String dist = "999m";
        distance_tv.setText(dist);
    }

    private void display_address(TextView address_tv, int position) {
        String addr = address.get(position);
        address_tv.setText(addr.substring(0, addr.indexOf(",")));
    }

    private void display_name(TextView name_tv, int position) {
        name_tv.setText(name.get(position));
    }

    @Override
    public int getItemCount() {
        return name.size();
    }
}