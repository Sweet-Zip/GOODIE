package com.example.goodie.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.goodie.R;
import com.example.goodie.function.CategoryAdapter;
import com.example.goodie.model.Category;
import com.example.goodie.model.Product;
import com.example.goodie.retrofit.RetrofitService;
import com.example.goodie.retrofit.ServerApi;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProductActivity extends AppCompatActivity {

    private ImageView backIcon, imageView;
    private EditText productName, productPrice, productDetail;
    private Button addProductBtn, updateBtn, deleteBtn;
    private TextView textView;
    private Uri selectedImage;
    private Spinner addProductSpinner;
    private static final int PICK_IMAGE = 1;
    private static final int PERMISSION_CODE = 1001;
    private String selectedItem;
    private Bundle extras;
    private ServerApi serverApi;
    private RetrofitService retrofitService;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        updateBtn = findViewById(R.id.updateProductBtn);
        deleteBtn = findViewById(R.id.deleteProductBtn);
        textView = findViewById(R.id.titleToolBar);

        backIcon = findViewById(R.id.addProductBackIcon);
        imageView = findViewById(R.id.addProductImageView);
        addProductBtn = findViewById(R.id.addProductBtn);
        productName = findViewById(R.id.addProductNameEditText);
        productPrice = findViewById(R.id.addPriceEditText);
        productDetail = findViewById(R.id.addProductDetailEditText);
        addProductSpinner = findViewById(R.id.addCategorySpinner);

        extras = getIntent().getExtras();
        if (extras != null) {
            updateBtn.setVisibility(View.VISIBLE);
            deleteBtn.setVisibility(View.VISIBLE);
            addProductBtn.setVisibility(View.GONE);

            textView.setText("Update Product");
            id = extras.getInt("id");
            productName.setText(extras.getString("name"));
            productPrice.setText(extras.getString("price"));
            productDetail.setText(extras.getString("detail"));
            fetchImage(extras.getString("fileCode"), imageView);
            Toast.makeText(this, id + "", Toast.LENGTH_SHORT).show();
        }

        loadCategory(addProductSpinner);
        addProductSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view;
                selectedItem = textView.getText().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageFromGallery();
            }
        });
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (extras != null) {
                    finish();
                } else {
                    Intent intent = new Intent(AddProductActivity.this, AdminMainActivity.class);
                    startActivity(intent);
                }

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
                    Toast.makeText(AddProductActivity.this, selectedItem + "Please Select Image", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    addProduct(name, Double.parseDouble(price), detail, selectedItem, getPath(selectedImage));
                }
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
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
                    Drawable drawable = imageView.getDrawable();
                    if (drawable instanceof BitmapDrawable) {
                        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                        Bitmap bitmap = bitmapDrawable.getBitmap();
                        selectedImage = getImageUri(AddProductActivity.this, bitmap);
                    }
                } else {
                    updateProduct(id, getPath(selectedImage), name, detail, Double.parseDouble(price), selectedItem);
                }
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProduct(id);
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

    public static Uri getImageUri(Context context, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
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
    private String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s = cursor.getString(column_index);
        cursor.close();
        return s;
    }

    private void addProduct(String addProductName, double addPrice, String addProductDetail, String addCategory, String imagePath) {

        File imageFile = new File(imagePath);

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", imageFile.getName(), requestBody);
        RequestBody nameBody = RequestBody.create(MediaType.parse("text/plain"), addProductName);
        RequestBody productDetailBody = RequestBody.create(MediaType.parse("text/plain"), addProductDetail);
        RequestBody addCategoryBody = RequestBody.create(MediaType.parse("text/plain"), addCategory);
        RequestBody priceBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(addPrice));
        retrofitService = new RetrofitService();

        serverApi = retrofitService.getRetrofit().create(ServerApi.class);

        Call<Product> call = serverApi.uploadFile(filePart, nameBody, priceBody, productDetailBody, addCategoryBody);

        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AddProductActivity.this, "Product added successfully", Toast.LENGTH_SHORT).show();
                    productName.setText(null);
                    productPrice.setText(null);
                    productDetail.setText(null);
                    selectedImage.equals(null);
                    imageView.setImageResource(R.drawable.baseline_add_a_photo);
                } else {
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

    private void updateProduct(int id, String imagePath, String addProductName, String addProductDetail, double addPrice, String addCategory) {

        File imageFile = new File(imagePath);
        //RequestBody idBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(id));
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", imageFile.getName(), requestBody);
        RequestBody nameBody = RequestBody.create(MediaType.parse("text/plain"), addProductName);
        RequestBody productDetailBody = RequestBody.create(MediaType.parse("text/plain"), addProductDetail);
        RequestBody addCategoryBody = RequestBody.create(MediaType.parse("text/plain"), addCategory);
        RequestBody priceBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(addPrice));

        retrofitService = new RetrofitService();
        serverApi = retrofitService.getRetrofit().create(ServerApi.class);
        Call<Product> call = serverApi.updateProduct(id, filePart, nameBody, priceBody, productDetailBody, addCategoryBody);

        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AddProductActivity.this, "Product update successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddProductActivity.this, AdminDetailActivity.class);
                    intent.putExtra("id", id);
                    /*intent.putExtra("name", addProductName);
                    intent.putExtra("price", addPrice);
                    intent.putExtra("detail", addProductDetail);*/
                    startActivity(intent);
                    finish();
                } else {
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

    private void loadCategory(Spinner spinner) {
        retrofitService = new RetrofitService();
        serverApi = retrofitService.getRetrofit().create(ServerApi.class);
        serverApi.getAllCategory().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                List<Category> categories = response.body();
                CategoryAdapter adapter = new CategoryAdapter(AddProductActivity.this, categories);
                spinner.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Toast.makeText(AddProductActivity.this, "Failed to load category", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteProduct(int id){
        retrofitService = new RetrofitService();
        serverApi = retrofitService.getRetrofit().create(ServerApi.class);
        serverApi.deleteProduct(id).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                Toast.makeText(AddProductActivity.this, id + " Something when wrong! Failed to delete.", Toast.LENGTH_LONG).show();
                        /*Intent intent = new Intent(UpdateActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();*/
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Toast.makeText(AddProductActivity.this, "Product delete successful.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddProductActivity.this, AdminMainActivity.class);
                startActivity(intent);
                finish();
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