package com.vincler.jf.projet6;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.vincler.jf.projet6.models.Restaurant;
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
        };
        MainActivityPresenter presenter = new MainActivityPresenter(view);

        ArrayList<Restaurant> restaurantArrayList = new ArrayList<>();

        Restaurant restaurant1 = new Restaurant("test", 112.2, 26262,
                "test", "test", 1.2, true, "test",
                "test");

        Restaurant restaurant2 = new Restaurant("TEST_FILTER", 112.2, 26262,
                "test", "test", 1.2, true, "test",
                "test");

        restaurantArrayList.add(0, restaurant1);
        restaurantArrayList.add(1, restaurant2);
        presenter.getRestaurantsData().setValue(restaurantArrayList);
        presenter.filterRestaurants("TEST_FILTER");

        Assertions.assertThat(presenter.restaurantsData.getValue().get(0).isVisible()).isFalse();
        Assertions.assertThat(presenter.restaurantsData.getValue().get(1).isVisible()).isTrue();

    }
}
