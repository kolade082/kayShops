package com.example.kayshops.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.kayshops.LoginActivity;
import com.example.kayshops.R;
import com.example.kayshops.user.UserActivity;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Retrieve fullName value from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String fullName = sharedPreferences.getString("fullName", "");


        Button addProductBtn = findViewById(R.id.addProductBtn);
        Button addCategoryBtn = findViewById(R.id.addCategoryBtn);
        Button manageProductBtn = findViewById(R.id.manageProductBtn);
        Button manageCategoryBtn = findViewById(R.id.manageCategoryBtn);
        Button manageUsersBtn = findViewById(R.id.manageUsersBtn);
        Button viewOrdersBtn = findViewById(R.id.viewOrdersBtn);
        Button logoutBtn = findViewById(R.id.logoutBtn);
        Button switchToUserActivityBtn = findViewById(R.id.switchToUserActivityBtn);

        addProductBtn.setOnClickListener(view -> {
            Intent intent = new Intent(AdminActivity.this, AddProductActivity.class);
            startActivity(intent);
        });

        addCategoryBtn.setOnClickListener(view -> {
            Intent intent = new Intent(AdminActivity.this, AddCategoryActivity.class);
            startActivity(intent);
        });

        manageProductBtn.setOnClickListener(view -> {
            Intent intent = new Intent(AdminActivity.this, ManageProductActivity.class);
            startActivity(intent);
        });

        viewOrdersBtn.setOnClickListener(view -> {
            Intent intent = new Intent(AdminActivity.this, ViewAllOrderActivity.class);
            startActivity(intent);
        });

        manageCategoryBtn.setOnClickListener(view -> {
            Intent intent = new Intent(AdminActivity.this, ManageCategoryActivity.class);
            startActivity(intent);
        });

        manageUsersBtn.setOnClickListener(view -> {
            Intent intent = new Intent(AdminActivity.this, ManageUserActivity.class);
            startActivity(intent);
        });


        logoutBtn.setOnClickListener(view -> {
            // Clear user data from SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            // Redirect user to the login screen
            Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });


        switchToUserActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, UserActivity.class);
                startActivity(intent);
            }
        });

    }
}