package com.example.kayshops.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.kayshops.R;
import com.example.kayshops.DbConnect;
import com.example.kayshops.user.adapter.ManageCategoryAdapter;
import com.example.kayshops.user.model.Categories;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ManageCategoryActivity extends AppCompatActivity {

    private final List<Categories> categories = new ArrayList<>();
    private ManageCategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_category);

        FloatingActionButton addCategoryButton = findViewById(R.id.addCategoryButton);
        RecyclerView recyclerView = findViewById(R.id.categoriesRecyclerView);

        // Initializing the adapter
        adapter = new ManageCategoryAdapter(this, categories, new ManageCategoryAdapter.OnCategoryItemClickListener() {
            @Override
            public void onEditCategoryClick(Categories category) {
                // category edit click here
                Intent intent = new Intent(ManageCategoryActivity.this, UpdateCategoryActivity.class);
                intent.putExtra("category", category);
                startActivity(intent);
            }

            @Override
            public void onDeleteCategoryClick(Categories category) {
                // category delete click here
                DbConnect dbConnect = new DbConnect(ManageCategoryActivity.this);
                if (dbConnect.deleteCategory(category)){
                    categories.remove(category);
                    adapter.notifyDataSetChanged();
                }else{
                    // Handle failure
                    Toast.makeText(ManageCategoryActivity.this, "Failed to delete category", Toast.LENGTH_SHORT).show();
                }

            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        addCategoryButton.setOnClickListener(view -> {
            Intent intent = new Intent(ManageCategoryActivity.this, AddCategoryActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        DbConnect dbConnect = new DbConnect(this);

        // Clearing the existing list and fetching categories again
        categories.clear();

        // producing the categories list
        categories.addAll(dbConnect.getAllCategories());

        adapter.notifyDataSetChanged();
    }
}
