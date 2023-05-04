package com.example.goodie.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.goodie.R;
import com.example.goodie.model.Product;
import com.example.goodie.retrofit.RetrofitService;
import com.example.goodie.retrofit.ServerApi;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminDetailActivity extends AppCompatActivity {

    private TextView nameTextView, priceTextView, detailTextView;
    private ImageView imageView, backIcon, editIcon;
    private int id;
    private Bundle extras;
    private RetrofitService retrofitService;
    private ServerApi serverApi;
    private String fileCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_detail);
        nameTextView = findViewById(R.id.productName);
        priceTextView = findViewById(R.id.productPrice);
        detailTextView = findViewById(R.id.productDetail);
        backIcon = findViewById(R.id.addProductBackIcon);
        imageView = findViewById(R.id.productImage);
        editIcon = findViewById(R.id.editProductIcon);

        extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getInt("id");
        }

        loadProduct(nameTextView, priceTextView, detailTextView, imageView);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        editIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminDetailActivity.this, AddProductActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("name", nameTextView.getText().toString());
                intent.putExtra("price", priceTextView.getText().toString());
                intent.putExtra("detail", detailTextView.getText().toString());
                intent.putExtra("fileCode", fileCode);
                startActivity(intent);

            }
        });
    }

    private void loadProduct(TextView name, TextView price, TextView detail, ImageView imageView) {
        retrofitService = new RetrofitService();
        serverApi = retrofitService.getRetrofit().create(ServerApi.class);
        serverApi.getProductByID(id).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                Product product = (Product) response.body();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        name.setText(product.getProductName());
                        price.setText(String.valueOf(product.getPrice()));
                        detail.setText(product.getProductDetail());
                        fileCode = product.getFileCode();
                        fetchImage(fileCode, imageView);
                    }
                });
            }
            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Toast.makeText(AdminDetailActivity.this, "Can't load data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchImage(String fileCode, ImageView imageView) {
        RetrofitService retrofitService = new RetrofitService();
        ServerApi productApi = retrofitService.getRetrofit().create(ServerApi.class);
        Call<ResponseBody> call = productApi.downloadFile(fileCode);
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        byte[] bytes = response.body().bytes();
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        runOnUiThread(() -> {
                            imageView.setImageBitmap(bitmap);
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        throw new IOException("Unexpected code " + response);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}