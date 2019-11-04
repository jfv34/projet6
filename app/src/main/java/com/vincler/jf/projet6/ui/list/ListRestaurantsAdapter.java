package com.vincler.jf.projet6.ui.list;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vincler.jf.projet6.R;
import com.vincler.jf.projet6.models.Restaurant;
import com.vincler.jf.projet6.ui.restaurant.RestaurantActivity;
import com.vincler.jf.projet6.utils.GetStringUtils;
import com.vincler.jf.projet6.utils.KeysUtils;

import java.util.List;

import static com.vincler.jf.projet6.utils.DistanceUtils.calculateDistance;

public class ListRestaurantsAdapter extends RecyclerView.Adapter<ListRestaurantsAdapter.ViewHolder> {

    private static final int WIDTH_PHOTO = 50;
    private static final String API_KEY = KeysUtils.API_KEY;
    private Context context;
    private List<Restaurant> restaurants;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public ListRestaurantsAdapter(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_restaurant, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Location location = getLocation();

        TextView name_tv = holder.itemView.findViewById(R.id.item_restaurant_name_tv);
        TextView address_tv = holder.itemView.findViewById(R.id.item_restaurant_address_tv);
        TextView distance_tv = holder.itemView.findViewById(R.id.item_restaurant_distance_tv);
        TextView openingHours_tv = holder.itemView.findViewById(R.id.item_restaurant_opening_hours_tv);
        ImageView photo_iv = holder.itemView.findViewById(R.id.item_restaurant_photo_iv);
        TextView workmatesNumber_tv = holder.itemView.findViewById(R.id.item_restaurant_numberOfWorkmates_tv);
        ImageView star1_iv = holder.itemView.findViewById(R.id.item_restaurant_star1_iv);
        ImageView star2_iv = holder.itemView.findViewById(R.id.item_restaurant_star2_iv);
        ImageView star3_iv = holder.itemView.findViewById(R.id.item_restaurant_star3_iv);
        ImageView star4_iv = holder.itemView.findViewById(R.id.item_restaurant_star4_iv);
        ImageView star5_iv = holder.itemView.findViewById(R.id.item_restaurant_star5_iv);

        display_name(name_tv, position);
        display_address(address_tv, position);
        display_rating(star1_iv, star2_iv, star3_iv, star4_iv, star5_iv, position);
        display_photo(photo_iv, position);
        display_opening(openingHours_tv, position);
        display_distance(distance_tv, position, location.getLatitude(), location.getLongitude());
        listenerClickOnRestaurant(holder, position, photo_iv);
    }

    private Location getLocation() {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        @SuppressLint("MissingPermission")
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        return location;
    }

    private void listenerClickOnRestaurant(ViewHolder holder, int position, View photo_iv) {

        View view = holder.itemView.findViewById(R.id.item_restaurant);
        view.setOnClickListener(v -> restaurantActivityIntent(position, photo_iv));
    }

    private void restaurantActivityIntent(int position, View photo_iv) {

        Restaurant restau = restaurants.get(position);

        Intent intent = new Intent(context.getApplicationContext(), RestaurantActivity.class);
        intent.putExtra("restaurant",restau);
        intent.putExtra("profile",ViewCompat.getTransitionName(photo_iv));

        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation((Activity)context,photo_iv, ViewCompat.getTransitionName(photo_iv));
        context.startActivity(intent, options.toBundle());
    }

    private void display_opening(TextView openingHours_tv, int position) {

        String opening;
        switch (restaurants.get(position).getIsOpenNow()) {

            case "true":
                opening = context.getString(R.string.ouvert);
                break;
            case "false":
                opening = context.getString(R.string.ferme);
                openingHours_tv.setTextColor(Color.RED);
                break;
            default:
                opening = "";
        }
        openingHours_tv.setText(opening);
    }

    private void display_photo(ImageView photo_iv, int position) {
        String photoRef = restaurants.get(position).getPhoto();
        String url = "https://maps.googleapis.com/maps/api/place/photo?"
                + "maxwidth=" + WIDTH_PHOTO
                + "&photoreference=" + photoRef
                + "&key=" + API_KEY;

        Log.i("tag_url", url);

        Glide.with(context).load(url).dontTransform().into(photo_iv);
    }

    private void display_rating(
            ImageView star1_iv,
            ImageView star2_iv,
            ImageView star3_iv,
            ImageView star4_iv,
            ImageView star5_iv,
            int position
    ) {
        int ra = (int) restaurants.get(position).getRating();

        switch (ra) {
            case 0:
                star1_iv.setVisibility(View.INVISIBLE);
            case 1:
                star2_iv.setVisibility(View.INVISIBLE);
            case 2:
                star3_iv.setVisibility(View.INVISIBLE);
            case 3:
                star4_iv.setVisibility(View.INVISIBLE);
            case 4:
                star5_iv.setVisibility(View.INVISIBLE);
        }
    }

    private void display_distance(TextView distance_tv, int position, Double latitudeUser,
                                  Double longitudeUser) {
        double dist;
        if (latitudeUser != null && longitudeUser != null) {
            dist = calculateDistance(longitudeUser, latitudeUser, restaurants.get(position).getLongitude(),
                    restaurants.get(position).getLatitude());
            String text = (int) dist + "m";
            distance_tv.setText(text);
        }
    }

    private void display_address(TextView address_tv, int position) {
        String addr = restaurants.get(position).getAddress();
        address_tv.setText(addr.substring(0, addr.indexOf(",")));
    }

    private void display_name(TextView name_tv, int position) {
        String name = GetStringUtils.getNoCutLastWord(restaurants.get(position).getName(),31);
        name_tv.setText(name);
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }
}