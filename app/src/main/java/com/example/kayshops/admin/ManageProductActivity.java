package com.example.kayshops.admin;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.kayshops.DbConnect;
import com.example.kayshops.R;
import com.example.kayshops.user.adapter.ManageProductAdapter;
import com.example.kayshops.user.model.Products;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ManageProductActivity extends AppCompatActivity {

    private List<Products> productList = new ArrayList<>();
    private ManageProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_product);

        FloatingActionButton addProductButton = findViewById(R.id.addProductButton);
        RecyclerView productRecyclerView = findViewById(R.id.productsRecyclerView);

        // Initializing the adapter
        adapter = new ManageProductAdapter(this, productList, new ManageProductAdapter.OnProductItemClickListener() {
            @Override
            public void onEditProductClick(Products product) {
                // product edit click here
                Log.d(TAG, "onEditProductClick: " + product.toString());
                Intent intent = new Intent(ManageProductActivity.this, UpdateProductActivity.class);
                intent.putExtra("product", product);
                startActivity(intent);
            }

            @Override
            public void onDeleteProductClick(Products product) {
                // product delete click here
                DbConnect dbConnect = new DbConnect(ManageProductActivity.this);
                if (dbConnect.deleteProduct(product)){
                    productList.remove(product);
                    adapter.notifyDataSetChanged();
                }else{
                    // Handle failure
                    Toast.makeText(ManageProductActivity.this, "Failed to delete product", Toast.LENGTH_SHORT).show();
                }

            }
        });

        // Set your adapter to the RecyclerView
        productRecyclerView.setAdapter(adapter);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        addProductButton.setOnClickListener(view -> {
            Intent intent = new Intent(ManageProductActivity.this, AddProductActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        DbConnect dbConnect = new DbConnect(this);

        Log.d(TAG, "onResume: productList = " + productList.toString()); //

        // Clearing the existing list and fetching products again
        productList.clear();
        productList.addAll(dbConnect.getAllProducts());

        // Notify the adapter that the data has changed
        adapter.notifyDataSetChanged();
    }
}
