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

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int RC_SIGN_IN = 123;
    private BottomNavigationView bottomNavigationView;
    private ViewPager viewPager;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private EditText customEditText;
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

        configureViews();
        displayToolbar();
        firebaseUI();

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
                String text = v.getText().toString();
                Log.i("tag_text",text);
                closeKeyboard();
                displayToolbar();

                return true;
            }
            return false;
        });
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
        this.drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    private void drawerLayout() {

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void navigationView() {

        navigationView.setNavigationItemSelectedListener(this);
    }


    private void configureViews() {
        viewPager();
        bottomView();
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
                            .setLogo(R.drawable.ic_logo)
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

