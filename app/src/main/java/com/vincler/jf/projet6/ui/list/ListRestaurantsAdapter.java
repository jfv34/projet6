package com.vincler.jf.projet6.ui.list;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
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
import com.vincler.jf.projet6.models.restaurants.nearby.NearbyRestaurant;
import com.vincler.jf.projet6.ui.restaurant.RestaurantActivity;

import java.util.List;

import static com.vincler.jf.projet6.utils.DistanceUtils.calculateDistance;

public class ListRestaurantsAdapter extends RecyclerView.Adapter<ListRestaurantsAdapter.ViewHolder> {

    private Context context;
    private List<NearbyRestaurant> restaurants;
    private TextView workmatesNumber_tv;
    private ImageView workmates_iv;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public ListRestaurantsAdapter(List<NearbyRestaurant> restaurants) {
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
        workmatesNumber_tv = holder.itemView.findViewById(R.id.item_restaurant_numberOfWorkmates_tv);
        workmates_iv = holder.itemView.findViewById(R.id.item_restaurant_workmates_iv);
        ImageView star1_iv = holder.itemView.findViewById(R.id.item_restaurant_star1_iv);
        ImageView star2_iv = holder.itemView.findViewById(R.id.item_restaurant_star2_iv);
        ImageView star3_iv = holder.itemView.findViewById(R.id.item_restaurant_star3_iv);

        display_name(name_tv, position);
        display_address(address_tv, position);
        display_rating(star1_iv, star2_iv, star3_iv, position);
        display_photo(photo_iv, position);
        display_opening(openingHours_tv, position);
        display_distance(distance_tv, position, location.getLatitude(), location.getLongitude());
        display_workmatesNumber(position);
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

        String restaurantsDisplayedId = restaurants.get(position).getPlaceid();

        Intent intent = new Intent(context.getApplicationContext(), RestaurantActivity.class);
        intent.putExtra("restaurantDisplayedId", restaurantsDisplayedId);
        intent.putExtra("restaurantStars", restaurants.get(position).getStars());
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

        String url = restaurants.get(position).getMapsPhotoUrl();
        Glide.with(context).load(url).dontTransform().into(photo_iv);
    }

    private void display_rating(
            ImageView star1_iv,
            ImageView star2_iv,
            ImageView star3_iv,
            int position
    ) {
        int ra = restaurants.get(position).getStars();

        star1_iv.setVisibility(View.INVISIBLE);
        star2_iv.setVisibility(View.INVISIBLE);
        star3_iv.setVisibility(View.INVISIBLE);

        switch (ra) {
            case 3:
                star3_iv.setVisibility(View.VISIBLE);
            case 2:
                star2_iv.setVisibility(View.VISIBLE);
            case 1:
                star1_iv.setVisibility(View.VISIBLE);

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

    private void display_workmatesNumber(int position) {

        int number = restaurants.get(position).getWorkmatesNumber();

        if (number > 0) {
            String text = "(" + number + ")";
            workmatesNumber_tv.setText(text);
            workmatesNumber_tv.setVisibility(View.VISIBLE);
            workmates_iv.setVisibility(View.VISIBLE);
        } else {
            workmatesNumber_tv.setVisibility(View.INVISIBLE);
            workmates_iv.setVisibility(View.INVISIBLE);
        }
    }

    private void display_address(TextView address_tv, int position) {
        String addr = restaurants.get(position).getAddress();
        address_tv.setText(addr.substring(0, addr.indexOf(",")));
    }

    private void display_name(TextView name_tv, int position) {
        name_tv.setText(restaurants.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }
}