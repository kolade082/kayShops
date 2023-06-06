package com.example.kayshops.user.model;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Orders {
    private String orderNumber;
    private String orderDate;
    private String orderStatus;
    private String orderTotalPrice;
    private Calendar deliveryDate;
    private int userId;
    private List<Products> products;

    public Orders(String orderNumber, String orderStatus, String orderTotalPrice, Calendar deliveryDate, int userId, List<Products> products) {
        this.orderNumber = orderNumber;
        this.orderStatus = orderStatus;
        this.orderTotalPrice = orderTotalPrice;
        this.userId = userId;
        this.products = products;
    }


    public List<Products> getProducts() {
        return products;
    }

    public void setProducts(List<Products> products) {
        this.products = products;
    }

    public Orders(String orderNumber, String orderStatus, String orderTotalPrice, int userId) {
        this.orderNumber = orderNumber;
        this.orderStatus = orderStatus;
        this.orderTotalPrice = orderTotalPrice;
        this.userId = userId;

        this.orderDate = getCurrentDateTime();
        this.deliveryDate = generateDeliveryDate();
    }

    private String getCurrentDateTime() {
        // Get the current date and time
        Date currentDate = Calendar.getInstance().getTime();

        // Format it as needed
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(currentDate);
    }

    private static Calendar generateDeliveryDate() {
        // Get the current date
        Calendar currentDate = Calendar.getInstance();

        // Add 3 days to the current date
        Calendar deliveryDate = Calendar.getInstance();
        deliveryDate.add(Calendar.DAY_OF_MONTH, 3);

        return deliveryDate;
    }

    public String getOrderStatus() {
        // Get the current date
        Calendar currentDate = Calendar.getInstance();

        // Compare the current date with the delivery date
        if (currentDate.before(deliveryDate)) {
            return "Pending";
        } else {
            return "Delivered";
        }
    }

    public String getDeliveryDate() {
        // Format the delivery date as "MMM dd, yyyy" (e.g., May 15, 2023)
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        return dateFormat.format(deliveryDate.getTime());
    }

    // Getters for order data
    public String getOrderNumber() {
        return orderNumber;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public String getOrderTotalPrice() {
        return orderTotalPrice;
    }

    public int getUserId() {
        return userId;
    }
}

