package com.example.goodie.function;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.goodie.R;
import com.example.goodie.activity.AddCategoryActivity;
import com.example.goodie.model.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryRecycleViewAdapter extends RecyclerView.Adapter<CategoryRecycleViewAdapter.ViewHolder> {
    private List<Category> categoryList;
    private List<Category> filteredList;

    public CategoryRecycleViewAdapter(List<Category> categoryList) {
        this.categoryList = categoryList;
        this.filteredList = new ArrayList<>(categoryList);
    }

    public void setItems(List<Category> categoryList) {
        this.categoryList = categoryList;
        this.filteredList = new ArrayList<>(categoryList);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public CategoryRecycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryRecycleViewAdapter.ViewHolder holder, int position) {
        Category category = filteredList.get(position);
        holder.categoryNameTextView.setText(category.getName());
        int id = Math.toIntExact(category.getId());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Item id: " + id + ", Name: " + category.getName() + " is clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(view.getContext(), AddCategoryActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("name", category.getName());
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public Category getItem(int position) {
        return categoryList.get(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView categoryNameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryNameTextView = itemView.findViewById(R.id.categoryList);
        }
    }

    public void filter(String query) {
        filteredList.clear();
        if (TextUtils.isEmpty(query)) {
            filteredList.addAll(categoryList);
        } else {
            String filterPattern = query.toLowerCase().trim();
            for (Category category : categoryList) {
                if (category.getName().toLowerCase().contains(filterPattern)) {
                    filteredList.add(category);
                }
            }
        }
        notifyDataSetChanged();
    }
}
