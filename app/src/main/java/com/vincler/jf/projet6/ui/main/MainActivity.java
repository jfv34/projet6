package com.vincler.jf.projet6.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.vincler.jf.projet6.BuildConfig;
import com.vincler.jf.projet6.R;
import com.vincler.jf.projet6.api.UserFirebase;
import com.vincler.jf.projet6.models.Search;
import com.vincler.jf.projet6.models.User;
import com.vincler.jf.projet6.ui.SharedData;
import com.vincler.jf.projet6.ui.list.ListFragment;
import com.vincler.jf.projet6.ui.map.MapFragment;
import com.vincler.jf.projet6.ui.restaurant.RestaurantActivity;
import com.vincler.jf.projet6.ui.search.SearchAdapter;
import com.vincler.jf.projet6.ui.settings.SettingsActivity;
import com.vincler.jf.projet6.ui.workmates.WorkmatesFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainActivityContract.View, NavigationView.OnNavigationItemSelectedListener {

    public MainActivityContract.Presenter presenter = new MainActivityPresenter(this);

    private final int RC_SIGN_IN = 123;
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    public EditText customEditText;
    private ImageButton searchButton;
    private NavigationView navigationView;
    private RecyclerView recyclerView;
    private boolean alreadyClosed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Places.initialize(getApplicationContext(), BuildConfig.API_KEY);
        bottomNavigationView = findViewById(R.id.activity_main_bottom_nav_view);
        toolbar = findViewById(R.id.toolbar_main);
        customEditText = findViewById(R.id.toolbar_customEditText);
        searchButton = findViewById(R.id.toolbar_searchButton_imButton);
        drawerLayout = findViewById(R.id.activity_main);
        navigationView = findViewById(R.id.nav_view);
        recyclerView = findViewById(R.id.activity_main_recycler_view);
        presenter.loadUser();
    }

    public void displayViews() {
        bottomView();
        displayToolbar();
    }

    final Fragment fragment1 = new MapFragment();
    final Fragment fragment2 = new ListFragment();
    final Fragment fragment3 = new WorkmatesFragment();
    Fragment active = fragment1;


    private void bottomView() {
        getSupportFragmentManager().beginTransaction().add(R.id.activity_main_frameLayout, fragment3, "3").hide(fragment3).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.activity_main_frameLayout, fragment2, "2").hide(fragment2).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.activity_main_frameLayout,fragment1, "1").commit();
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.menu_bottom_map:
                    bottomNavigationView.getMenu().getItem(0).setChecked(true);
                    switchFragment(fragment1);
                    break;
                case R.id.menu_bottom_listview:
                    bottomNavigationView.getMenu().getItem(1).setChecked(true);
                    switchFragment(fragment2);
                    break;
                case R.id.menu_bottom_workmates:
                    bottomNavigationView.getMenu().getItem(2).setChecked(true);
                    switchFragment(fragment3);
            }
            return true;
        });
    }

    private void switchFragment(Fragment fragment){
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().hide(active).show(fragment).commit();
        active = fragment;
    }

    private void drawerLayout() {

        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        SharedData.favoritedRestaurant.observe(this, restaurant ->
                navigationView.getMenu().getItem(0).setVisible(restaurant != null && !restaurant.isEmpty()));
    }

    public void displayToolbar() {

        setSupportActionBar(toolbar);
        noDisplayEditText();
        displayTitle();
        displaySearchButton();
        drawerLayout();
    }

    public void noDisplayEditText() {

        customEditText.setVisibility(View.GONE);
    }

    private void displayTitle() {
        toolbar.setTitle(getString(R.string.title_hungry));
        getSupportActionBar().setTitle(getString(R.string.title_hungry));
    }

    private void displaySearchButton() {

        searchButton.setVisibility(View.VISIBLE);
        searchButtonListener();
    }

    private void searchButtonListener() {

        searchButton.setOnClickListener(v -> displaySearchBar());
    }

    private void displaySearchBar() {

        noDisplayTitle();
        noDisplaySearchButton();
        noDisplayNavigationIcon();
        displayEditText();
    }

    private void noDisplayTitle() {
        getSupportActionBar().setTitle("");
    }

    private void noDisplaySearchButton() {
        searchButton.setVisibility(View.INVISIBLE);
    }

    private void noDisplayNavigationIcon() {
        toolbar.setNavigationIcon(null);
    }

    private void displayEditText() {
        customEditText.setVisibility(View.VISIBLE);
        eraseEditText();
        editTextListener();
    }

    private void editTextListener() {

        PlacesClient placesClient = com.google.android.libraries.places.api.Places.createClient(getApplicationContext());

        customEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.toString().equals("")) {
                    if (!alreadyClosed) {
                        closeKeyboard();
                        displayToolbar();
                        presenter.clearSearchList();
                        alreadyClosed = true;
                    }
                } else {
                    alreadyClosed = false;
                    presenter.autocompleteRequest(s, placesClient);
                }
            }
        });

        customEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                closeKeyboard();
                displayToolbar();
                return true;
            }
            return false;
        });
    }

    public void closeKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void eraseEditText() {
        customEditText.setText("");
    }

    @Override
    public void updateSearch(ArrayList<Search> searchList) {

        if (searchList != null && !searchList.isEmpty()) {
            RecyclerView.Adapter adapter = new SearchAdapter(searchList, search -> {
                presenter.search(search);
            });
            recyclerView.setAdapter(adapter);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.activity_main_drawer_1:
                restaurantActivityIntent();
                break;
            case R.id.activity_main_drawer_2:
                settingsFragmentIntent();
                break;
            case R.id.activity_main_drawer_3:
                disconnectUser();

                break;
            default:
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void restaurantActivityIntent() {

        String uid = presenter.getUidFirebase();
        Task<DocumentSnapshot> data = UserFirebase.getUser(uid);

        data.addOnCompleteListener(task -> {
            if (data.getResult() != null) {
                String restaurantDisplayedId = data.getResult().getData().get("restaurantFavoriteId").toString();
                if (!restaurantDisplayedId.equals("")) {
                    Intent intent = new Intent(getApplicationContext(), RestaurantActivity.class);
                    intent.putExtra("restaurantDisplayedId", restaurantDisplayedId);
                    startActivity(intent);
                }
            }
        });
    }

    private void settingsFragmentIntent() {

        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(intent);
    }

    private void disconnectUser() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(task -> {
                    startLogin();
                });
    }

    @Override
    public void startLogin() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build(),
                new AuthUI.IdpConfig.TwitterBuilder().build()
        );

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setTheme(R.style.LoginTheme)
                        .setLogo(R.drawable.logo)
                        .build(),
                RC_SIGN_IN);
        drawerLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                drawerLayout.setVisibility(View.VISIBLE);
                toast(R.string.connectActivity_toast_successful);
                presenter.loadUser();
            } else {
                finish();
            }
        }
    }

    @Override
    public void displayUserInformation(User user) {
        View headerLayout = navigationView.inflateHeaderView(R.layout.activity_main_nav_header);

        TextView viewName = headerLayout.findViewById(R.id.nav_header_name_and_surname_tv);
        TextView viewMail = headerLayout.findViewById(R.id.nav_header_mail_tv);
        ImageView imageView = headerLayout.findViewById(R.id.nav_header_iv);

        viewName.setText(user.getUsername());
        viewMail.setText(user.getEmail());
        Glide.with(this)
                .load(user.getPhotoUserUrl())
                .into(imageView);
    }

    @Override
    public void onBackPressed() {

        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void toast(int message) {
        Toast toast = Toast.makeText(this, getString(message), Toast.LENGTH_LONG);
        toast.show();
    }

}