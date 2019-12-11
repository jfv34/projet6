package com.vincler.jf.projet6.data;

import com.vincler.jf.projet6.models.googleMapResponse.GeometryResponse;
import com.vincler.jf.projet6.models.restaurants.details.DetailsRestaurantResponse;
import com.vincler.jf.projet6.models.restaurants.nearby.ListNearbyRestaurantResponse;
import com.vincler.jf.projet6.models.search.SearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RestaurantsService {

    @GET("nearbysearch/json?type=restaurant&fields=url,rating,opening_hours,photo&key=AIzaSyDxfJVIikFlDrFiDOQsfG7cFeQICbmZrtc")
    Call<ListNearbyRestaurantResponse> listRestaurants(
            @Query("location") String location,
            @Query("radius") String radius
    );

    @GET("details/json?fields=name,formatted_address,photo,website,international_phone_number&language=fr&key=AIzaSyDxfJVIikFlDrFiDOQsfG7cFeQICbmZrtc")
    Call<DetailsRestaurantResponse> listDetails(
            @Query("placeid") String placeid
    );

    @GET("details/json?fields=geometry&language=fr&key=AIzaSyDxfJVIikFlDrFiDOQsfG7cFeQICbmZrtc")
    Call<SearchResponse> geometry(
            @Query("placeid") String placeid
    );
}