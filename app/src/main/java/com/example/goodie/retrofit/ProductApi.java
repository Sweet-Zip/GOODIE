package com.example.goodie.retrofit;

import com.example.goodie.model.Product;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ProductApi {
    @GET("/product")
    Call<List<Product>> getAllData();

    @Multipart
    @POST("/uploadFile")
    Call<Product> uploadFile(
            @Part MultipartBody.Part file,
            @Part("productName") RequestBody productName,
            @Part("price") RequestBody price,
            @Part("productDetail") RequestBody productDetail
    );

}
