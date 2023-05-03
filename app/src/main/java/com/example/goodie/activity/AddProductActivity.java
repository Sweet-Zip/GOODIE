package com.example.goodie.activity;

import static java.sql.Types.NULL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.goodie.R;
import com.example.goodie.model.Product;
import com.example.goodie.retrofit.ProductApi;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddProductActivity extends AppCompatActivity {

    private ImageView backIcon, imageView;
    private EditText productName, productPrice, productDetail;
    private Button addProductBtn;
    private Uri selectedImage;
    private static final int PICK_IMAGE = 1;
    private static final int PERMISSION_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        backIcon = findViewById(R.id.addProductBackIcon);
        imageView = findViewById(R.id.addProductImageView);
        addProductBtn = findViewById(R.id.addProductBtn);
        productName = findViewById(R.id.addProductNameEditText);
        productPrice = findViewById(R.id.addPriceEditText);
        productDetail = findViewById(R.id.addProductDetailEditText);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageFromGallery();
            }
        });
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddProductActivity.this, AdminMainActivity.class);
                startActivity(intent);
            }
        });
        addProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = productName.getText().toString();
                String price = productPrice.getText().toString();
                String detail = productDetail.getText().toString();
                if (name.isEmpty()) {
                    productName.setError("Please Input Product Name");
                    productName.requestFocus();
                } else if (price.isEmpty()) {
                    productPrice.setError("Please Input Product Price");
                    productPrice.requestFocus();
                } else if (selectedImage == null) {
                    Toast.makeText(AddProductActivity.this, "Please Select Image", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    addProduct(name, Double.parseDouble(price), detail, getPath(selectedImage));
                }
            }
        });
    }

    //Select Check Permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery();
                } else {
                    Toast.makeText(this, "Permission denied! Please allow storage permission.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && data != null && data.getData() != null) {
            selectedImage = data.getData();
            System.out.println(data.getData() + "===========================");
            imageView.setImageURI(data.getData());
        }
    }

    //Select Image
    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE);
    }

    //Convert Image to File
    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s = cursor.getString(column_index);
        cursor.close();
        return s;
    }

    // create a method to add a new product using Retrofit
    private void addProduct(String productName, double price, String productDetail, String imagePath) {

        File imageFile = new File(imagePath);

        // create a RequestBody instance from file
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);

        // create a MultipartBody.Part instance from RequestBody and file name
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", imageFile.getName(), requestBody);

        // create a RequestBody instance from the product name
        RequestBody nameBody = RequestBody.create(MediaType.parse("text/plain"), productName);
        RequestBody productDetailBody = RequestBody.create(MediaType.parse("text/plain"), productDetail);

        // create a RequestBody instance from the product price
        RequestBody priceBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(price));

        // create an instance of Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.50.115:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // create an instance of the API interface
        ProductApi productApi = retrofit.create(ProductApi.class);

        // make a request to add a new product
        Call<Product> call = productApi.uploadFile(filePart, nameBody, priceBody, productDetailBody);
        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful()) {
                    // product added successfully
                    Toast.makeText(AddProductActivity.this, "Product added successfully", Toast.LENGTH_SHORT).show();
                } else {
                    // request failed
                    Toast.makeText(AddProductActivity.this, "Request failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                // request failed
                Toast.makeText(AddProductActivity.this, "Request failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}