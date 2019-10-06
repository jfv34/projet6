package com.vincler.jf.projet6.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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
import androidx.lifecycle.MutableLiveData;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.vincler.jf.projet6.PageAdapter;
import com.vincler.jf.projet6.R;
import com.vincler.jf.projet6.UnsafeOkHttpClient;
import com.vincler.jf.projet6.data.RestaurantsService;
import com.vincler.jf.projet6.fragments.MapFragment;
import com.vincler.jf.projet6.models.Restaurant;
import com.vincler.jf.projet6.models.SearchStatus;
import com.vincler.jf.projet6.models.googleMapResponse.DetailsResponse;
import com.vincler.jf.projet6.models.googleMapResponse.ListRestaurantResponse;
import com.vincler.jf.projet6.models.googleMapResponse.RestaurantResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private final int RC_SIGN_IN = 123;

    RestaurantsService service;
    public MutableLiveData<ArrayList<Restaurant>> restaurantsData = new MutableLiveData<>(new ArrayList<>());

    private BottomNavigationView bottomNavigationView;
    private ViewPager viewPager;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    public EditText customEditText;
    private ImageButton searchButton;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.activity_main_bottom_nav_view);
        viewPager = findViewById(R.id.activity_main_viewpager);
        toolbar = findViewById(R.id.toolbar);
        customEditText = findViewById(R.id.toolbar_customEditText);
        searchButton = findViewById(R.id.toolbar_searchButton_imButton);
        drawerLayout = findViewById(R.id.fragment_map_drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        retrofit();
        configureViews();
        displayToolbar();
        firebaseUI();

    }

    private void retrofit() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builder = UnsafeOkHttpClient.getUnsafeOkHttpClient().addInterceptor(interceptor);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/place/")
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(RestaurantsService.class);
    }

    private void configureViews() {
        viewPager();
        bottomView();
    }

    public void displayToolbar() {

        setSupportActionBar(toolbar);
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

        searchButton.setOnClickListener(v -> displaySearchBar());
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
        getSupportActionBar().setTitle("    " + getString(R.string.title_hungry));
    }

    private void noDisplayTitle() {
        getSupportActionBar().setTitle("");
    }

    private void editTextListener() {
        customEditText.setOnEditorActionListener((v, actionId, event) -> {

            if (actionId == EditorInfo.IME_ACTION_DONE) {
                String restaurant = v.getText().toString();
                closeKeyboard();
                displayToolbar();

                return true;
            }
            return false;
        });
    }

    public void hideRestaurantsNotSearched(String searchText) {

        if (searchText.isEmpty()) {
            searchText = customEditText.getText().toString();
        }

        int restaurantsDataSize = restaurantsData.getValue().size();
        Log.i("tag_search", searchText);

        for (int i = 0; i < restaurantsDataSize; i++) {
            String r = restaurantsData.getValue().get(i).getName();
            Restaurant data = restaurantsData.getValue().get(i);
            if (!r.toLowerCase().contains(searchText.toLowerCase())) {

                Restaurant restaurantModified = new Restaurant(
                        data.getName(),
                        data.getLatitude(),
                        data.getLongitude(),
                        data.getAddress(),
                        data.getPhoto(),
                        data.getRating(),
                        SearchStatus.DO_NOT_DISPLAY,
                        restaurantsData.getValue().get(i).getOpening_hours_List(),  // not changed
                        data.getPlaceid()
                );

                restaurantsData.getValue().set(i, (restaurantModified));
            } else {
                Restaurant restaurantModified = new Restaurant(
                        data.getName(),
                        data.getLatitude(),
                        data.getLongitude(),
                        data.getAddress(),
                        data.getPhoto(),
                        data.getRating(),
                        SearchStatus.DEFAULT,
                        restaurantsData.getValue().get(i).getOpening_hours_List(), // not changed
                        data.getPlaceid()
                );

                restaurantsData.getValue().set(i, (restaurantModified));
            }
        }
    }

    private void closeKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
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
                disconnectUser();
                break;
            default:
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    public void findRestaurantsNearCoordinates(double latitude, double longitude) {
        String locationRequest = latitude + "," + longitude;

        service.listRestaurants(locationRequest, "1000").enqueue(new Callback<ListRestaurantResponse>() {

            @Override
            public void onResponse(Call<ListRestaurantResponse> call, Response<ListRestaurantResponse> response) {


                Log.i("tag_response", "ok");
                if (!response.body().getResults().isEmpty()) {
                    getDataRestaurants(response);
                }
            }

            @Override
            public void onFailure(Call<ListRestaurantResponse> call, Throwable t) {
                Log.i("tag_response", "onFailure");
                t.printStackTrace();
            }
        });
    }

    public void findDetailsRestaurants() {

        for (int restau = 0; restau < restaurantsData.getValue().size(); restau++) {
            String placeid = restaurantsData.getValue().get(restau).getPlaceid();

            service.detailsRestaurants(placeid).enqueue(new Callback<DetailsResponse>() {

                @Override
                public void onResponse(Call<DetailsResponse> call, Response<DetailsResponse> response) {

                    Log.i("tag_getdetailrestau", "ok");
                    // getDetailRestaurants(response);
                }

                @Override
                public void onFailure(Call<DetailsResponse> call, Throwable t) {

                }

            });
        }
    }


    private void getDataRestaurants(Response<ListRestaurantResponse> response) {

        ArrayList newRestaurants = new ArrayList();
        Log.i("tag_newRestau", "getDataRestau");

        int sizeRestaurantsData = response.body().results.size();

        for (int i = 0; i < sizeRestaurantsData; i++) {

            RestaurantResponse res = response.body().getResults().get(i);
            ArrayList<String> opening_hours_List = null;

            Restaurant restaurant = new Restaurant(res.getName(), res.getLatitude(), res.getLongitude(),
                    res.getAddress(), res.getPhoto(), res.getRating(), SearchStatus.DEFAULT, opening_hours_List, res.getPlaceid());
            newRestaurants.add(i, restaurant);

        }
        restaurantsData.setValue(newRestaurants);
    }

   /* private void getDetailRestaurants(Response<DetailsResponse> response) {


        ArrayList<String> opening_hours_List = new ArrayList<>();



        int sizeDetailsData = response.size();
        response.get(0);
        for (int restau = 0; restau < sizeDetailsData; restau++) {

            String opening_of_this_day = null;
            for (int day = 0; day < 6; day++) {
                opening_of_this_day = response.get(restau)
                        .getOpening_hours().get(day).toString();
            }
            opening_hours_List.set(restau, opening_of_this_day);

            Restaurant res = restaurantsData.getValue().get(restau);

            Restaurant restaurant = new Restaurant(
                    res.getName(),
                    res.getLatitude(),
                    res.getLongitude(),
                    res.getAddress(),
                    res.getPhoto(),
                    res.getSearchStatus(),
                    opening_hours_List,
                    res.getPlaceid()
            );

            newRestaurants.add(restau, restaurant);
        }
        restaurantsData.setValue(newRestaurants);
    }*/


    private void drawerLayout() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void navigationView() {
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void firebaseUI() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {

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
        } else {
            displayUserInNavDrawer();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == RC_SIGN_IN) {

            if (resultCode == RESULT_OK) {
                toast(R.string.connectActivity_toast_successful);

                displayUserInNavDrawer();

            } else {
                toast(R.string.connectActivity_toast_failed);
            }
        }
    }


    private void disconnectUser() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        recreate();
                    }
                });
    }

    private void displayUserInNavDrawer() {
        View headerLayout =
                navigationView.inflateHeaderView(R.layout.activity_main_nav_header);

        TextView viewName = headerLayout.findViewById(R.id.nav_header_name_and_surname_tv);
        TextView viewMail = headerLayout.findViewById(R.id.nav_header_mail_tv);
        ImageView imageView = headerLayout.findViewById(R.id.nav_header_iv);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            viewName.setText(firebaseUser.getDisplayName());
            viewMail.setText(firebaseUser.getEmail());
            Glide.with(this)
                    .load(firebaseUser.getPhotoUrl())
                    .into(imageView);
        }


    }

    @Override
    public void onBackPressed() {
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void viewPager() {

        viewPager.setAdapter(new PageAdapter(getSupportFragmentManager()));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void bottomView() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_bottom_map:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.menu_bottom_listview:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.menu_bottom_workmates:
                        viewPager.setCurrentItem(2);
                }
                return true;
            }
        });
    }

    private void toast(int message) {
        Toast toast = Toast.makeText(this, getString(message), Toast.LENGTH_LONG);
        toast.show();
    }



}



