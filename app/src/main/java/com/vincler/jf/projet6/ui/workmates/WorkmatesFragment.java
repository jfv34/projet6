package com.vincler.jf.projet6.ui.workmates;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;
import com.vincler.jf.projet6.R;
import com.vincler.jf.projet6.api.UserFirebase;
import com.vincler.jf.projet6.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WorkmatesFragment extends Fragment {

    public static WorkmatesFragment newInstance() {

        return new WorkmatesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_workmates, container, false);

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
                            hm.get("restaurantChoice").toString(),
                            hm.get("restaurantName").toString(),
                            hm.get("photoUserUrl").toString());

                    users.add(user);
                }

                RecyclerView recyclerView = rootView.findViewById(R.id.fragment_workmates_recyclerview);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setHasFixedSize(true);
                RecyclerView.Adapter adapter = new ListWorkmatesAdapter(users, getContext());
                recyclerView.setAdapter(adapter);

                recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                        DividerItemDecoration.VERTICAL));
            }
        });

        return rootView;
    }
}