package com.example.goodie.function;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.goodie.R;

public class GridAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private String[] title;
    private String[] price;
    private int[] imageId;
    private ImageView imageView;
    TextView titleTextView, priceTextView;
    public GridAdapter(Context context, String[] title, String[] price, int[] imageId) {
        this.context = context;
        this.title = title;
        this.price = price;
        this.imageId = imageId;
    }

    @Override
    public int getCount() {
        return title.length;
    }

    @Override
    public Object getItem(int position) {
        return title[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /*if (inflater == null){
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }*/
        if (convertView == null){
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.gridadapter, null);
        }

        imageView = convertView.findViewById(R.id.productImageView);
        titleTextView = convertView.findViewById(R.id.titleTextView);
        priceTextView = convertView.findViewById(R.id.priceTextView);

        imageView.setImageResource(imageId[position]);
        titleTextView.setText(title[position]);
        priceTextView.setText(price[position]);

        return convertView;
    }
}
