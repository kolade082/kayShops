package com.example.kayshops.user.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kayshops.DbConnect;
import com.example.kayshops.user.OnProductAddedToCartListener;
import com.example.kayshops.R;
import com.example.kayshops.user.model.Products;

import java.io.File;
import java.util.List;

public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.ViewHolder> {
    private List<Products> cartItems;
    private OnProductAddedToCartListener listener;
    private DbConnect dbConnect;

    public interface OnQuantityChangedListener {
        void onQuantityChanged();
    }
    private OnQuantityChangedListener quantityChangedListener;

    public void setQuantityChangedListener(OnQuantityChangedListener listener) {
        quantityChangedListener = listener;
    }
    public interface OnDeleteItemClickListener {
        void onDeleteItemClick(int position);
    }
    private OnDeleteItemClickListener onDeleteItemClickListener;

    public void setOnDeleteItemClickListener(OnDeleteItemClickListener onDeleteItemClickListener) {
        this.onDeleteItemClickListener = onDeleteItemClickListener;
    }

    public ShoppingCartAdapter(List<Products> cartItems, OnProductAddedToCartListener listener, DbConnect dbConnect) {
        this.cartItems = cartItems;
        this.listener = listener;
        this.dbConnect = dbConnect;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_cart_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Products product = cartItems.get(position);
        Glide.with(holder.productImage.getContext())
                .load(new File(product.getProductImage()))
                .into(holder.productImage);

        holder.productName.setText(product.getProductName());
        String categoryName = dbConnect.getCategoryNameById(product.getCategoryId());
        holder.categoryName.setText(categoryName);
        holder.productPrice.setText("£"+product.getProductPrice());
        holder.productSize.setText("Size: "+product.getProductSize());
        holder.productQuantity.setText(String.valueOf(product.getProductQuantity()));

        holder.increaseQuantityButton.setOnClickListener(v -> {
            int currentQuantity = product.getProductQuantity();
            product.setProductQuantity(currentQuantity + 1);
            holder.productQuantity.setText(String.valueOf(product.getProductQuantity()));
            listener.onProductAddedToCart();
            if (quantityChangedListener != null) {
                quantityChangedListener.onQuantityChanged();
            }
        });

        holder.decreaseQuantityButton.setOnClickListener(v -> {
            int currentQuantity = product.getProductQuantity();
            if (currentQuantity > 1) {
                product.setProductQuantity(currentQuantity - 1);
                holder.productQuantity.setText(String.valueOf(product.getProductQuantity()));
                listener.onProductAddedToCart();
                if (quantityChangedListener != null) {
                    quantityChangedListener.onQuantityChanged();
                }
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDeleteItemClickListener != null) {
                    int currentPosition = holder.getAdapterPosition();
                    onDeleteItemClickListener.onDeleteItemClick(currentPosition);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice, productQuantity, categoryName, productSize;
        ImageView productImage;
        Button decreaseQuantityButton, increaseQuantityButton, deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.productName);
            categoryName = itemView.findViewById(R.id.categoryName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productSize = itemView.findViewById(R.id.productSize);
            productImage = itemView.findViewById(R.id.productImage);
            productQuantity = itemView.findViewById(R.id.productQuantity);
            decreaseQuantityButton = itemView.findViewById(R.id.decreaseQuantityButton);
            increaseQuantityButton = itemView.findViewById(R.id.increaseQuantityButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);

            // visibility of the deleteButton to GONE
            deleteButton.setVisibility(View.GONE);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (deleteButton.getVisibility() == View.VISIBLE) {
                        deleteButton.setVisibility(View.GONE);
                    } else {
                        deleteButton.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }

    public void updateData(List<Products> newCartItems) {
        cartItems = newCartItems;
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        cartItems.remove(position);
        notifyItemRemoved(position);
    }


}
