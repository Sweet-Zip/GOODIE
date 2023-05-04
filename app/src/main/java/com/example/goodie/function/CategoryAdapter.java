package com.example.goodie.function;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.goodie.R;
import com.example.goodie.model.Category;

import java.util.List;

public class CategoryAdapter extends ArrayAdapter<Category> {

    public CategoryAdapter(Context context, List<Category> categories) {
        super(context, 0, categories);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.spinner_item_category, parent, false);
        }

        TextView textView = convertView.findViewById(android.R.id.text1);
        Category category = getItem(position);
        if (category != null) {
            textView.setText(category.getName());
        }

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.spinner_item_category, parent, false);
        }

        TextView textView = convertView.findViewById(android.R.id.text1);
        Category category = getItem(position);
        if (category != null) {
            textView.setText(category.getName());
        }

        return convertView;
    }
}
