package com.example.kayshops.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kayshops.DbConnect;
import com.example.kayshops.R;
import com.example.kayshops.user.model.Categories;

public class UpdateCategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_category);

        Categories category = (Categories) getIntent().getSerializableExtra("category");

        EditText categoryNameEditText = findViewById(R.id.categoryNameEditText);
        Button updateCategoryButton = findViewById(R.id.update_category_btn);

        if (category != null) {
            categoryNameEditText.setText(category.getCategoryName());
        }

        // Create an instance of the database
        DbConnect dbConnect = new DbConnect(this);

        updateCategoryButton.setOnClickListener(view -> {
            String updatedCategoryName = categoryNameEditText.getText().toString();
            if (!updatedCategoryName.isEmpty()) {
                // Categories method to update the category name
                category.setCategoryName(updatedCategoryName);
                boolean isUpdated = dbConnect.updateCategory(category);
                if (isUpdated) {
                    Toast.makeText(this, "Category Updated Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Update Failed", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please enter category name", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
