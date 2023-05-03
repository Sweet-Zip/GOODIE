package com.example.goodie.function;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.goodie.R;
import com.example.goodie.model.Product;
import com.example.goodie.retrofit.ProductApi;
import com.example.goodie.retrofit.RetrofitService;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GridAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private String[] title;
    private double[] price;
    private int[] imageId;
    private ImageView imageView;
    private TextView titleTextView, priceTextView;

    private Thread mUiThread;
    final Handler mHandler = new Handler();
    private List<Product> productList = new ArrayList<>();

    /*public GridAdapter(Context context, String[] title, double[] price, int[] imageId) {
        this.context = context;
        this.title = title;
        this.price = price;
        this.imageId = imageId;
    }*/

    public GridAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return productList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View gridViewItem = convertView;
        if (inflater == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.gridadapter, null);
        }
        if (gridViewItem == null) {
            gridViewItem = LayoutInflater.from(context).inflate(R.layout.gridadapter, null);
        }

        imageView = gridViewItem.findViewById(R.id.productImageView);
        titleTextView = gridViewItem.findViewById(R.id.titleTextView);
        priceTextView = gridViewItem.findViewById(R.id.priceTextView);
        Product product = productList.get(position);

        titleTextView.setText(product.getProductName());
        priceTextView.setText(String.valueOf(product.getPrice()));
        if (product.getFileCode().isEmpty()){
            imageView.setImageResource(R.drawable.logo);
        } else {
            fetchImage(product.getFileCode(), imageView);
        }
        gridViewItem.setTag(product.getId());
        return gridViewItem;
    }

    private void fetchImage(String fileCode, ImageView im) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://192.168.50.115:8080/downloadFile/" + fileCode)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                InputStream inputStream = response.body().byteStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                runOnUiThread(() -> {
                    // Set the bitmap to an ImageView or use it in any other way
                    im.setImageBitmap(bitmap);
                });
            }
        });
    }

    public final void runOnUiThread(Runnable action) {
        if (Thread.currentThread() != mUiThread) {
            mHandler.post(action);
        } else {
            action.run();
        }
    }

}
