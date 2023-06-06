package com.example.kayshops.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.kayshops.DbConnect;
import com.example.kayshops.R;
import com.example.kayshops.user.adapter.OrdersAdapter;
import com.example.kayshops.user.model.Orders;

import java.util.List;

public class ViewUserOrdersActivity extends AppCompatActivity {

    private RecyclerView ordersRecyclerView;
    private OrdersAdapter ordersAdapter;
    private DbConnect dbConnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_orders);

        ordersRecyclerView = findViewById(R.id.ordersRecyclerView);
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbConnect = new DbConnect(this);

        // Retrieve the email from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");

        // Get the user by email
        Users user = dbConnect.getUserByEmail(email);
        Log.d("ViewUserOrdersActivity", "Retrieved email: " + email);
        if (user != null) {
            int userId = user.getUserID();

            // Fetch the user's orders from the database
            List<Orders> userOrders = dbConnect.getUserOrders(userId);

            // Initialize the adapter and set it to the RecyclerView
            ordersAdapter = new OrdersAdapter(userOrders, false);
            ordersRecyclerView.setAdapter(ordersAdapter);
        } else {
            // Handle the case when no user is found for the given email
            Log.e("ViewUserOrdersActivity", "No user found for the email: " + email);
            return;
        }
    }
}
