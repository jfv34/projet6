package com.vincler.jf.projet6;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.vincler.jf.projet6.model.User;

import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private static final int RC_SIGN_IN = 123;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


       setContentView(R.layout.activity_main);

        configureToolbar();
        configureDrawerLayout();
        configureNavigationView();
        configureSearchView();


        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build(),
                new AuthUI.IdpConfig.TwitterBuilder().build()
        );

/*        AuthMethodPickerLayout customLayout = new AuthMethodPickerLayout
                .Builder(R.layout.custom_layout)
                .setEmailButtonId(R.id.custom_layout_emailButton)
                .setGoogleButtonId(R.id.custom_layout_googleButton)
                .setFacebookButtonId(R.id.custom_layout_facebookButton)
                .setTwitterButtonId(R.id.custom_layout_twitterButton)
                .build();*/


        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setTheme(R.style.LoginTheme)
                        .setLogo(R.drawable.ic_logo)
                        //.setAuthMethodPickerLayout(customLayout)
                        .build(),
                RC_SIGN_IN);

    }

    private void configureSearchView() {
        SearchView searchView = findViewById(R.id.searchView);
        searchView.setLayoutParams(new Toolbar.LayoutParams(Gravity.END));

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbar.setTitle("");
                toolbar.setNavigationIcon(null);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                toast(R.string.connectActivity_toast_successful);
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                createUser(firebaseUser);
                displayUserinNavDrawer();

            } else {
                // Sign in failed. If response is null the firebaseUser canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
                toast(R.string.connectActivity_toast_failed);
            }
        }
    }

    private void displayUserinNavDrawer() {
        TextView viewName = findViewById(R.id.nav_header_name_and_surname_tv);
        TextView viewMail = findViewById(R.id.nav_header_mail_tv);
        ImageView imageView = findViewById(R.id.nav_header_iv);

        viewName.setText(user.name);
        viewMail.setText(user.mail);

        Glide.with(this)
                .load(user.photo)
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

    private void configureToolbar() {
        toolbar = findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("I'm Hungry !");
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

    private void disconnectUser() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        recreate();
                    }
                });
    }

    private void configureDrawerLayout() {
        drawerLayout = findViewById(R.id.activity_main_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void configureNavigationView() {
        NavigationView navigationView = findViewById(R.id.activity_main_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void createUser(FirebaseUser firebaseUser) {
        if (firebaseUser != null) {

            String name = firebaseUser.getDisplayName();
            String mail = firebaseUser.getEmail();
            Uri photoUrl = firebaseUser.getPhotoUrl();
            user = new User(name, mail, photoUrl);
        }
    }


    private void toast(int message) {
        Toast toast = Toast.makeText(this, getString(message), Toast.LENGTH_LONG);
        toast.show();
    }
}
