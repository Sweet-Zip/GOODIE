package com.example.goodie.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.goodie.R;
import com.example.goodie.fragment.AdminHomeFragment;
import com.example.goodie.fragment.CategoryFragment;
import com.example.goodie.fragment.ProfileFragment;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AdminMainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    BadgeDrawable badgeDrawable;
    Toolbar toolbar;
    TextView titleToolBar;
    ImageView imageViewToolBar;
    FrameLayout mainFragment;
    private static final int STORAGE_PERMISSION_CODE = 101;
    private static final int CAMERA_PERMISSION_CODE = 100;
    private FloatingActionButton floatingBtnAddProduct, floatingBtnAddCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);

        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);
        titleToolBar = findViewById(R.id.titleToolBar);
        toolbar = findViewById(R.id.toolbar);
        imageViewToolBar = findViewById(R.id.logoToolBar);
        mainFragment = findViewById(R.id.adminMainFragment);
        floatingBtnAddProduct = findViewById(R.id.floatingBtnAddProduct);
        floatingBtnAddCategory = findViewById(R.id.floatingBtnAddCategory);
        setSupportActionBar(toolbar);
        bottomNavigationView = findViewById(R.id.adminBottomNavigationView);
        getSupportFragmentManager().beginTransaction().replace(R.id.adminMainFragment, new AdminHomeFragment()).commit();
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.adminHome:
                    titleToolBar.setText("GOODIE");
                    params.addRule(RelativeLayout.END_OF, R.id.logoToolBar);
                    params.addRule(RelativeLayout.ALIGN_PARENT_END);
                    params.removeRule(RelativeLayout.ALIGN_PARENT_START);
                    titleToolBar.setLayoutParams(params);
                    titleToolBar.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
                    imageViewToolBar.setVisibility(View.VISIBLE);
                    floatingBtnAddProduct.setVisibility(View.VISIBLE);
                    floatingBtnAddCategory.setVisibility(View.GONE);
                    replaceFragment(new AdminHomeFragment());
                    break;
                case R.id.categories:
                    titleToolBar.setText("Category");
                    params.removeRule(RelativeLayout.END_OF);
                    params.addRule(RelativeLayout.ALIGN_PARENT_START);
                    params.addRule(RelativeLayout.ALIGN_PARENT_END);
                    titleToolBar.setLayoutParams(params);
                    titleToolBar.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
                    imageViewToolBar.setVisibility(View.INVISIBLE);
                    floatingBtnAddProduct.setVisibility(View.GONE);
                    floatingBtnAddCategory.setVisibility(View.VISIBLE);
                    replaceFragment(new CategoryFragment());
                    break;
                case R.id.adminProfile:
                    titleToolBar.setText("Profile");
                    params.removeRule(RelativeLayout.END_OF);
                    params.addRule(RelativeLayout.ALIGN_PARENT_START);
                    params.addRule(RelativeLayout.ALIGN_PARENT_END);
                    titleToolBar.setLayoutParams(params);
                    titleToolBar.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
                    imageViewToolBar.setVisibility(View.INVISIBLE);
                    floatingBtnAddProduct.setVisibility(View.GONE);
                    floatingBtnAddCategory.setVisibility(View.GONE);
                    replaceFragment(new ProfileFragment());
                    break;
            }
            return true;
        });
        floatingBtnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMainActivity.this, AddProductActivity.class);
                startActivity(intent);
            }
        });
        floatingBtnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMainActivity.this, AddCategoryActivity.class);
                startActivity(intent);
            }
        });
    }

    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(AdminMainActivity.this, permission) == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(AdminMainActivity.this, new String[]{permission}, requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,
                permissions,
                grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(AdminMainActivity.this, "Camera Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(AdminMainActivity.this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(AdminMainActivity.this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(AdminMainActivity.this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.adminMainFragment, fragment);
        fragmentTransaction.commit();
    }
}
