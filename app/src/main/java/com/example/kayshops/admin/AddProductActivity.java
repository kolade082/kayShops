package com.example.kayshops.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.VectorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;


import com.example.kayshops.DbConnect;
import com.example.kayshops.R;
import com.example.kayshops.user.model.Categories;
import com.example.kayshops.user.model.ImageUtils;
import com.example.kayshops.user.model.Products;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

public class AddProductActivity extends AppCompatActivity {
    EditText productNameEditText, productDescriptionEditText,productSizeEditText,
            productPriceEditText, productListPriceEditText, productRetailPriceEditText,
            productDateCreatedEditText;
    Spinner productCategorySpinner;
    ImageView productImageView;
    Button add_product_btn;
    // Define a constant for the permission request
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }



        productNameEditText = findViewById(R.id.productNameEditText);
        productCategorySpinner = findViewById(R.id.productCategorySpinner);
        productDescriptionEditText = findViewById(R.id.productDescriptionEditText);
        productSizeEditText = findViewById(R.id.productSizeEditText);
        productPriceEditText = findViewById(R.id.productPriceEditText);
        productListPriceEditText = findViewById(R.id.productListPriceEditText);
        productRetailPriceEditText = findViewById(R.id.productRetailPriceEditText);
        productDateCreatedEditText = findViewById(R.id.productDateCreatedEditText);
        productImageView = findViewById(R.id.productImageView);
        add_product_btn = findViewById(R.id.update_product_btn);

        Drawable addImageDrawable = getResources().getDrawable(R.drawable.add_image);
        if (addImageDrawable != null) {
            productImageView.setImageDrawable(addImageDrawable);
        } else {
            Log.e("AddProductActivity", "Error: Drawable not found.");
        }


        // Create an instance of the database
        DbConnect dbConnect = new DbConnect(this);

        // Set current date and time to productDateCreatedEditText
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);
        String productDateCreated = String.format("%02d-%02d-%04d %02d:%02d:%02d", day, month, year, hour, minute, second);
        productDateCreatedEditText.setText(productDateCreated);


        // setting an image to the ImageView
        productImageView.setImageResource(R.drawable.add_image);

        // Fetch categories from the database
        List<Categories> categoriesList = dbConnect.getAllCategories();

        // Map List<Categories> to List<String> containing only category names
        List<String> categoryNames = categoriesList.stream().map(Categories::getCategoryName).collect(Collectors.toList());

        // Create an adapter for the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the adapter for the spinner
        productCategorySpinner.setAdapter(adapter);

        add_product_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String productName = productNameEditText.getText().toString();
                    String productCategory = (productCategorySpinner.getSelectedItem() != null) ? productCategorySpinner.getSelectedItem().toString() : "";
                    String productDescription = productDescriptionEditText.getText().toString();
                    String productSize = productSizeEditText.getText().toString();
                    String productPrice = productPriceEditText.getText().toString();
                    String productListPrice = productListPriceEditText.getText().toString();
                    String productRetailPrice = productRetailPriceEditText.getText().toString();
                    String productDateCreated = productDateCreatedEditText.getText().toString();
                    Integer categoryId = dbConnect.getCategoryId(productCategory);

                    Drawable drawable = productImageView.getDrawable();
                    String productImage = "";
                    if (drawable instanceof BitmapDrawable) {
                        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                        productImage = ImageUtils.saveBitmapAndGetPath(bitmap, productName, AddProductActivity.this, false);
                    } else if (drawable instanceof VectorDrawable || drawable instanceof GradientDrawable) {
                        Bitmap bitmap = ImageUtils.getBitmapFromVectorDrawable(drawable);
                        productImage = ImageUtils.saveBitmapAndGetPath(bitmap, productName, AddProductActivity.this, false);
                    }

                    if (productName.isEmpty() || productDescription.isEmpty() || productPrice.isEmpty() ||
                            productListPrice.isEmpty() || productRetailPrice.isEmpty() || productDateCreated.isEmpty()) {
                        // If any required fields are empty, show a toast indicating that all fields are required
                        Toast.makeText(AddProductActivity.this,
                                " Fields are required ",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Products products = new Products(productName, productCategory,
                                productDescription, productSize, productPrice,
                                productListPrice, productRetailPrice,
                                productDateCreated, productImage, categoryId);
                        dbConnect.addProduct(products);
                        dbConnect.close();
                        // If all details are valid, it shows a toast indicating user has been successfully registered
                        Toast.makeText(AddProductActivity.this,
                                " Successfully Added ",
                                Toast.LENGTH_LONG).show();

                        // Clear the fields
                        productNameEditText.setText("");
                        productCategorySpinner.setSelection(0);
                        productDescriptionEditText.setText("");
                        productSizeEditText.setText("");
                        productPriceEditText.setText("");
                        productListPriceEditText.setText("");
                        productRetailPriceEditText.setText("");
                        productImageView.setImageResource(R.drawable.add_image);
                    }
                } catch (Exception e) {
                    Log.e("AddProductActivity", "Error in onClick: ", e);
                }
            }
        });

        // setting an onClickListener to the ImageView to allow the user to select an image
        productImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 100);
            }
        });
    }

    // This is the method to handle the permissions request response
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(this, "Permission denied to read External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    // handling the result of the image selection
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == 100 && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();

            Bitmap bitmap = BitmapFactory.decodeFile(filePath);

            // create a new BitmapDrawable from the new bitmap
            BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);

            // set the new drawable to the image view
            productImageView.setImageDrawable(bitmapDrawable);
        }
    }
}