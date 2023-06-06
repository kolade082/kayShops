package com.example.kayshops.user.model;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {
    private static ShoppingCart instance;
    private List<Products> cartItems;

    private ShoppingCart() {
        cartItems = new ArrayList<>();
    }

    public static ShoppingCart getInstance() {
        if (instance == null) {
            instance = new ShoppingCart();
        }
        return instance;
    }

    public List<Products> getCartItems() {
        return cartItems;
    }

    public void addToCart(Products product) {
        for (Products cartItem : cartItems) {
            if (cartItem.getProductId().equals(product.getProductId())) {
                cartItem.setProductQuantity(cartItem.getProductQuantity() + 1);
                return;
            }
        }
        product.setProductQuantity(1);
        cartItems.add(product);
    }

    public double getTotalPrice() {
        double totalPrice = 0.0;
        for (Products product : cartItems) {
            String priceString = product.getProductPrice().replace("£", "");
            double price = Double.parseDouble(priceString);
            totalPrice += product.getProductQuantity() * price;
        }
        return totalPrice;
    }

    public int getCartSize() {
        return cartItems.size();
    }
    public void removeItem(int position) {
        if (position >= 0 && position < cartItems.size()) {
            cartItems.remove(position);
        }
    }
    public void clearCart() {
        cartItems.clear();
    }

}
