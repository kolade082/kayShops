/**
 The UserActivity contains a bottom navigation which allows users to switch
 between different fragments depending on what clicked on
 */
package com.example.kayshops.user;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.kayshops.DbConnect;
import com.example.kayshops.R;
import com.example.kayshops.user.model.Products;
import com.example.kayshops.user.model.ShoppingCart;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.List;

public class UserActivity extends AppCompatActivity implements OnProductAddedToCartListener {
    BottomNavigationView bottomNav;

    // Create fragment objects
    HomeFragment homeFragment = new HomeFragment();
    SearchFragment searchFragment = new SearchFragment();
    BagFragment bagFragment = new BagFragment();
    ProfileFragment profileFragment = new ProfileFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        // Retrieve fullName value from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String fullName = sharedPreferences.getString("fullName", "");
        int userId = sharedPreferences.getInt("userId", -1);

        DbConnect dbConnect = new DbConnect(this);
        Users user = dbConnect.getUserById(userId);

        // Put the user in the intent
        getIntent().putExtra("user", user);
        bottomNav = findViewById(R.id.bottomNav);

        // Set the default fragment to be HomeFragment
        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();

        // Set a badge for the bag icon
        BadgeDrawable badgeDrawable = bottomNav.getOrCreateBadge(R.id.bag);
        badgeDrawable.setVisible(true);

        // Set listener for the bottom navigation bar
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    // Replace the container with the selected fragment
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
                        return true;
                    case R.id.search:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, searchFragment).commit();
                        return true;
                    case R.id.bag:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, bagFragment).commit();
                        return true;
                    case R.id.profile:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, profileFragment).commit();
                        return true;
                }
                return false;
            }
        });

        // openBagFragment
        Intent intent = getIntent();
        if (intent != null && intent.getBooleanExtra("openBagFragment", false)) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new HomeFragment())
                    .commit();
        }

        if (intent != null && intent.getBooleanExtra("openBagFragment", false)) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new HomeFragment())
                    .commit();
            if (intent.getBooleanExtra("fromProductDetails", false)) {
                updateBagBadge();
            }
        }

        if(getIntent().getBooleanExtra("loadHomeFragment", false)){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new HomeFragment())
                    .commit();
        }
        // Add this code to handle the "openFragment" extra
        String openFragment = getIntent().getStringExtra("openFragment");
        if ("HomeFragment".equals(openFragment)) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
        }
    }

    public void loadFragment(Fragment fragment) {
        // Load the given fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }
    public void updateBagBadge() {
        int cartSize = 0;

        for (Products product : ShoppingCart.getInstance().getCartItems()) {
            cartSize += product.getProductQuantity();
        }

        BadgeDrawable badgeDrawable = bottomNav.getOrCreateBadge(R.id.bag);
        badgeDrawable.setVisible(true);
        badgeDrawable.setNumber(cartSize);
    }

    public void updateBagFragmentAndBadge() {
        // Update the BagFragment
        if (bagFragment != null && bagFragment.isVisible()) {
            bagFragment.updateCartItems();
            updateTotalPrice();
        }

        // Update the badge
        updateBagBadge();
    }


    // this method is used track of product added so as to update the badge and number in the bag fragment
    @Override
    public void onProductAddedToCart() {
        updateBagFragmentAndBadge();

//        updateBagBadge();
    }
    public interface OnProductAddedToCartListener {
        void onProductAddedToCart();
    }
    private void updateTotalPrice() {
        List<Products> cartItems = ShoppingCart.getInstance().getCartItems();
        double totalPrice = 0.0;
        for (Products product : cartItems) {
            totalPrice += product.getProductQuantity() * Double.parseDouble(product.getProductPrice().replaceAll("£", ""));
        }
        // Get the inflated view of BagFragment and find the totalPriceTextView from there
        if (bagFragment != null && bagFragment.isVisible()) {
            TextView totalPriceTextView = bagFragment.getView().findViewById(R.id.totalPriceTextView);
            totalPriceTextView.setText("Total Price: £" + String.format("%.2f", totalPrice));
        }
    }

}