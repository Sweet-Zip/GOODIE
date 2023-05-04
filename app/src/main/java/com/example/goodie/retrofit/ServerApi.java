package com.example.goodie.retrofit;

import com.example.goodie.model.Category;
import com.example.goodie.model.Product;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

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

    //@Streaming
    @GET("/downloadFile/{fileCode}")
    Call<ResponseBody> downloadFile(@Path("fileCode") String fileCode);

    @POST("/category/add")
    Call<Category> addCategory(@Body Category category);

    @GET("/category/get-all")
    Call<List<Category>> getAllCategory();

    @PUT("/category/update/{id}")
    Call<Category> updateCategory(@Path("id") long id, @Body Category category);

    @DELETE("/category/delete/{id}")
    Call<Category> deleteCategoryByID(@Path("id") long id);
}
