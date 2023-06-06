package com.example.kayshops.admin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.VectorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class UpdateProductActivity extends AppCompatActivity {
    private Products products;
    EditText productNameEditText, productDescriptionEditText, productSizeEditText,
            productPriceEditText, productListPriceEditText, productRetailPriceEditText,
            productDateCreatedEditText;
    Spinner productCategorySpinner;
    ImageView productImageView;
    Button updateProductButton;
    // Create an instance of the database
    DbConnect dbConnect = new DbConnect(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);

        products = (Products) getIntent().getSerializableExtra("product");

        productNameEditText = findViewById(R.id.productNameEditText);
        productCategorySpinner = findViewById(R.id.productCategorySpinner);
        productDescriptionEditText = findViewById(R.id.productDescriptionEditText);
        productSizeEditText = findViewById(R.id.productSizeEditText);
        productPriceEditText = findViewById(R.id.productPriceEditText);
        productListPriceEditText = findViewById(R.id.productListPriceEditText);
        productRetailPriceEditText = findViewById(R.id.productRetailPriceEditText);
        productDateCreatedEditText = findViewById(R.id.productDateCreatedEditText);
        productImageView = findViewById(R.id.productImageView);
        updateProductButton = findViewById(R.id.update_product_btn);

        if (products != null) {
            productNameEditText.setText(products.getProductName());
            productDescriptionEditText.setText(products.getProductDescription());
            productSizeEditText.setText(products.getProductSize());
            productPriceEditText.setText(String.valueOf(products.getProductPrice()));
            productListPriceEditText.setText(String.valueOf(products.getProductListPrice()));
            productRetailPriceEditText.setText(String.valueOf(products.getProductRetailPrice()));
            productDateCreatedEditText.setText(products.getProductDateCreated());
            // setting image view needs to load image from URL or resource
            String productImagePath = products.getProductImage();
            Bitmap myBitmap = BitmapFactory.decodeFile(productImagePath);
            productImageView.setImageBitmap(myBitmap);
        }
        List<Categories> categoriesList = dbConnect.getAllCategories();

        // Map List<Categories> to List<String> containing only category names
        List<String> categoryNames = categoriesList.stream().map(Categories::getCategoryName).collect(Collectors.toList());
        int currentCategoryIndex = categoryNames.indexOf(products.getProductCategory());


        // Create an adapter for the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Setting the adapter for the spinner
        productCategorySpinner.setAdapter(adapter);

        // Setting the spinner's selected item to the current product's category
        if (currentCategoryIndex != -1) {
            productCategorySpinner.setSelection(currentCategoryIndex);
        }

        updateProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateFields()) {
                    Products updatedProduct = getUpdatedProduct();
                    updateProductInDatabase(updatedProduct);
                } else {
                    Toast.makeText(UpdateProductActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        productImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 100);
            }
        });

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



    private boolean validateFields() {
        String productName = productNameEditText.getText().toString();
        String productDescription = productDescriptionEditText.getText().toString();
        String productPrice = productPriceEditText.getText().toString();
        String productListPrice = productListPriceEditText.getText().toString();
        String productRetailPrice = productRetailPriceEditText.getText().toString();
        String productDateCreated = productDateCreatedEditText.getText().toString();

        return !productName.isEmpty() && !productDescription.isEmpty() && !productPrice.isEmpty() &&
                !productListPrice.isEmpty() && !productRetailPrice.isEmpty() && !productDateCreated.isEmpty();
    }

    private Products getUpdatedProduct() {
        String updatedProductName = productNameEditText.getText().toString();
        String updatedProductCategory = (productCategorySpinner.getSelectedItem() != null) ? productCategorySpinner.getSelectedItem().toString() : "";
        String updatedProductDescription = productDescriptionEditText.getText().toString();
        String updatedProductSize = productSizeEditText.getText().toString();
        String updatedProductPrice = productPriceEditText.getText().toString();
        String updatedProductListPrice = productListPriceEditText.getText().toString();
        String updatedProductRetailPrice = productRetailPriceEditText.getText().toString();
        String updatedProductDateCreated = productDateCreatedEditText.getText().toString();
//        String updatedProductCategory = productCategorySpinner.getSelectedItem().toString();
        Drawable drawable = productImageView.getDrawable();
        String updatedProductImage = "";
        if (drawable instanceof BitmapDrawable) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            updatedProductImage = ImageUtils.saveBitmapAndGetPath(bitmap, updatedProductName, UpdateProductActivity.this, true);
        } else if (drawable instanceof VectorDrawable || drawable instanceof GradientDrawable) {
            Bitmap bitmap = ImageUtils.getBitmapFromVectorDrawable(drawable);
            updatedProductImage = ImageUtils.saveBitmapAndGetPath(bitmap, updatedProductName, UpdateProductActivity.this, true);
        }
        Integer updatedCategoryId = dbConnect.getCategoryId(updatedProductCategory);
        int productId = products.getProductId();
        Products updatedProduct = new Products(productId, updatedProductName, updatedProductCategory, updatedProductDescription, updatedProductSize,
                updatedProductPrice, updatedProductListPrice, updatedProductRetailPrice,
                updatedProductDateCreated, updatedProductImage, updatedCategoryId);
        return updatedProduct;
    }

    private void updateProductInDatabase(Products updatedProduct) {
        boolean isUpdated = dbConnect.updateProduct(updatedProduct);
        if (isUpdated) {
            Toast.makeText(UpdateProductActivity.this, "Product Updated Successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(UpdateProductActivity.this, "Update Failed", Toast.LENGTH_SHORT).show();
        }
    }
}