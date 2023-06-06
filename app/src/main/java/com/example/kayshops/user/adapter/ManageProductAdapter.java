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

public class ManageProductAdapter extends RecyclerView.Adapter<ManageProductAdapter.ProductViewHolder> {
    Context context;
    List<Products> productsList;

    public interface OnProductItemClickListener {
        void onEditProductClick(Products product);
        void onDeleteProductClick(Products product);
    }
    private OnProductItemClickListener clickListener;
    public ManageProductAdapter(Context context, List<Products> productsList, OnProductItemClickListener clickListener) {
        this.context = context;
        this.productsList = productsList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_listing, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Products product = productsList.get(position);
        Glide.with(context)
                .load(product.getProductImage())
                .into(holder.productImage);

        holder.productName.setText(product.getProductName());
        holder.productSize.setText("Size"+product.getProductSize());
        holder.productPrice.setText("£"+product.getProductPrice());

        holder.editProduct.setOnClickListener(v -> clickListener.onEditProductClick(product));
        holder.deleteProduct.setOnClickListener(v -> clickListener.onDeleteProductClick(product));
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public static final class ProductViewHolder extends RecyclerView.ViewHolder{
        ImageView productImage, editProduct, deleteProduct;
        TextView productName, productSize, productPrice;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productSize = itemView.findViewById(R.id.productSize);
            productPrice = itemView.findViewById(R.id.productPrice);
            editProduct = itemView.findViewById(R.id.edit_product);
            deleteProduct = itemView.findViewById(R.id.delete_product);
        }
    }
}
