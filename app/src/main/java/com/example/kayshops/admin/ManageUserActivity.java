package com.example.kayshops.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.kayshops.DbConnect;
import com.example.kayshops.R;
import com.example.kayshops.user.Users;
import com.example.kayshops.user.adapter.ManageCategoryAdapter;
import com.example.kayshops.user.adapter.ManageUserAdapter;
import com.example.kayshops.user.model.Categories;

import java.util.ArrayList;
import java.util.List;

public class ManageUserActivity extends AppCompatActivity {
    private final List<Users> users = new ArrayList<>();
    private ManageUserAdapter manageUserAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_user);

        RecyclerView recyclerView = findViewById(R.id.usersRecyclerView);

        //initializing adapter
        manageUserAdapter = new ManageUserAdapter(this, users, new ManageUserAdapter.OnUserItemClickListener() {
            @Override
            public void onEditUserClick(Users user) {
                Intent intent = new Intent(ManageUserActivity.this, UpdateUserActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }

            @Override
            public void onDeleteUserClick(Users user) {
                // category user click here
                DbConnect dbConnect = new DbConnect(ManageUserActivity.this);
                if (dbConnect.deleteUser(user)){
                    users.remove(user);
                    manageUserAdapter.notifyDataSetChanged();
                }else{
                    // Handle failure
                    Toast.makeText(ManageUserActivity.this, "Failed to delete user", Toast.LENGTH_SHORT).show();
                }
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(manageUserAdapter);

    }
    @Override
    protected void onResume() {
        super.onResume();

        DbConnect dbConnect = new DbConnect(this);

        // Clearing the existing list and fetching categories again
        users.clear();

        // producing the categories list
        users.addAll(dbConnect.getAllUsers());

        manageUserAdapter.notifyDataSetChanged();
    }
}