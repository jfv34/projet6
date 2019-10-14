package com.vincler.jf.projet6;

import com.vincler.jf.projet6.models.Restaurant;
import com.vincler.jf.projet6.ui.main.MainActivityContract;
import com.vincler.jf.projet6.ui.main.MainActivityPresenter;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class MainActivityPresenterTest {

    @Test
    public void When_() {

        MainActivityContract.View view = new MainActivityContract.View() {
        };
        MainActivityPresenter presenter = new MainActivityPresenter(view);

        ArrayList<Restaurant> restaurantArrayList = new ArrayList<>();

        Restaurant restaurant1 = new Restaurant("test",112.2,26262,
                "test","test",1.2,true,"test",
                "test");

        Restaurant restaurant2 = new Restaurant("TEST_FILTER",112.2,26262,
                "test","test",1.2,true,"test",
                "test");

        restaurantArrayList.add(0,restaurant1);
        restaurantArrayList.add(1,restaurant2);

        presenter.filterRestaurants("TEST_FILTER");

        Boolean restaurant1_isVisible = presenter.getRestaurantsData().getValue().get(0).isVisible();
        Boolean restaurant2_isNotVisible = !presenter.getRestaurantsData().getValue().get(1).isVisible();

        Assert.assertTrue(restaurant1_isVisible&&restaurant2_isNotVisible);

    }
}
