package com.example.goodie.function;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.goodie.R;
import com.example.goodie.activity.AdminDetailActivity;
import com.example.goodie.model.Category;
import com.example.goodie.model.Product;
import com.example.goodie.retrofit.ServerApi;
import com.example.goodie.retrofit.RetrofitService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;


public class GridAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ImageView imageView;
    private TextView titleTextView, priceTextView;

    private Thread mUiThread;
    final Handler mHandler = new Handler();
    private List<Product> productList;
    private List<Product> filteredProducts;


    public GridAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
        Log.e(TAG, "GridAdapter: " + productList);
        this.filteredProducts = new ArrayList<>(productList);
    }

    @Override
    public int getCount() {
        return filteredProducts.size();
    }

    @Override
    public Object getItem(int position) {
        return filteredProducts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return filteredProducts.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View gridViewItem = convertView;
        if (inflater == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.grid_adapter, null);
        }
        if (gridViewItem == null) {
            gridViewItem = LayoutInflater.from(context).inflate(R.layout.grid_adapter, null);
        }
        imageView = gridViewItem.findViewById(R.id.productImageView);
        titleTextView = gridViewItem.findViewById(R.id.titleTextView);
        priceTextView = gridViewItem.findViewById(R.id.priceTextView);
        Product product = filteredProducts.get(position);

        titleTextView.setText(product.getProductName());
        priceTextView.setText(String.valueOf(product.getPrice()));
        if (product.getFileCode().isEmpty()) {
            imageView.setImageResource(R.drawable.logo);
        } else {
            fetchImage(product.getFileCode(), imageView);
        }
        gridViewItem.setTag(product.getId());
        return gridViewItem;
    }

    private void fetchImage(String fileCode, ImageView im) {
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
                            im.setImageBitmap(bitmap);
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

    public void filter(String query) {
        filteredProducts.clear();
        if (TextUtils.isEmpty(query)) {
            filteredProducts.addAll(productList);
        } else {
            String filterPattern = query.toLowerCase().trim();
            for (Product product : productList) {
                if (product.getProductName().toLowerCase().contains(filterPattern)) {
                    filteredProducts.add(product);
                }
            }
        }
        notifyDataSetChanged();
    }

    public final void runOnUiThread(Runnable action) {
        if (Thread.currentThread() != mUiThread) {
            mHandler.post(action);
        } else {
            action.run();
        }
    }
}
