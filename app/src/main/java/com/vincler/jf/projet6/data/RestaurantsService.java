package com.vincler.jf.projet6.data;

import com.vincler.jf.projet6.models.ListRestaurantResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RestaurantsService {

    @GET("nearbysearch/json?type=restaurant&key=AIzaSyDxfJVIikFlDrFiDOQsfG7cFeQICbmZrtc")
    Call<ListRestaurantResponse> listRestaurants(@Query("location") String location, @Query("radius") String radius);

    @GET("details/json?fields=opening_hours&key=AIzaSyDxfJVIikFlDrFiDOQsfG7cFeQICbmZrtc")
    Call<ListRestaurantResponse> opening_Hours_Restaurants(@Query("placeid") String placeid);
}









