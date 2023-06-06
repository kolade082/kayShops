package com.example.kayshops.user.adapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kayshops.R;
import com.example.kayshops.user.model.Products;

import java.util.List;

public class OrderItemsAdapter extends RecyclerView.Adapter<OrderItemsAdapter.ViewHolder> {

    private List<Products> items;

    public OrderItemsAdapter(List<Products> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Products product = items.get(position);
        holder.productNameTextView.setText(product.getProductName());
        holder.productSizeTextView.setText(product.getProductSize());
        holder.productPriceTextView.setText(product.getProductPrice());
        holder.productQuantityTextView.setText("x" + product.getProductQuantity());
        Glide.with(holder.productImageView.getContext())
                .load(product.getProductImage())
                .placeholder(R.drawable.add_image)
                .into(holder.productImageView);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImageView;
        TextView productNameTextView, productSizeTextView, productPriceTextView, productQuantityTextView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImageView = itemView.findViewById(R.id.productImageView);
            productNameTextView = itemView.findViewById(R.id.productNameTextView);
            productSizeTextView = itemView.findViewById(R.id.productSizeTextView);
            productPriceTextView = itemView.findViewById(R.id.productPriceTextView);
            productQuantityTextView = itemView.findViewById(R.id.productQuantityTextView);
        }
    }
}
