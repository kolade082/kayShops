package com.example.kayshops.user.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kayshops.R;
import com.example.kayshops.user.model.Orders;
import com.example.kayshops.user.model.Products;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {

    private List<Orders> ordersList;
    private boolean isAdminContext;

    public OrdersAdapter(List<Orders> ordersList, boolean isAdminContext) {
        this.ordersList = ordersList;
        this.isAdminContext = isAdminContext;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Orders order = ordersList.get(position);
        holder.orderTotalPrice.setText("Total Price: " + order.getOrderTotalPrice());
        holder.orderStatus.setText("Status: " + order.getOrderStatus());
        holder.orderNumber.setText("Order Number: " + order.getOrderNumber());

        // If the adapter is being used in an admin context, set the text color to white
        if (isAdminContext) {
            holder.orderTotalPrice.setTextColor(Color.WHITE);
            holder.orderStatus.setTextColor(Color.WHITE);
            holder.orderNumber.setTextColor(Color.WHITE);
        }
    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        ImageView orderItemImage;
        TextView orderTotalPrice, orderNumber;
        TextView orderStatus;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderItemImage = itemView.findViewById(R.id.orderItemImage);
            orderTotalPrice = itemView.findViewById(R.id.orderItemTotalPrice);
            orderStatus = itemView.findViewById(R.id.orderItemStatus);
            orderNumber = itemView.findViewById(R.id.orderNumber);
        }
    }
}
