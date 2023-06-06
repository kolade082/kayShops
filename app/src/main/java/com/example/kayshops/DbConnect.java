/**
 This class is responsible for creating and managing the database used by the application.
 It extends SQLiteOpenHelper, a helper class to manage database creation and version management.
 */

package com.example.kayshops;

import static com.example.kayshops.PasswordHasher.hashPassword;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.kayshops.user.Users;
import com.example.kayshops.user.model.Categories;
import com.example.kayshops.user.model.Orders;
import com.example.kayshops.user.model.Products;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DbConnect extends SQLiteOpenHelper {
    private static String DBNAME = "kayShop.db";
    private static String ID = "id";
    private static int DBVERSION = 1;

    // column for users table
    private static String dbTableUsers = "users";
    private static String fullName = "full_name";
    private static String password = "password";
    private static String dateOfBirth = "date_of_birth";
    private static String email = "email";
    private static String phoneNumber = "phone_number";
    private static String streetAddress = "street_address";
    private static String cityAddress = "city_address";
    private static String countyAddress = "county_address";
    private static String postcode = "postcode";
    private static String userType = "user_type";
    private static String userDateCreated = "user_date_created";
    private static String userDateUpdated = "user_date_updated";

    // column for product table
    private static String dbTableProducts = "products";
    private static String productName = "product_name";
    private static String productDescription = "product_description";
    private static String productSize = "product_size";
    private static String productPrice = "product_price";
    private static String productImage = "product_image";
    private static String productListPrice = "product_list_price";
    private static String productRetailPrice = "product_retail_price";
    private static String productDateCreated = "product_date_created";
    private static String productDateUpdated = "product_date_updated";
    private static String categoryId = "category_id";

    // column for categories table
    private static String dbTableCategories = "categories";
    private static String categoryName = "category_name";

    // column for order table
    private static  String dbTableOrders = "orders";
    private static String orderNumber = "order_number";
    private static String userId = "user_id";
    private static String orderDate = "order_date";
    private static String orderStatus = "order_status";
    private static String orderTotalPrice = "order_total_price";
    private static String deliveryDate = "delivery_date";

    // column for order_product table
    private static String dbTableOrderProducts = "order_products"; // table name
    private static String orderId = "orderId";
    private static String productId = "productId";

    /**
     * Constructor for DbConnect class
     *
     * @param context - the context in which the database is being used
     */
    public DbConnect(@Nullable Context context) {
        super(context, DBNAME, null, DBVERSION);
    }

    /**
     * Called when the database is created for the first time. This is where the creation
     * of tables and the initial population of the tables should happen.
     *
     * @param sqLiteDatabase - the database being created
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // create users table
        String queryUsers = "CREATE TABLE " + dbTableUsers + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + fullName + " TEXT, " + password + " TEXT, " + dateOfBirth + " TEXT, "
                + email + " TEXT, " + phoneNumber + " TEXT, " + streetAddress + " TEXT, "
                + cityAddress + " TEXT, " + countyAddress + " TEXT, " + postcode + " TEXT, "
                + userType + " TEXT DEFAULT 'user', "
                + userDateCreated + " TEXT, " + userDateUpdated + " TEXT)";
        // create category table
        String queryCategories = "CREATE TABLE " + dbTableCategories + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + categoryName + " TEXT DEFAULT 'categories')";
        // create products table
        String queryProducts = "CREATE TABLE " + dbTableProducts + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + productName + " TEXT, " + productDescription + " TEXT, " + productSize + " TEXT, " + productPrice + " TEXT, " + productImage + " TEXT, "
                + productListPrice + " TEXT, " + productRetailPrice + " TEXT, " + productDateCreated + " TEXT, "
                + productDateUpdated + " TEXT, "
                + categoryId + " INTEGER, "
                + "FOREIGN KEY(" + categoryId + ") REFERENCES " + dbTableCategories + "(" + ID + "))";
        // create orders table
        String queryOrders = "CREATE TABLE " + dbTableOrders + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + orderNumber + " TEXT, " + orderDate + " TEXT, " + orderStatus
                + " TEXT, " + orderTotalPrice  + " TEXT, " + deliveryDate + " TEXT, " + userId + " INTEGER, "
                + "FOREIGN KEY(" + userId + ") REFERENCES " + dbTableUsers + "(" + ID + "))";
        // create order_product table
        String queryOrderProducts = "CREATE TABLE " + dbTableOrderProducts + "(" + orderId + " INTEGER, " + productId + " INTEGER, "
                + "FOREIGN KEY(" + orderId + ") REFERENCES " + dbTableOrders + "(" + ID + "), "
                + "FOREIGN KEY(" + productId + ") REFERENCES " + dbTableProducts + "(" + ID + "))";
        // execute the queries to create the tables
        sqLiteDatabase.execSQL(queryUsers);
        sqLiteDatabase.execSQL(queryCategories);
        sqLiteDatabase.execSQL(queryProducts);
        sqLiteDatabase.execSQL(queryOrders);
        sqLiteDatabase.execSQL(queryOrderProducts);
    }

    /**
     * Called when the database needs to be upgraded. This method will only be called if
     * a database already exists on disk with the same DBNAME, but the DBVERSION is different
     *
     * @param sqLiteDatabase - the database being upgraded
     * @param i1     - the version of the old database
     * @param i     - the version of the new database
     */

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbTableUsers);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbTableProducts);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbTableCategories);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbTableOrders);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbTableOrderProducts);
        onCreate(sqLiteDatabase);
    }

    // add a new user to the database
    public void addUser(Users user) throws NoSuchAlgorithmException {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(fullName, user.getFullName());
        // hash the password before storing it in the database
        String hashedPassword = hashPassword(user.getPassword());
        values.put(password, hashedPassword);
        values.put(dateOfBirth, user.getDateOfBirth());
        values.put(email, user.getEmail());
        values.put(phoneNumber, user.getPhoneNumber());
        values.put(streetAddress, user.getStreetAddress());
        values.put(cityAddress, user.getCityAddress());
        values.put(countyAddress, user.getCountyAddress());
        values.put(postcode, user.getPostcode());
        values.put(userDateCreated, getCurrentDateTime());
        database.insert(dbTableUsers, null, values);
        database.close();
    }

    // add a new product to the database
    public void addProduct(Products product) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(productName, product.getProductName());
        values.put(productDescription, product.getProductDescription());
        values.put(productSize, product.getProductSize());
        values.put(productPrice, product.getProductPrice());
        values.put(productImage, product.getProductImage());
        values.put(productListPrice, product.getProductListPrice());
        values.put(productRetailPrice, product.getProductRetailPrice());
        values.put(productDateCreated, product.getProductDateCreated());
        values.put(productDateUpdated, product.getProductDateUpdated());
        values.put(categoryId, product.getCategoryId());
        database.insert(dbTableProducts, null, values);
        database.close();
    }

    // add a new category to the database
    public void addCategory(Categories category) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(categoryName, category.getCategoryName());
        database.insert(dbTableCategories, null, values);
        database.close();
    }

    // add a new order to the database
    public void addOrder(Orders orders) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Add values for each column
        values.put(orderNumber, orders.getOrderNumber());
        values.put(orderDate, orders.getOrderDate());
        values.put(orderStatus, orders.getOrderStatus());
        values.put(orderTotalPrice, orders.getOrderTotalPrice());
        values.put(deliveryDate, orders.getDeliveryDate());
        values.put(userId, orders.getUserId());

        // Insert the new order into the database
        database.insert(dbTableOrders, null, values);
        database.close();
    }



    // this method is used check if the email and password entered by the user are valid
    public String checkEmailPassword(Users users) throws NoSuchAlgorithmException {
        SQLiteDatabase database = this.getReadableDatabase();
        // hash the password before storing it in the database
        String hashedPassword = hashPassword(users.getPassword());
        String query = "SELECT *, " + userType + " from " + dbTableUsers + " where " + email + "=? AND "
                + password + " =?";
        Cursor cursor = database.rawQuery(query, new String[]{users.getEmail(), hashedPassword});
        String[] columnNames = cursor.getColumnNames();
        for (String name : columnNames) {
            Log.d("DbConnect", "Column name: " + name);
        }
        if (cursor.moveToFirst()){
            @SuppressLint("Range") String userType = cursor.getString(cursor.getColumnIndex("user_type"));
            if (userType.equals("admin")) {
                return "admin";
            } else {
                return "user";
            }
        } else {
            return "invalid";
        }
    }

    // this is used to pick out the user logged in from the database based on the information entered
    public Users getUserByEmail(String email) {
        SQLiteDatabase database = this.getReadableDatabase();
        String query = "SELECT * from " + dbTableUsers + " where " + DbConnect.email + "=?";
        Cursor cursor = database.rawQuery(query, new String[]{email});
        if (cursor.moveToFirst()) {
            Users user = new Users(fullName, password, dateOfBirth, email, phoneNumber,
                    streetAddress, cityAddress, countyAddress, postcode);
            user.setUserID(cursor.getInt(0));
            user.setFullName(cursor.getString(1));
            user.setPassword(cursor.getString(2));
            user.setDateOfBirth(cursor.getString(3));
            user.setEmail(cursor.getString(4));
            user.setPhoneNumber(cursor.getString(5));
            user.setStreetAddress(cursor.getString(6));
            user.setCityAddress(cursor.getString(7));
            user.setCountyAddress(cursor.getString(8));
            user.setPostcode(cursor.getString(9));
            return user;
        } else {
            return null;
        }
    }
    public Users getUserById(int id) {
        SQLiteDatabase database = this.getReadableDatabase();
        String query = "SELECT * from " + dbTableUsers + " where " + ID + "=?";
        Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(id)});
        if (cursor.moveToFirst()) {
            Users user = new Users();
            user.setUserID(cursor.getInt(cursor.getColumnIndexOrThrow(ID)));
            user.setFullName(cursor.getString(cursor.getColumnIndexOrThrow(fullName)));
            user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(password)));
            user.setDateOfBirth(cursor.getString(cursor.getColumnIndexOrThrow(dateOfBirth)));
            user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(email)));
            user.setPhoneNumber(cursor.getString(cursor.getColumnIndexOrThrow(phoneNumber)));
            user.setStreetAddress(cursor.getString(cursor.getColumnIndexOrThrow(streetAddress)));
            user.setCityAddress(cursor.getString(cursor.getColumnIndexOrThrow(cityAddress)));
            user.setCountyAddress(cursor.getString(cursor.getColumnIndexOrThrow(countyAddress)));
            user.setPostcode(cursor.getString(cursor.getColumnIndexOrThrow(postcode)));
            return user;
        } else {
            return null;
        }
    }

    // This is used to get all users from the database
    public List<Users> getAllUsers() {
        List<Users> users = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + dbTableUsers, null);
        if (cursor.moveToFirst()) {
            do {
                int userId = cursor.getInt(cursor.getColumnIndexOrThrow(ID));
                String fullName = cursor.getString(cursor.getColumnIndexOrThrow(this.fullName));
                String dateOfBirth = cursor.getString(cursor.getColumnIndexOrThrow(this.dateOfBirth));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(this.email));
                String phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(this.phoneNumber));
                String userType = cursor.getString(cursor.getColumnIndexOrThrow(this.userType));
                users.add(new Users(userId, fullName, dateOfBirth, email, phoneNumber, userType));

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return users;
    }
    // this is used to get all categories from the database
    public List<Categories> getAllCategories() {
        List<Categories> categoryList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + dbTableCategories;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Categories category = new Categories(cursor.getString(1)); // assuming categoryName is the second column
                category.setCategoryId(cursor.getInt(0)); // assuming ID is the first column
                categoryList.add(category);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return categoryList;
    }

    // This is used to get all products from the database
    public List<Products> getAllProducts() {
        List<Products> products = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + dbTableProducts, null);
        if (cursor.moveToFirst()) {
            do {
                int productId = cursor.getInt(cursor.getColumnIndexOrThrow(ID));
                String productName = cursor.getString(cursor.getColumnIndexOrThrow(this.productName));
                String productDescription = cursor.getString(cursor.getColumnIndexOrThrow(this.productDescription));
                String productSize = cursor.getString(cursor.getColumnIndexOrThrow(this.productSize));
                String productPrice = cursor.getString(cursor.getColumnIndexOrThrow(this.productPrice));
                String productListPrice = cursor.getString(cursor.getColumnIndexOrThrow(this.productListPrice));
                String productRetailPrice = cursor.getString(cursor.getColumnIndexOrThrow(this.productRetailPrice));
                String productDateCreated = cursor.getString(cursor.getColumnIndexOrThrow(this.productDateCreated));
                String productDateUpdated = cursor.getString(cursor.getColumnIndexOrThrow(this.productDateUpdated));
                String productImage = cursor.getString(cursor.getColumnIndexOrThrow(this.productImage));
                int productCategoryId = cursor.getInt(cursor.getColumnIndexOrThrow(this.categoryId));
                String productCategoryName = getCategoryNameById(productCategoryId);
                products.add(new Products(productId, productName, productCategoryName, productDescription, productSize, productPrice, productListPrice, productRetailPrice, productDateCreated, productImage, productCategoryId));

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return products;
    }

    // This is used to get all orders from the database
    public List<Orders> getAllOrders() {
        List<Orders> allOrders = new ArrayList<>();

        SQLiteDatabase database = this.getReadableDatabase();
        String query = "SELECT * FROM " + dbTableOrders;
        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int orderId = cursor.getInt(cursor.getColumnIndexOrThrow(ID));
                String orderNumber = cursor.getString(cursor.getColumnIndexOrThrow(this.orderNumber));
                String orderStatus = cursor.getString(cursor.getColumnIndexOrThrow(this.orderStatus));
                String orderTotalPrice = cursor.getString(cursor.getColumnIndexOrThrow(this.orderTotalPrice));
                int userId = cursor.getInt(cursor.getColumnIndexOrThrow(this.userId));

                // Create a new order without the related products
                Orders order = new Orders(orderNumber, orderStatus, orderTotalPrice, userId);

                // Add the order to the list
                allOrders.add(order);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return allOrders;
    }


    public Integer getCategoryId(String categoryName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("categories",
                new String[]{"id"},
                "category_name=?",
                new String[]{categoryName}, // selection argument
                null, null, null);
        if (cursor.moveToFirst()) {
            int categoryId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            cursor.close();
            return categoryId;
        } else {
            cursor.close();
            return null;
        }
    }
    public String getCategoryNameById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(dbTableCategories,
                new String[]{categoryName},
                ID + "=?",
                new String[]{String.valueOf(id)}, // selection argument
                null, null, null);
        if (cursor.moveToFirst()) {
            String categoryName = cursor.getString(cursor.getColumnIndexOrThrow(this.categoryName));
            cursor.close();
            return categoryName;
        } else {
            cursor.close();
            return null;
        }
    }
    // delete category
    public boolean deleteCategory(Categories categories){
        SQLiteDatabase db = this.getWritableDatabase();
        int endResult = db.delete(dbTableCategories, ID + "=?",
                new String[]{String.valueOf(categories.getCategoryId())});
        Log.d("DeleteCategory", "Deleting category with id: " + categories.getCategoryId() + ". Delete result: " + endResult);
        if(endResult > 0){
            return true;
        }else {
            return false;
        }
    }
    // delete category
    public boolean deleteProduct(Products products){
        SQLiteDatabase db = this.getWritableDatabase();
        int endResult = db.delete(dbTableProducts, ID + "=?", new String[]{String.valueOf(products.getProductId())});
        if(endResult > 0){
            return true;
        }else {
            return false;
        }
    }
    // delete user

    public boolean deleteUser(Users users){
        SQLiteDatabase db = this.getWritableDatabase();
        int endResult = db.delete(dbTableUsers, ID + "=?", new String[]{String.valueOf(users.getUserID())});
        if(endResult > 0){
            return true;
        }else {
            return false;
        }
    }

    public boolean updateUser(Users user) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(fullName, user.getFullName());
        values.put(dateOfBirth, user.getDateOfBirth());
        values.put(email, user.getEmail());
        values.put(phoneNumber, user.getPhoneNumber());
        values.put(userType, user.getUserType());

        // Update the userDateUpdated column with the current date and time
        String currentDateTime = getCurrentDateTime();
        values.put(userDateUpdated, currentDateTime);

        int rowsAffected = database.update("Users", values, "id = ?",
                new String[]{String.valueOf(user.getUserID())});
        return rowsAffected > 0;
    }
    public boolean updateUserAddress(Users user) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(streetAddress, user.getStreetAddress());
        values.put(cityAddress, user.getCityAddress());
        values.put(countyAddress, user.getCountyAddress());
        values.put(postcode, user.getPostcode());

        // Update the userDateUpdated column with the current date and time
        String currentDateTime = getCurrentDateTime();
        values.put(userDateUpdated, currentDateTime);

        int rowsAffected = database.update("Users", values, "id = ?",
                new String[]{String.valueOf(user.getUserID())});
        return rowsAffected > 0;
    }


    public boolean updatePassword(int userID, String newPassword) throws NoSuchAlgorithmException {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Hash the password
        String hashedPassword = hashPassword(newPassword);
        values.put(password, hashedPassword);

        int rowsAffected = db.update("Users", values, "id = ?",
                new String[]{String.valueOf(userID)});
        db.close();

        return rowsAffected > 0;
    }

    public boolean isPasswordCorrect(int userId, String inputPassword) throws NoSuchAlgorithmException {
        SQLiteDatabase db = this.getReadableDatabase();
        String hashedInputPassword = hashPassword(inputPassword);

        Cursor cursor = db.query("Users", new String[]{password}, "id = ?",
                new String[]{String.valueOf(userId)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String storedPassword = cursor.getString(cursor.getColumnIndexOrThrow(password));
            cursor.close();
            db.close();
            return hashedInputPassword.equals(storedPassword);
        }

        return false;
    }



    // update category
    public boolean updateCategory(Categories categories) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(categoryName, categories.getCategoryName());
        int rowsAffected = database.update(dbTableCategories, values, ID + "=?",
                new String[]{String.valueOf(categories.getCategoryId())});
        return rowsAffected > 0;
    }

    // update product
    public boolean updateProduct(Products products) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(productName, products.getProductName());
        values.put(productDescription, products.getProductDescription());
        values.put(productSize, products.getProductSize());
        values.put(productPrice, products.getProductPrice());
        values.put(productImage, products.getProductImage());
        values.put(productListPrice, products.getProductListPrice());
        values.put(productRetailPrice, products.getProductRetailPrice());
        values.put(productDateCreated, products.getProductDateCreated());

        // Update the productDateUpdated column with the current date and time
        String currentDateTime = getCurrentDateTime();
        values.put(productDateUpdated, currentDateTime);

        values.put(categoryId, products.getCategoryId());
        int rowsAffected = database.update(dbTableProducts, values, ID + "=?",
                new String[]{String.valueOf(products.getProductId())});
        return rowsAffected > 0;
    }

    // Helper method to get the current date and time in the desired format
    private String getCurrentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        Date currentDate = new Date();
        return dateFormat.format(currentDate);
    }


    public List<Orders> getUserOrders(int userId) {
        List<Orders> userOrders = new ArrayList<>();

        SQLiteDatabase database = this.getReadableDatabase();
        String query = "SELECT * FROM " + dbTableOrders + " WHERE " + this.userId + "=?";
        Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                int orderId = cursor.getInt(cursor.getColumnIndexOrThrow(ID));
                String orderNumber = cursor.getString(cursor.getColumnIndexOrThrow(this.orderNumber));
                String orderStatus = cursor.getString(cursor.getColumnIndexOrThrow(this.orderStatus));
                String orderTotalPrice = cursor.getString(cursor.getColumnIndexOrThrow(this.orderTotalPrice));

                // Create a new order without the related products
                Orders order = new Orders(orderNumber, orderStatus, orderTotalPrice, userId);

                // Add the order to the list
                userOrders.add(order);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return userOrders;
    }
}
