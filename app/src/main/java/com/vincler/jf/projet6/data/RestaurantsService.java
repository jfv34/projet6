package com.vincler.jf.projet6.data;

import com.vincler.jf.projet6.models.googleMapResponse.ListRestaurantResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RestaurantsService {

    @GET("nearbysearch/json?type=restaurant&fields=url, rating,opening_hours,photo&key=AIzaSyDxfJVIikFlDrFiDOQsfG7cFeQICbmZrtc")
    Call<ListRestaurantResponse> listRestaurants(
            @Query("location") String location,
            @Query("radius") String radius
    );
}









