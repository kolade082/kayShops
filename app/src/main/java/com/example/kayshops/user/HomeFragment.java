package com.example.kayshops.user;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kayshops.DbConnect;
import com.example.kayshops.R;
import com.example.kayshops.user.adapter.ProductAdapter;
import com.example.kayshops.user.adapter.ProductFilterAdapter;
import com.example.kayshops.user.model.ProductFilter;
import com.example.kayshops.user.model.Products;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements ProductAdapter.OnProductItemClickListener {
    ProductFilterAdapter productFilterAdapter;
    RecyclerView productCatRecycler, productItemRecycler;
    ProductAdapter productAdapter;
    List<Products> productsList;
    private static final String SHARED_PREFS_NAME = "MyPrefs";
    private static final String FULL_NAME_KEY = "fullName";
    TextView nikeTextView, pumaTextView, bapeTextView, checkTextView, otherTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Retrieve the user's full name from shared preferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        String fullName = sharedPreferences.getString(FULL_NAME_KEY, "");

        // Setting the text of the TextView with the retrieved user's full name
        TextView welcomeTxtView = view.findViewById(R.id.welcomeTxtView);
        welcomeTxtView.setText("Hello, " + fullName);


        List<ProductFilter> productFilterList = new ArrayList<>();
        productFilterList.add(new ProductFilter(1, "Most Popular"));
        productFilterList.add(new ProductFilter(2, "New"));
        productFilterList.add(new ProductFilter(3, "Colour"));

        setProductRecycler(view, productFilterList); // Passing the view object to setProductRecycler

        DbConnect dbConnect = new DbConnect(getActivity());
        try {
            productsList = dbConnect.getAllProducts();

            // Debug log to check the product categories
            for (Products product : productsList) {
                if (product != null) {
                    Log.d("ProductCategoryCheck", "Product: " + product.getProductName() + ", Category: " + product.getProductCategory());
                }
            }
            setProductItemRecycler(view, productsList);
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception or show an error message to the user
            productsList = new ArrayList<>(); // Initialize the productsList as an empty ArrayList
        }


        setProductItemRecycler(view, productsList);

        // Add click listeners for the TextViews
        nikeTextView = view.findViewById(R.id.nikeTextView);
        pumaTextView = view.findViewById(R.id.pumaTextView);
        bapeTextView = view.findViewById(R.id.bapeTextView);
        checkTextView = view.findViewById(R.id.checkTextView);
        otherTextView = view.findViewById(R.id.otherTextView);

        nikeTextView.setOnClickListener(v -> filterProductsByCategory(1));
        pumaTextView.setOnClickListener(v -> filterProductsByCategory(2));
        bapeTextView.setOnClickListener(v -> {
            Toast.makeText(getContext(), "BAPE clicked", Toast.LENGTH_SHORT).show();
            filterProductsByCategory(5);
        });

        otherTextView.setOnClickListener(v -> filterProductsByCategory(4));

        checkTextView.setOnClickListener(v -> {
            BagFragment bagFragment = new BagFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.container, bagFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

    }

    private void setProductRecycler(View view, List<ProductFilter> productFilterList) { // Accept a View object as a parameter
        productCatRecycler = view.findViewById(R.id.categoryRecycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        //setting the LayoutManager and the Adapter for the RecyclerView
        productCatRecycler.setLayoutManager(layoutManager);
        productFilterAdapter = new ProductFilterAdapter(getActivity(), productFilterList);
        productCatRecycler.setAdapter(productFilterAdapter);
    }

    private void setProductItemRecycler(View view, List<Products> productsList) { // Accept a View object as a parameter
        productItemRecycler = view.findViewById(R.id.productRecycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        //setting the LayoutManager and the Adapter for the RecyclerView
        productItemRecycler.setLayoutManager(layoutManager);
        productAdapter = new ProductAdapter(getActivity(), productsList, this);
        productItemRecycler.setAdapter(productAdapter);
    }

    @Override
    public void onProductItemClick(Products product) {
        Intent intent = new Intent(getActivity(), ProductDetails.class);
        intent.putExtra("product", product);
        startActivity(intent);
    }

    public void filterProductsByCategory(int categoryId) {
        List<Products> filteredProducts = new ArrayList<>();

        if (productsList != null) {
            for (Products product : productsList) {
                if (product != null) {
                    String category = product.getProductCategory() != null ? product.getProductCategory().toUpperCase() : null;
                    if (category != null) {
                        switch (categoryId) {
                            case 1:
                                if (category.equalsIgnoreCase("NIKE")) {
                                    filteredProducts.add(product);
                                }
                                break;
                            case 2:
                                if (category.equalsIgnoreCase("PUMA")) {
                                    filteredProducts.add(product);
                                }
                                break;
                            case 4:
                                if (category.equalsIgnoreCase("OTHERS")) {
                                    filteredProducts.add(product);
                                }
                                break;
                            case 5:
                                if (category.equalsIgnoreCase("BAPE")) {
                                    filteredProducts.add(product);
                                }
                                break;
                        }
                    }
                }
            }
        }

        productAdapter.updateProductList(filteredProducts);  // update the adapter with the filtered list
    }

}
