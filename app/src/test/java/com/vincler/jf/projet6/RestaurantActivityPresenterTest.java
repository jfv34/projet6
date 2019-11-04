package com.vincler.jf.projet6;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.vincler.jf.projet6.models.Details;
import com.vincler.jf.projet6.models.Restaurant;
import com.vincler.jf.projet6.ui.restaurant.RestaurantActivityContract;
import com.vincler.jf.projet6.ui.restaurant.RestaurantActivityPresenter;

import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

public class RestaurantActivityPresenterTest {

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    @Test
    public void When_query_is_not_nul_restaurant_are_filtered() {

        final boolean[] detailsIsLoaded = {false};

        RestaurantActivityContract.View view = new RestaurantActivityContract.View() {
            @Override
            public void displayDetails(Details details) {
                detailsIsLoaded[0] = true;
            }
        };

        RestaurantActivityPresenter presenter = new RestaurantActivityPresenter(view,
                new Restaurant("test", 112.2, 26262,
                        "test", "test", 1.2, true, "test",
                        "test"));

        presenter.loadRestaurant();

        Assertions.assertThat(detailsIsLoaded[0]).isTrue();
    }

    @Test
    public void When_() {
        RestaurantActivityContract.View view = new RestaurantActivityContract.View(){
            @Override
            public void displayDetails(Details details) {

            }
        };
        RestaurantActivityPresenter presenter = new RestaurantActivityPresenter(view,
                new Restaurant("test", 112.2, 26262,
                "test", "test", 1.2, true, "test",
                "test"));



    }

}
