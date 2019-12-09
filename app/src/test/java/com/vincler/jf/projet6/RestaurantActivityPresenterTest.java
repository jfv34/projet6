package com.vincler.jf.projet6;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.vincler.jf.projet6.models.User;
import com.vincler.jf.projet6.models.restaurants.details.Details;
import com.vincler.jf.projet6.models.restaurants.nearby.NearbyRestaurant;
import com.vincler.jf.projet6.ui.restaurant.RestaurantActivityContract;
import com.vincler.jf.projet6.ui.restaurant.RestaurantActivityPresenter;

import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import java.util.ArrayList;

public class RestaurantActivityPresenterTest {

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    /*@Test
    public void When_restaurant_is_loaded__view_is_updated_correctly() {

        final boolean[] viewUpdated = {false};
        final NearbyRestaurant[] viewRestaurant = new NearbyRestaurant[1];

        RestaurantActivityContract.View view = new RestaurantActivityContract.View() {
            @Override
            public void displayDetails(Details details) {

            }

            @Override
            public void displayFavorite(boolean isFavorited) {

            }

            @Override
            public void displayLike(boolean isLiked) {

            }

            @Override
            public void displayUsers(ArrayList<User> users) {

            }

            @Override
            public void displayRestaurant(Details details) {

            }

            @Override
            public void displayRestaurant(NearbyRestaurant restaurant) {
                viewUpdated[0] = true;
                viewRestaurant[0] = restaurant;
            }
        };

        RestaurantActivityPresenter presenter = new RestaurantActivityPresenter(context, view,
                new NearbyRestaurant(
                        "NearbyRestaurant de Paris",
                        112.2,
                        26262,
                        "test",
                        "test",
                        1,
                        true,
                        "test",
                        3,
                        "test")
        );

        presenter.loadRestaurant();

        Assertions.assertThat(viewUpdated[0]).isTrue();
        Assertions.assertThat(viewRestaurant[0].getName()).isEqualTo("NearbyRestaurant de Paris");
        Assertions.assertThat(viewRestaurant[0].getRating()).isEqualTo(1.2);
    }*/
}