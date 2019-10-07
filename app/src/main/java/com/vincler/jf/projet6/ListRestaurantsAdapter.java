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

import com.apptakk.http_request.HttpRequest;
import com.apptakk.http_request.HttpRequestTask;
import com.apptakk.http_request.HttpResponse;
import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.vincler.jf.projet6.models.Opening_hours;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListRestaurantsAdapter extends RecyclerView.Adapter<ListRestaurantsAdapter.ViewHolder> {

    private final String API_KEY = "AIzaSyDxfJVIikFlDrFiDOQsfG7cFeQICbmZrtc";
    private String language = "fr";
    private static final int WIDTH_PHOTO = 50;
    Context context;

    List<String> name;
    List<String> address;
    List<Double> latitude;
    List<Double> longitude;
    List<String> photo;
    List<Double> rating;
    List<String> placeId;
    private Double latitudeUser;
    private Double longitudeUser;

    public static class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(@NonNull View itemView) {

            super(itemView);

        }
    }

    public ListRestaurantsAdapter(List name, List address, List photo, List rating, List latitude,
                                  List longitude, List placeId) {
        this.name = name;
        this.address = address;
        this.photo = photo;
        this.rating = rating;
        this.latitude = latitude;
        this.longitude = longitude;
        this.placeId = placeId;
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
        TextView openingHours_tv = holder.itemView.findViewById(R.id.item_restaurant_opening_hours_tv);
        ImageView photo_iv = holder.itemView.findViewById(R.id.item_restaurant_photo_iv);

        ImageView star1_iv = holder.itemView.findViewById(R.id.item_restaurant_star1_iv);
        ImageView star2_iv = holder.itemView.findViewById(R.id.item_restaurant_star2_iv);
        ImageView star3_iv = holder.itemView.findViewById(R.id.item_restaurant_star3_iv);
        ImageView star4_iv = holder.itemView.findViewById(R.id.item_restaurant_star4_iv);
        ImageView star5_iv = holder.itemView.findViewById(R.id.item_restaurant_star5_iv);

        display_name(name_tv, position);
        display_address(address_tv, position);
        display_rating(star1_iv, star2_iv, star3_iv, star4_iv, star5_iv, position);
        display_photo(photo_iv, position);
        display_distance(distance_tv, position, latitudeUser, longitudeUser);
        display_openingHours(openingHours_tv, position, language);
    }

    private void display_openingHours(TextView openingHours_tv, int position, String language) {

        //https://maps.googleapis.com/maps/api/place/details/json?placeid=ChIJY1FiPRC6j4ARzhKBypjO7eg&fields=opening_hours/weekday_text&language=fr&key=AIzaSyDxfJVIikFlDrFiDOQsfG7cFeQICbmZrtc
        String urlOpeningHours = "https://maps.googleapis.com/maps/api/place/details/json?placeid="
                + placeId.get(position)
                + "&fields=opening_hours"
                + "&language=" + language
                + "&key=" + API_KEY;
        Log.i("tag_url_hours", urlOpeningHours);


        new HttpRequestTask(
                new HttpRequest(urlOpeningHours, HttpRequest.POST, "{ \"some\": \"data\" }"),
                new HttpRequest.Handler() {
                    @Override
                    public void response(HttpResponse response) {
                        if (response.code == 200) {
                            Log.d(this.getClass().toString(), "Request successful!");
                            parseJson(response);
                        } else {
                            Log.e(this.getClass().toString(), "Request unsuccessful: " + response);
                        }
                    }
                }).execute();
    }

    private void parseJson(HttpResponse response) {
        String body = response.body;
        ArrayList<Opening_hours> opening_hours_List = new ArrayList<Opening_hours>();

        try {
            JSONObject bodyJson = new JSONObject(body);
            JSONObject result_Json = bodyJson.getJSONObject("result");
            JSONObject openingHours_Json = result_Json.getJSONObject("opening_hours");
            JSONArray periods_Json = openingHours_Json.getJSONArray("periods");

            for (int day = 0; day < 7; day++) {
         //       JsonObject day_Json = periods_Json.get(0).
            }

            Log.i("tag_json", periods_Json.toString());


        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    private void display_distance(TextView distance_tv, int position,
                                  Double latitudeUser, Double longitudeUser) {

        double dist = 0.0;
        if (latitudeUser != null && longitudeUser != null) {
            dist = calculateDistance(longitudeUser, latitudeUser,
                    longitude.get(position), latitude.get(position));
            distance_tv.setText(String.valueOf(dist));
        }
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