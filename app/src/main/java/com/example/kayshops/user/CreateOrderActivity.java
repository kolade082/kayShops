package com.example.kayshops.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kayshops.DbConnect;
import com.example.kayshops.R;
import com.example.kayshops.user.adapter.OrderItemsAdapter;
import com.example.kayshops.user.model.Orders;
import com.example.kayshops.user.model.Products;
import com.example.kayshops.user.model.ShoppingCart;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CreateOrderActivity extends AppCompatActivity {
    private DbConnect dbConnect;

    RecyclerView orderItemsRecyclerView;
    OrderItemsAdapter orderItemsAdapter;
    TextView orderNumberTextView, orderStatusTextView, deliveryDateTextView, totalPriceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_order);

        ImageView orderImageView;
        orderNumberTextView = findViewById(R.id.orderNumberTextView);
        orderStatusTextView = findViewById(R.id.orderStatusTextView);
        deliveryDateTextView = findViewById(R.id.deliveryDateTextView);
        totalPriceTextView = findViewById(R.id.totalPriceTextView);

        orderItemsRecyclerView = findViewById(R.id.orderItemsRecyclerView);
        orderItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Products> cartItems = (List<Products>) getIntent().getSerializableExtra("cartItems");
        orderItemsAdapter = new OrderItemsAdapter(cartItems);
        orderItemsRecyclerView.setAdapter(orderItemsAdapter);

        // Initialize your DbConnect instance
        dbConnect = new DbConnect(this);

        double totalPrice = getIntent().getDoubleExtra(BagFragment.EXTRA_TOTAL_PRICE, 0.0);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");

        // Ensure you have a valid email
        if (email.equals("")) {
            // Handle the case when no email is found in shared preferences
            Log.e("CreateOrderActivity", "No email found in shared preferences");
            return;
        }

        // Get the user by email
        Users user = dbConnect.getUserByEmail(email);
        if (user != null) {
            int userId = user.getUserID();

            // Create a new Orders instance
            Orders order = new Orders("KAY" + System.currentTimeMillis(), "Pending", String.valueOf(totalPrice), userId);
            dbConnect.addOrder(order);

            // Clear the shopping cart
            ShoppingCart.getInstance().clearCart();

            orderNumberTextView.setText("Order Number: " + order.getOrderNumber());
            orderStatusTextView.setText("Order Status: " + order.getOrderStatus());
            deliveryDateTextView.setText("Delivery Date: " + order.getDeliveryDate());
            totalPriceTextView.setText("Total Price: £" + String.format("%.2f", totalPrice));
        } else {
            // Handle the case when no user is found for the given email
            Log.e("CreateOrderActivity", "No user found for the email: " + email);
            return;
        }
        Button continueShoppingBtn = findViewById(R.id.continueShoppingBtn);

        continueShoppingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShoppingCart.getInstance().clearCart();
                Intent intent = new Intent(CreateOrderActivity.this, UserActivity.class);
                intent.putExtra("loadHomeFragment", true);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CreateOrderActivity.this, UserActivity.class);
        intent.putExtra("loadHomeFragment", true);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }


}
