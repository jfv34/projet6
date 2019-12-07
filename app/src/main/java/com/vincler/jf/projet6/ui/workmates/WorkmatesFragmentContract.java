package com.vincler.jf.projet6.ui.workmates;


import com.vincler.jf.projet6.models.User;

import java.util.ArrayList;

public interface WorkmatesFragmentContract {

    interface View {
        void displayWorkmates(ArrayList<User> users);
    }

    interface Presenter {
        void loadWorkmates();
    }
}