package com.vincler.jf.projet6.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.vincler.jf.projet6.PageAdapter;
import com.vincler.jf.projet6.R;
import com.vincler.jf.projet6.models.Users;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    private static final int RC_SIGN_IN = 123;
    private Users users;
    private BottomNavigationView bottomNavigationView;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        bottomNavigationView = findViewById(R.id.activity_main_bottom_nav_view);
        viewPager = findViewById(R.id.activity_main_viewpager);


        configureViews();
        firebaseUI();


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
        }
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

    private void createUser(FirebaseUser firebaseUser) {
        if (firebaseUser != null) {

            String name = firebaseUser.getDisplayName();
            String mail = firebaseUser.getEmail();
            Uri photoUrl = firebaseUser.getPhotoUrl();
            users = new Users(name, mail, photoUrl);
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
        TextView viewName = findViewById(R.id.nav_header_name_and_surname_tv);
        TextView viewMail = findViewById(R.id.nav_header_mail_tv);
        ImageView imageView = findViewById(R.id.nav_header_iv);

        viewName.setText(users.name);
        viewMail.setText(users.mail);

        Glide.with(this)
                .load(users.photo)
                .into(imageView);
    }

 /*   @Override
    public void onBackPressed() {
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }*/


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

