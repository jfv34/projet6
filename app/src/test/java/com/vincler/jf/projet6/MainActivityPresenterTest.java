package com.vincler.jf.projet6;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.google.firebase.auth.FirebaseUser;
import com.vincler.jf.projet6.models.Search;
import com.vincler.jf.projet6.models.User;
import com.vincler.jf.projet6.models.restaurants.nearby.NearbyRestaurant;
import com.vincler.jf.projet6.ui.main.MainActivityContract;
import com.vincler.jf.projet6.ui.main.MainActivityPresenter;

import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import java.util.ArrayList;

public class MainActivityPresenterTest {

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    @Test
    public void When_query_is_not_nul_restaurant_are_filtered() {

        MainActivityContract.View view = new MainActivityContract.View() {

            @Override
            public void displayUserInformation(User user) {

            }

            @Override
            public void startLogin() {

            }

            @Override
            public void instanceSearchFragment(ArrayList<Search> search) {

            }
        };
        MainActivityPresenter presenter = new MainActivityPresenter(view);

        ArrayList<NearbyRestaurant> restaurantArrayList = new ArrayList<>();

        NearbyRestaurant restaurant1 = new NearbyRestaurant("test_no_filtred", 112.2, 26262,
                "test", "test", 1, true, "test",
                3,"test");

        NearbyRestaurant restaurant2 = new NearbyRestaurant("TEST_FILTER", 112.2, 26262,
                "test", "test", 2, true, "test",
                2,"test");

        restaurantArrayList.add(0, restaurant1);
        restaurantArrayList.add(1, restaurant2);
        presenter.getLiveData().setValue(restaurantArrayList);
        presenter.filterRestaurants("TEST_FILTER");

        Assertions.assertThat(presenter.restaurantsData.getValue().get(0).isVisible()).isFalse();
        Assertions.assertThat(presenter.restaurantsData.getValue().get(1).isVisible()).isTrue();

    }
}
