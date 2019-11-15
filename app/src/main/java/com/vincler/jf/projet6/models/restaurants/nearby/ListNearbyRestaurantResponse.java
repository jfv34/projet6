package com.vincler.jf.projet6.models.restaurants.nearby;

import java.util.List;
// use to deserialise nearby restaurants
public class ListNearbyRestaurantResponse {
    public List<NearbyRestaurantResponse> results;

    public List<NearbyRestaurantResponse> getResults() {
        return results;
    }
}