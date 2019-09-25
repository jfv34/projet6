package com.vincler.jf.projet6.data;

import com.vincler.jf.projet6.models.ListRestaurantResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RestaurantsService {

    @GET("json?type=restaurant&key=AIzaSyDxfJVIikFlDrFiDOQsfG7cFeQICbmZrtc\n")
    Call<ListRestaurantResponse> listRestaurants(@Query("location") String location, @Query("radius") String radius);

}









