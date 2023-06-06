package com.example.kayshops.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kayshops.DbConnect;
import com.example.kayshops.R;
import com.example.kayshops.user.model.Categories;

public class AddCategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        EditText categoryNameEditText = findViewById(R.id.categoryNameEditText);
        Button add_category_btn = findViewById(R.id.update_category_btn);
        // Create an instance of the database
        DbConnect dbConnect = new DbConnect(this);



        add_category_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String categoryName = categoryNameEditText.getText().toString();

                    if (categoryName.isEmpty()) {
                        // If any required fields are empty, show a toast indicating that all fields are required
                        Toast.makeText(AddCategoryActivity.this,
                                " Fields are required ",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Categories categories = new Categories(categoryName);
                        dbConnect.addCategory(categories);
                        dbConnect.close();
                        // If all details are valid, it shows a toast indicating user has been successfully registered
                        Toast.makeText(AddCategoryActivity.this,
                                " Successfully Added ",
                                Toast.LENGTH_LONG).show();

                        // Clear the fields
                        categoryNameEditText.setText("");
                    }
                } catch (Exception e) {
                    Log.e("AddProductActivity", "Error in onClick: ", e);
                }
            }
        });
    }
}