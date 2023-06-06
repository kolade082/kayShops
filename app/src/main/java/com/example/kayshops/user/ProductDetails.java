package com.example.kayshops.user;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kayshops.R;
import com.example.kayshops.user.model.Products;
import com.example.kayshops.user.model.ShoppingCart;

import java.io.File;

public class ProductDetails extends AppCompatActivity {
    private OnProductAddedToCartListener onProductAddedToCartListener;

    public void setOnProductAddedToCartListener(OnProductAddedToCartListener onProductAddedToCartListener) {
        this.onProductAddedToCartListener = onProductAddedToCartListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        setOnProductAddedToCartListener((UserActivity) getParent());

        ImageView productImage = findViewById(R.id.product_image);
        TextView productCategory = findViewById(R.id.productCategory);
        TextView productSize = findViewById(R.id.productSize);
        TextView productName = findViewById(R.id.product);
        TextView productPrice = findViewById(R.id.product_price);
        TextView productDescription = findViewById(R.id.productDescription);
        ImageView returnImageView = findViewById(R.id.returnImageView);
        Button addToCartBtn = findViewById(R.id.addToCartBtn);


        Intent intent = getIntent();
        Products productDetails = null;
        if (intent != null && intent.hasExtra("product")) {
            productDetails = (Products) intent.getSerializableExtra("product");
            Glide.with(productImage.getContext())
                    .load(new File(productDetails.getProductImage()))
                    .into(productImage);
            // Set other details based on the received product
             productCategory.setText(productDetails.getProductCategory());
            productSize.setText("Size " + productDetails.getProductSize());
            productName.setText(productDetails.getProductName());
            productPrice.setText("£"+productDetails.getProductPrice());
            productDescription.setText(productDetails.getProductDescription());
        }

        final Products finalProductDetails = productDetails;
        returnImageView.setOnClickListener(view -> onBackPressed());

        if (finalProductDetails != null) {
            addToCartBtn.setOnClickListener(view -> {
                ShoppingCart.getInstance().addToCart(finalProductDetails);

                // Calling the update method in the activity to update the BagFragment and the badge
                if (onProductAddedToCartListener != null) {
                    onProductAddedToCartListener.onProductAddedToCart();
                }

                Intent resultIntent = new Intent();
                resultIntent.putExtra("productAddedToCart", true);
                setResult(Activity.RESULT_OK, resultIntent);
                // Navigate to the BagFragment
                onBackPressed();
                Intent intentToBag = new Intent(ProductDetails.this, UserActivity.class);
                intentToBag.putExtra("openBagFragment", true);
                intentToBag.putExtra("fromProductDetails", true);
                startActivity(intentToBag);
                finish();
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}