package com.vincler.jf.projet6.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;
import com.vincler.jf.projet6.R;

public abstract class DrawerFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private EditText customEditText;
    private ImageButton searchButton;
    private NavigationView navigationView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        toolbar = rootView.findViewById(R.id.toolbar);
        customEditText = rootView.findViewById(R.id.activity_main_customEditText);
        searchButton = rootView.findViewById(R.id.activity_main_searchButton_imButton);
        drawerLayout = rootView.findViewById(R.id.fragment_map_drawer_layout);
        navigationView = rootView.findViewById(R.id.activity_main_nav_view);

        displayToolbar();

        return rootView;

    }

    public void displayToolbar() {

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        navigationView();
        noDisplayEditText();
        displayTitle();
        drawerLayout();
        displaySearchButton();
    }

    private void displaySearchBar() {

        noDisplayTitle();
        noDisplaySearchButton();
        noDisplayNavigationIcon();
        displayEditText();
    }

    private void searchButtonListener() {

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displaySearchBar();
            }
        });
    }

    private void displayEditText() {
        customEditText.setVisibility(View.VISIBLE);
        editTextListener();
    }

    private void noDisplayEditText() {

        customEditText.setVisibility(View.GONE);
    }

    private void noDisplayNavigationIcon() {
        toolbar.setNavigationIcon(null);
    }

    private void noDisplaySearchButton() {
        searchButton.setVisibility(View.INVISIBLE);
    }

    private void displaySearchButton() {
        searchButton.setVisibility(View.VISIBLE);
        searchButtonListener();
    }

    private void displayTitle() {
        toolbar.setTitle("    " + getString(R.string.title_hungry));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("    " + getString(R.string.title_hungry));
    }

    private void noDisplayTitle() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");
    }

    private void editTextListener() {
        customEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    closeKeyboard();
                    displayToolbar();
                    return true;
                }
                return false;
            }
        });
    }

    private void closeKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.activity_main_drawer_1:

                break;
            case R.id.activity_main_drawer_2:

                break;
            case R.id.activity_main_drawer_3:
                // disconnectUser();
                break;
            default:
                break;
        }
        this.drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    private void drawerLayout() {

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void navigationView() {

        navigationView.setNavigationItemSelectedListener(this);
    }
}



