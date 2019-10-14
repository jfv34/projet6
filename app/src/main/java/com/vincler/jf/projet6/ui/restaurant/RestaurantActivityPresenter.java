package com.vincler.jf.projet6.ui.restaurant;

public class RestaurantActivityPresenter implements RestaurantActivityContract.Presenter {

    private RestaurantActivityContract.View view;

    public RestaurantActivityPresenter(RestaurantActivityContract.View view) {
        this.view = view;
    }
}