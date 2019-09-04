package com.vincler.jf.projet6;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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

import com.bumptech.glide.Glide;
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

    private static final int RC_SIGN_IN = 123;
    private User user;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private EditText customEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.activity_main_toolbar);
        customEditText = findViewById(R.id.activity_main_customEditText);

        String text = customEditText.getText().toString();


        setSupportActionBar(toolbar);
        displayToolbar();
        DrawerLayout();
        NavigationView();
        FirebaseUI();
    }

    private void FirebaseUI() {
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
                displayUserInNavDrawer();

            } else {
                toast(R.string.connectActivity_toast_failed);
            }
        }
    }

    private void displayUserInNavDrawer() {
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

    private void displayToolbar() {

        noDisplayEditText();
        displayTitle();
        searchButtonListener();
    }

    private void searchButtonListener() {
        final ImageButton searchButton = findViewById(R.id.activity_main_searchButton_imButton);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noDisplayTitle();
                noDisplaySearchButton(searchButton);
                noDisplayNavigationIcon();
                displayEditText();
            }
        });
    }

    private void displayEditText() {
        customEditText.setVisibility(View.VISIBLE);
    }


    private void noDisplayEditText() {

        customEditText.setVisibility(View.GONE);
    }

    private void noDisplayNavigationIcon() {
        toolbar.setNavigationIcon(null);
    }

    private void noDisplaySearchButton(ImageButton searchButton) {
        searchButton.setVisibility(View.INVISIBLE);
    }


    private void displayTitle() {
        getSupportActionBar().setTitle("    " + getString(R.string.title_hungry));
    }

    private void noDisplayTitle() {
        getSupportActionBar().setTitle("");
    }

    private void editTextListener() {
        customEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = customEditText.getText().toString();
                Log.i("tag_ontextchange", text);
            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = customEditText.getText().toString();
                Log.i("tag_aftertextchange", text);
            }
        });

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


    private void DrawerLayout() {
        drawerLayout = findViewById(R.id.activity_main_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void NavigationView() {
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

    private void disconnectUser() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        recreate();
                    }
                });
    }

    private void toast(int message) {
        Toast toast = Toast.makeText(this, getString(message), Toast.LENGTH_LONG);
        toast.show();
    }
}