package com.vincler.jf.projet6.ui.workmates;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;
import com.vincler.jf.projet6.api.UserFirebase;
import com.vincler.jf.projet6.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WorkmatesFragmentPresenter implements WorkmatesFragmentContract.Presenter {

    private WorkmatesFragmentContract.View view;

    public WorkmatesFragmentPresenter(WorkmatesFragmentContract.View view) {
        this.view = view;
    }

    @Override
    public void loadWorkmates() {
        ArrayList<User> users = new ArrayList<>();
        List<HashMap> result = new ArrayList<>();
        Task<QuerySnapshot> data = UserFirebase.getUsers();
        data.addOnCompleteListener(task -> {
            if (data.getResult() != null) {

                for (int i = 0; i < data.getResult().size(); i++) {
                    HashMap h = (HashMap) data.getResult().getDocuments().get(i).getData();
                    result.add(h);
                }

                for (int i = 0; i < result.size(); i++) {
                    HashMap hm = result.get(i);
                    User user = new User(
                            hm.get("uid").toString(),
                            hm.get("username").toString(),
                            hm.get("email").toString(),
                            hm.get("phoneNumber").toString(),
                            hm.get("restaurantFavoriteId").toString(),
                            hm.get("restaurantFavoriteName").toString(),
                            hm.get("photoUserUrl").toString());

                    users.add(user);
                }

                view.displayWorkmates(users);
            }
        });
    }
}
