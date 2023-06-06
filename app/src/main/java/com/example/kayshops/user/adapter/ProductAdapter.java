package com.example.kayshops.user.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kayshops.R;
import com.example.kayshops.user.model.Products;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    Context context;
    List<Products> productsList;

    public interface OnProductItemClickListener {
        void onProductItemClick(Products product);
    }
    private OnProductItemClickListener clickListener;
    public ProductAdapter(Context context, List<Products> productsList, OnProductItemClickListener clickListener) {
        this.context = context;
        this.productsList = productsList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_display, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Products products = productsList.get(position);
        Glide.with(context)
                .load(productsList.get(position).getProductImage())
                .into(holder.productImage);

        holder.productName.setText(productsList.get(position).getProductName());
        holder.productQuantity.setText(productsList.get(position).getProductSize());
        holder.productPrice.setText("£"+productsList.get(position).getProductPrice());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onProductItemClick(products);
            }
        });
    }


    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public static final class ProductViewHolder extends RecyclerView.ViewHolder{
        ImageView productImage;
        TextView productName, productQuantity, productPrice;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productQuantity = itemView.findViewById(R.id.productSize);
            productPrice = itemView.findViewById(R.id.productPrice);
        }
    }
    // Update the displayed product list
    public void updateProductList(List<Products> newProductList) {
        this.productsList = newProductList;
        notifyDataSetChanged();
    }
}
