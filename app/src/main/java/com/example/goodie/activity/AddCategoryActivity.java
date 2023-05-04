package com.example.goodie.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.goodie.R;
import com.example.goodie.function.CategoryRecycleViewAdapter;
import com.example.goodie.model.Category;
import com.example.goodie.retrofit.ServerApi;
import com.example.goodie.retrofit.RetrofitService;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCategoryActivity extends AppCompatActivity {

    private Button addCategoryBtn, updateCategoryBtn, deleteCategoryBtn;
    private EditText categoryEditText;
    private ImageView backIcon;
    private static final int ADD_CATEGORY_REQUEST_CODE = 100;
    private Bundle extras;
    private String name;
    private TextView titleToolBar;
    private Category category;
    private RetrofitService retrofitService;
    private ServerApi productApi;
    private long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        addCategoryBtn = findViewById(R.id.addCategoryBtn);
        categoryEditText = findViewById(R.id.addCategoryEditText);
        backIcon = findViewById(R.id.addProductBackIcon);
        updateCategoryBtn = findViewById(R.id.updateCategoryBtn);
        deleteCategoryBtn = findViewById(R.id.deleteCategoryBtn);
        titleToolBar = findViewById(R.id.titleToolBar);
        category = new Category();
        extras = getIntent().getExtras();
        retrofitService = new RetrofitService();
        productApi = retrofitService.getRetrofit().create(ServerApi.class);

        if (extras != null) {
            id = extras.getInt("id");
            name = extras.getString("name");
            categoryEditText.setText(name);
            addCategoryBtn.setVisibility(View.GONE);
            updateCategoryBtn.setVisibility(View.VISIBLE);
            deleteCategoryBtn.setVisibility(View.VISIBLE);
            titleToolBar.setText("Update Category");
        }

        addCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = categoryEditText.getText().toString();
                addCategory(name);
            }
        });

        updateCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = categoryEditText.getText().toString();
                updateCategory(id, name);
            }
        });

        deleteCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCategoryByID(id);
            }
        });


        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("category_added", true);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    private void addCategory(String name) {

        if (name.isEmpty()) {
            categoryEditText.setError("Please Input Category Name");
            categoryEditText.requestFocus();
            return;
        }
        category.setName(name);
        productApi.addCategory(category).enqueue(new Callback<Category>() {
            @Override
            public void onResponse(Call<Category> call, Response<Category> response) {
                Toast.makeText(AddCategoryActivity.this, name + " Save Successful", Toast.LENGTH_LONG).show();
                loadCategories();
                categoryEditText.setText(null);
            }

            @Override
            public void onFailure(Call<Category> call, Throwable t) {
                Toast.makeText(AddCategoryActivity.this, "Save Failed", Toast.LENGTH_LONG).show();
                Logger.getLogger(AddCategoryActivity.class.getName()).log(Level.SEVERE, "Error occurred");
            }
        });
    }

    private void updateCategory(long id, String name) {
        category.setName(name);
        productApi.updateCategory(id, category).enqueue(new Callback<Category>() {
            @Override
            public void onResponse(Call<Category> call, Response<Category> response) {
                //Toast.makeText(AddCategoryActivity.this, "Update failed something is wrong please try again.", Toast.LENGTH_SHORT).show();
                if (response.isSuccessful()) {
                    Toast.makeText(AddCategoryActivity.this, "Category: " + name + " Update Successful.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddCategoryActivity.this, AdminMainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(AddCategoryActivity.this, "Update failed something is wrong please try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Category> call, Throwable t) {
                Toast.makeText(AddCategoryActivity.this, "Update failed something is wrong please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteCategoryByID(long id) {
        productApi.deleteCategoryByID(id).enqueue(new Callback<Category>() {
            @Override
            public void onResponse(Call<Category> call, Response<Category> response) {
                Toast.makeText(AddCategoryActivity.this, "Something went wrong! Failed to delete category " + id, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Category> call, Throwable t) {
                Toast.makeText(AddCategoryActivity.this, "Category " + id + " has been deleted.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(AddCategoryActivity.this, AdminMainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loadCategories() {
        RetrofitService retrofitService = new RetrofitService();
        ServerApi productApi = retrofitService.getRetrofit().create(ServerApi.class);
        productApi.getAllCategory().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                CategoryRecycleViewAdapter adapter = new CategoryRecycleViewAdapter(new ArrayList<Category>());
                adapter.setItems(response.body());
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
            }
        });
    }
}