package com.example.kayshops.user.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kayshops.R;
import com.example.kayshops.user.model.ProductFilter;

import java.util.List;

public class ProductFilterAdapter extends RecyclerView.Adapter<ProductFilterAdapter.ProductViewHolder> {
    Context context;
    List<ProductFilter> productFilterList;
    private int selectedCategoryPosition = 0;

    public ProductFilterAdapter(Context context, List<ProductFilter> productFilterList) {
        this.context = context;
        this.productFilterList = productFilterList;
        Log.d("ProductFilterAdapter", "Constructor called, list size: " + productFilterList.size());
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("ProductFilterAdapter", "onCreateViewHolder called");
        // create a recyclerview row item layout file
        View view = LayoutInflater.from(context).inflate(R.layout.category_row_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Log.d("ProductFilterAdapter", "onBindViewHolder called, position: " + position);
        holder.catName.setText(productFilterList.get(position).getProductName());
    }

    @Override
    public int getItemCount() {
        return productFilterList.size();
    }

    public static final class ProductViewHolder extends RecyclerView.ViewHolder{
        TextView catName;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            catName = itemView.findViewById(R.id.catName);
        }
    }
}
