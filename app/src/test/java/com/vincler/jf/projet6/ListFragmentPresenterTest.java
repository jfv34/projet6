package com.vincler.jf.projet6;

import com.vincler.jf.projet6.ui.list.ListFragmentContract;
import com.vincler.jf.projet6.ui.list.ListFragmentPresenter;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class ListFragmentPresenterTest {
    @Test
    public void When_likesNumber_is_46_and_likeSize_is_455_stars_is_2() {
        ListFragmentContract.View view = restaurants -> {
        };
        ListFragmentPresenter presenter = new ListFragmentPresenter(view);
        Assertions.assertThat(presenter.getStars(46, 455)).isEqualTo(2);
    }


}
