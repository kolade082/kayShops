package com.example.kayshops.user.model;

import java.io.Serializable;

public class Products implements Serializable {
    Integer productId;
    Integer productQuantity;

    public Products(String productName, String productCategory, String productDescription, String productSize, String productPrice, String productListPrice, String productRetailPrice, String productDateCreated, String productImage, Integer categoryId) {
        this.productName = productName;
        this.productCategory = productCategory;
        this.productDescription = productDescription;
        this.productSize = productSize;
        this.productPrice = productPrice;
        this.productListPrice = productListPrice;
        this.productRetailPrice = productRetailPrice;
        this.productDateCreated = productDateCreated;
        this.productImage = productImage;
        this.categoryId = categoryId;
    }
    public Products(Integer productId, String productName, String productCategory, String productDescription, String productSize, String productPrice, String productListPrice, String productRetailPrice, String productDateCreated, String productImage, Integer categoryId) {
        this.productId = productId;
        this.productName = productName;
        this.productCategory = productCategory;
        this.productDescription = productDescription;
        this.productSize = productSize;
        this.productPrice = productPrice;
        this.productListPrice = productListPrice;
        this.productRetailPrice = productRetailPrice;
        this.productDateCreated = productDateCreated;
        this.productImage = productImage;
        this.categoryId = categoryId;
    }

    String productName;
    String productCategory;
    String productDescription;
    String productSize;
    String productPrice;
    String productListPrice;
    String productRetailPrice;
    String productDateCreated;
    String productDateUpdated;

    public Products(String productName, String productDescription, String productImage) {
        this.productName = productName;
        this.productDescription = productDescription;
        this.productImage = productImage;
    }

    public Products(Integer productId, String productName, String productDescription, String productSize, String productPrice, String productImage, String productCategory) {
        this.productId = productId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productSize = productSize;
        this.productPrice = productPrice;
        this.productImage = productImage;
        this.productCategory = productCategory;
    }

    String productImage;
    Integer categoryId;


    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getProductListPrice() {
        return productListPrice;
    }

    public void setProductListPrice(String productListPrice) {
        this.productListPrice = productListPrice;
    }

    public String getProductRetailPrice() {
        return productRetailPrice;
    }

    public void setProductRetailPrice(String productRetailPrice) {
        this.productRetailPrice = productRetailPrice;
    }

    public String getProductDateCreated() {
        return productDateCreated;
    }

    public void setProductDateCreated(String productDateCreated) {
        this.productDateCreated = productDateCreated;
    }

    public String getProductDateUpdated() {
        return productDateUpdated;
    }

    public void setProductDateUpdated(String productDateUpdated) {
        this.productDateUpdated = productDateUpdated;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public Integer getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(Integer productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductSize() {
        return productSize;
    }

    public void setProductSize(String productSize) {
        this.productSize = productSize;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    @Override
    public String toString() {
        return "Products{" +
                "productName='" + productName + '\'' +
                ", productDescription='" + productDescription + '\'' +
                ", productSize='" + productSize + '\'' +
                ", productPrice='" + productPrice + '\'' +
                ", productListPrice='" + productListPrice + '\'' +
                ", productRetailPrice='" + productRetailPrice + '\'' +
                ", productDateCreated='" + productDateCreated + '\'' +
                ", productImage='" + productImage + '\'' +
                ", productCategory='" + productCategory + '\'' +
                '}';
    }

}

