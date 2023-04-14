package com.example.goodie.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.goodie.R;
import com.example.goodie.fragment.CartFragment;
import com.example.goodie.fragment.HomeFragment;
import com.example.goodie.fragment.ProfileFragment;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    boolean doubleBackToExitPressedOnce = false;

    BottomNavigationView bottomNavigationView;
    BadgeDrawable badgeDrawable;
    Toolbar toolbar;
    TextView titleToolBar;
    ImageView imageViewToolBar;
    SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);

        titleToolBar = findViewById(R.id.titleToolBar);
        toolbar = findViewById(R.id.toolbar);
        imageViewToolBar = findViewById(R.id.logoToolBar);
        setSupportActionBar(toolbar);
        searchView = findViewById(R.id.searchView);
        searchView.setQueryHint("Search...");
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment, new HomeFragment()).commit();
        badgeDrawable = bottomNavigationView.getOrCreateBadge(R.id.cart);
        badgeDrawable.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
        badgeDrawable.setVisible(true);
        badgeDrawable.setNumber(7);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    titleToolBar.setText("GOODIE");
                    params.addRule(RelativeLayout.END_OF, R.id.logoToolBar);
                    params.addRule(RelativeLayout.ALIGN_PARENT_END);
                    params.removeRule(RelativeLayout.ALIGN_PARENT_START);
                    titleToolBar.setLayoutParams(params);
                    titleToolBar.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
                    imageViewToolBar.setVisibility(View.VISIBLE);
                    searchView.setVisibility(View.VISIBLE);
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.cart:
                    titleToolBar.setText("Cart");
                    params.removeRule(RelativeLayout.END_OF);
                    params.addRule(RelativeLayout.ALIGN_PARENT_START);
                    params.addRule(RelativeLayout.ALIGN_PARENT_END);
                    titleToolBar.setLayoutParams(params);
                    titleToolBar.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
                    imageViewToolBar.setVisibility(View.INVISIBLE);
                    searchView.setVisibility(View.INVISIBLE);
                    replaceFragment(new CartFragment());
                    break;
                case R.id.profile:
                    titleToolBar.setText("Profile");
                    params.removeRule(RelativeLayout.END_OF);
                    params.addRule(RelativeLayout.ALIGN_PARENT_START);
                    params.addRule(RelativeLayout.ALIGN_PARENT_END);
                    titleToolBar.setLayoutParams(params);
                    titleToolBar.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
                    imageViewToolBar.setVisibility(View.INVISIBLE);
                    searchView.setVisibility(View.INVISIBLE);
                    replaceFragment(new ProfileFragment());
                    break;
            }
            return true;
        });
    }

    @Override
    public void onBackPressed() {
        /*if (doubleBackToExitPressedOnce) {
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                        homeIntent.addCategory( Intent.CATEGORY_HOME );
                        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);*/

        new AlertDialog.Builder(this)
                .setTitle("Exit GOODIE")
                .setMessage("Do you want to exit GOODIE?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                        homeIntent.addCategory(Intent.CATEGORY_HOME);
                        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mainFragment, fragment);
        fragmentTransaction.commit();
    }
}