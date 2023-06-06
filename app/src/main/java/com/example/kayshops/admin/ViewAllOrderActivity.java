package com.example.kayshops.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.kayshops.DbConnect;
import com.example.kayshops.R;
import com.example.kayshops.user.adapter.OrdersAdapter;
import com.example.kayshops.user.model.Orders;

import java.util.List;

public class ViewAllOrderActivity extends AppCompatActivity {

    private RecyclerView ordersRecyclerView;
    private OrdersAdapter ordersAdapter;
    private DbConnect dbConnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_order);

        ordersRecyclerView = findViewById(R.id.ordersRecyclerView);
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbConnect = new DbConnect(this);

        // Fetch all the orders from the database
        List<Orders> allOrders = dbConnect.getAllOrders();

        // Initialize the adapter and set it to the RecyclerView
        ordersAdapter = new OrdersAdapter(allOrders, true);
        ordersRecyclerView.setAdapter(ordersAdapter);
    }
}
