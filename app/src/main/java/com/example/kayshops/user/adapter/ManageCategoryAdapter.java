package com.example.kayshops.user.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kayshops.R;
import com.example.kayshops.user.model.Categories;

import java.util.List;

public class ManageCategoryAdapter extends RecyclerView.Adapter<ManageCategoryAdapter.CategoryViewHolder> {
    private final Context context;
    private final List<Categories> categoriesList;

    public interface OnCategoryItemClickListener {
        void onEditCategoryClick(Categories category);
        void onDeleteCategoryClick(Categories category);
    }

    private final OnCategoryItemClickListener clickListener;

    public ManageCategoryAdapter(Context context, List<Categories> categoriesList, OnCategoryItemClickListener clickListener) {
        this.context = context;
        this.categoriesList = categoriesList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.category_listing, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Categories category = categoriesList.get(position);
        holder.categoryName.setText(category.getCategoryName());

        holder.editCategory.setOnClickListener(v -> clickListener.onEditCategoryClick(category));
        holder.deleteCategory.setOnClickListener(v -> clickListener.onDeleteCategoryClick(category));
    }

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }

    public static final class CategoryViewHolder extends RecyclerView.ViewHolder{
        ImageView editCategory, deleteCategory;
        TextView categoryName;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryName = itemView.findViewById(R.id.category_name);
            editCategory = itemView.findViewById(R.id.edit_category);
            deleteCategory = itemView.findViewById(R.id.delete_category);
        }
    }
}
