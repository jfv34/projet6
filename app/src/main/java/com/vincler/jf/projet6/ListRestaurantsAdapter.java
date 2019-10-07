package com.vincler.jf.projet6;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import static java.lang.Double.parseDouble;

public class ListRestaurantsAdapter extends RecyclerView.Adapter<ListRestaurantsAdapter.ViewHolder> {

    private final String API_KEY = "AIzaSyDxfJVIikFlDrFiDOQsfG7cFeQICbmZrtc";
    private static final int WIDTH_PHOTO = 50;
    private Double latitudeUser;
    private Double longitudeUser;
    Context context;

    List<String> name;
    List<String> address;
    List<Double> latitude;
    List<Double> longitude;
    List<String> photo;
    List<Double> rating;

    public static class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(@NonNull View itemView) {

            super(itemView);

        }
    }

    public ListRestaurantsAdapter(List name, List address, List photo, List rating, List latitude,
                                  List longitude, Double latitudeUser, Double longitudeUser) {
        this.name = name;
        this.address = address;
        this.photo = photo;
        this.rating = rating;
        this.latitude = latitude;
        this.longitude = longitude;
        this.latitudeUser = latitudeUser;
        this.longitudeUser = longitudeUser;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {

        this.context = parent.getContext();


        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_restaurant, parent, false);


        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        TextView name_tv = holder.itemView.findViewById(R.id.item_restaurant_name_tv);
        TextView address_tv = holder.itemView.findViewById(R.id.item_restaurant_address_tv);
        TextView distance_tv = holder.itemView.findViewById(R.id.item_restaurant_distance_tv);
        ImageView photo_iv = holder.itemView.findViewById(R.id.item_restaurant_photo_iv);

        ImageView star1_iv = holder.itemView.findViewById(R.id.item_restaurant_star1_iv);
        ImageView star2_iv = holder.itemView.findViewById(R.id.item_restaurant_star2_iv);
        ImageView star3_iv = holder.itemView.findViewById(R.id.item_restaurant_star3_iv);
        ImageView star4_iv = holder.itemView.findViewById(R.id.item_restaurant_star4_iv);
        ImageView star5_iv = holder.itemView.findViewById(R.id.item_restaurant_star5_iv);

        display_name(name_tv, position);
        display_address(address_tv, position);



        double longitudeRestaurant = longitude.get(position);
        double latitudeRestaurant = latitude.get(position);

        if(latitudeUser!=null && longitudeUser!=null){
            Log.i("tag_latitudeUser",latitudeUser.toString());
        display_distance(distance_tv, latitudeUser, longitudeUser,
                latitudeRestaurant, longitudeRestaurant);}

        display_rating(star1_iv, star2_iv, star3_iv, star4_iv, star5_iv, position);
        display_photo(photo_iv, position);
    }

    private void display_photo(ImageView photo_iv, int position) {

        String photoRef = photo.get(position);
        String url = "https://maps.googleapis.com/maps/api/place/photo?"
                + "maxwidth=" + WIDTH_PHOTO
                + "&photoreference=" + photoRef
                + "&key=" + "AIzaSyDxfJVIikFlDrFiDOQsfG7cFeQICbmZrtc";

        Log.i("tag_url", url);

        Glide.with(context).load(url).into(photo_iv);
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

    private void display_distance(TextView distance_tv,
                                  Double latitudeUser, Double longitudeUser,
                                  Double latitudeRestaurant, Double longitudeRestaurant) {

        double dist = 0.0;
        dist = calculateDistance(longitudeUser, latitudeUser, longitudeRestaurant, latitudeRestaurant);
        distance_tv.setText(String.valueOf(dist));
    }

    private void display_address(TextView address_tv, int position) {
        String addr = address.get(position);
        address_tv.setText(addr.substring(0, addr.indexOf(",")));
    }

    private void display_name(TextView name_tv, int position) {
        name_tv.setText(name.get(position));
    }

    private double calculateDistance(double fromLong, double fromLat,
                                     double toLong, double toLat) {
        double d2r = Math.PI / 180;
        double dLong = (toLong - fromLong) * d2r;
        double dLat = (toLat - fromLat) * d2r;
        double a = Math.pow(Math.sin(dLat / 2.0), 2) + Math.cos(fromLat * d2r)
                * Math.cos(toLat * d2r) * Math.pow(Math.sin(dLong / 2.0), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = 6367000 * c;
        return Math.round(d);
    }

    @Override
    public int getItemCount() {
        return name.size();
    }
}