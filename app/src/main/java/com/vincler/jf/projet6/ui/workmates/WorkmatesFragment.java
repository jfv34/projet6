package com.vincler.jf.projet6.ui.workmates;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vincler.jf.projet6.R;
import com.vincler.jf.projet6.models.User;

import java.util.ArrayList;

public class WorkmatesFragment extends Fragment implements WorkmatesFragmentContract.View {

    public static WorkmatesFragment newInstance() {
        return new WorkmatesFragment();
    }

    WorkmatesFragmentContract.Presenter presenter = new WorkmatesFragmentPresenter(this);

    private RecyclerView recyclerView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_workmates, container, false);

        recyclerView = rootView.findViewById(R.id.fragment_workmates_recyclerview);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter.loadWorkmates();
    }

    @Override
    public void displayWorkmates(ArrayList<User> users) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new ListWorkmatesAdapter(users, getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }
}