package com.example.kayshops.user;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.kayshops.DbConnect;
import com.example.kayshops.R;
import com.example.kayshops.user.adapter.ShoppingCartAdapter;
import com.example.kayshops.user.model.Products;
import com.example.kayshops.user.model.ShoppingCart;

import java.util.ArrayList;
import java.util.List;

public class BagFragment extends Fragment implements ShoppingCartAdapter.OnQuantityChangedListener, ShoppingCartAdapter.OnDeleteItemClickListener {
    private ShoppingCartAdapter shoppingCartAdapter;
    private TextView totalPriceTextView;
    Button checkoutButton;
    public static final String EXTRA_TOTAL_PRICE = "extra_total_price";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bag, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        DbConnect dbConnect = new DbConnect(getContext());

        List<Products> cartItems = ShoppingCart.getInstance().getCartItems();
        shoppingCartAdapter = new ShoppingCartAdapter(cartItems, (UserActivity) getActivity(), dbConnect);
        shoppingCartAdapter.setQuantityChangedListener(this);
        shoppingCartAdapter.setOnDeleteItemClickListener(this);
        recyclerView.setAdapter(shoppingCartAdapter);


        totalPriceTextView = view.findViewById(R.id.totalPriceTextView);

        updateTotalPrice();

        checkoutButton = view.findViewById(R.id.checkoutButton);
        checkoutButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CreateOrderActivity.class);
            List<Products> itemsInCart = ShoppingCart.getInstance().getCartItems();
            intent.putExtra("cartItems", new ArrayList<>(itemsInCart));
            double totalPrice = ShoppingCart.getInstance().getTotalPrice();
            intent.putExtra(EXTRA_TOTAL_PRICE, totalPrice);
            startActivity(intent);
        });

        return view;
    }

    // this method is used to update the RecyclerView data
    public void updateCartItems() {
        List<Products> cartItems = ShoppingCart.getInstance().getCartItems();
        shoppingCartAdapter.updateData(cartItems);
    }

    // this method is used to update the total price
    private void updateTotalPrice() {
        double totalPrice = ShoppingCart.getInstance().getTotalPrice();
        totalPriceTextView.setText("Total Price: £" + String.format("%.2f", totalPrice));
    }

    @Override
    public void onQuantityChanged() {
        updateTotalPrice();
    }

    @Override
    public void onDeleteItemClick(int position) {
        ShoppingCart.getInstance().removeItem(position);
        updateCartItems();
        updateTotalPrice();
    }
}