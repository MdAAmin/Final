package com.example.menumade;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MenuMade.db";
    public static final int DATABASE_VERSION = 4;

    public static final String TABLE_ADMIN = "admin";
    public static final String TABLE_USER = "user";
    public static final String TABLE_PRODUCTS = "products";
    public static final String TABLE_TABLES = "tables";
    public static final String TABLE_ORDERS = "orders";  // Fixed table name

    public static final String COLUMN_ADMIN_ID = "admin_id";
    public static final String COLUMN_ADMIN_NAME = "admin_name";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PHONE = "phone";

    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_USER_NAME = "username";
    public static final String COLUMN_USER_PASSWORD = "password";
    public static final String COLUMN_USER_EMAIL = "email";
    public static final String COLUMN_USER_PHONE = "phone";


    public static final String COLUMN_PRODUCT_ID = "product_id";
    public static final String COLUMN_PRODUCT_NAME = "product_name";
    public static final String COLUMN_PRODUCT_PRICE = "product_price";
    public static final String COLUMN_PRODUCT_QUANTITY = "product_quantity";
    public static final String COLUMN_PRODUCT_IMAGE = "product_image";
    public static final String COLUMN_TABLE_ID = "table_id";
    public static final String COLUMN_TABLE_NUMBER = "table_number";

    // Orders Table Column Names
    public static final String COLUMN_ORDER_ID = "order_id"; // Added order ID
    public static final String COLUMN_ORDER_PRODUCT_NAME = "product_name";
    public static final String COLUMN_ORDER_PRICE = "o_price";
    public static final String COLUMN_ORDER_QUANTITY = "o_quantity";

    // Creating tables
    private static final String CREATE_ADMIN_TABLE = "CREATE TABLE " + TABLE_ADMIN + " (" +
            COLUMN_ADMIN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_ADMIN_NAME + " TEXT NOT NULL UNIQUE, " +
            COLUMN_PASSWORD + " TEXT NOT NULL, " +
            COLUMN_EMAIL + " TEXT, " +
            COLUMN_PHONE + " TEXT);";

    private static final String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + " (" +
            COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_USER_NAME + " TEXT NOT NULL UNIQUE, " +
            COLUMN_USER_PASSWORD + " TEXT NOT NULL, " +
            COLUMN_USER_EMAIL + " TEXT, " +
            COLUMN_USER_PHONE + " TEXT);";


    private static final String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_PRODUCTS + " (" +
            COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_PRODUCT_NAME + " TEXT NOT NULL UNIQUE, " +
            COLUMN_PRODUCT_PRICE + " REAL NOT NULL, " +
            COLUMN_PRODUCT_QUANTITY + " INTEGER NOT NULL, " +
            COLUMN_PRODUCT_IMAGE + " BLOB);";

    private static final String CREATE_TABLES_TABLE = "CREATE TABLE " + TABLE_TABLES + " (" +
            COLUMN_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_TABLE_NUMBER + " INTEGER NOT NULL UNIQUE);";

    // Orders table creation
    private static final String CREATE_ORDERS_TABLE = "CREATE TABLE " + TABLE_ORDERS + "(" +
            COLUMN_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_ORDER_PRODUCT_NAME + " TEXT, " +
            COLUMN_ORDER_PRICE + " REAL, " +
            COLUMN_ORDER_QUANTITY + " INTEGER, " +
            COLUMN_TABLE_NUMBER + " INTEGER, " + // Add this line
            "FOREIGN KEY (" + COLUMN_TABLE_NUMBER + ") REFERENCES " + TABLE_TABLES + "(" + COLUMN_TABLE_NUMBER + "));";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ADMIN_TABLE);
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_PRODUCTS_TABLE);
        db.execSQL(CREATE_TABLES_TABLE);
        db.execSQL(CREATE_ORDERS_TABLE);  // Fixed orders table creation
        addAdmin(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("PRAGMA foreign_keys=ON;");

        if (oldVersion < 4) {
            // If upgrading to version 4, recreate the orders table with foreign keys (if needed)
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
            db.execSQL(CREATE_ORDERS_TABLE);  // Recreate the orders table with the correct foreign key

            // Ensure the 'user' table exists if upgrading from an older version
            if (oldVersion < 3) {
                // Drop and recreate other tables if needed
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADMIN);
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_TABLES);
                onCreate(db); // Recreate all tables
            }
        }
    }





    private void addAdmin(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ADMIN_NAME, "Admin");
        values.put(COLUMN_PASSWORD, "12345Aa@");
        db.insert(TABLE_ADMIN, null, values);
    }


    // Add user, check credentials, update and delete operations (as in your original code)
    public boolean addUser(String username, String password, String email, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, username);
        values.put(COLUMN_USER_PASSWORD, password);
        values.put(COLUMN_USER_EMAIL, email);
        values.put(COLUMN_USER_PHONE, phone);

        long result = db.insert(TABLE_USER, null, values);
        return result != -1;
    }


    public boolean checkAdmin(String username, String password) {
        return checkCredentials(TABLE_ADMIN, username, password, COLUMN_ADMIN_NAME);
    }

    public boolean checkUser(String username, String password) {
        return checkCredentials(TABLE_USER, username, password, COLUMN_USER_NAME);
    }


    private boolean checkCredentials(String tableName, String username, String password, String columnName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {tableName.equals(TABLE_ADMIN) ? COLUMN_ADMIN_ID : COLUMN_USER_ID};
        String selection = columnName + "=? AND " + COLUMN_PASSWORD + "=?";
        String[] selectionArgs = {username, password};
        Cursor cursor = db.query(tableName, columns, selection, selectionArgs, null, null, null);

        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        return isValid;
    }




    // Insert a new product
    public boolean insertProduct(String productName, double productPrice, int productQuantity, byte[] productImage) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_NAME, productName);
        values.put(COLUMN_PRODUCT_PRICE, productPrice);
        values.put(COLUMN_PRODUCT_QUANTITY, productQuantity);
        values.put(COLUMN_PRODUCT_IMAGE, productImage);

        long result = db.insert(TABLE_PRODUCTS, null, values);
        return result != -1;
    }

    // Insert a new table
    public boolean insertTable(int tableNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TABLE_NUMBER, tableNumber);  // Only store table number

        long result = db.insert(TABLE_TABLES, null, values);
        return result != -1;  // Return true if insert is successful
    }

    // Get all tables (only fetching table numbers now)
    public Cursor getAllTables() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_TABLES, null);
    }

    // Get a specific table by table number
    public Cursor getTableByNumber(int tableNumber) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_TABLES,
                null,  // Retrieve all columns
                COLUMN_TABLE_NUMBER + "=?",
                new String[]{String.valueOf(tableNumber)}, null, null, null);
    }


    // Update an existing product
    public boolean updateProduct(int productId, String productName, double productPrice, int productQuantity, byte[] productImage) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_NAME, productName);
        values.put(COLUMN_PRODUCT_PRICE, productPrice);
        values.put(COLUMN_PRODUCT_QUANTITY, productQuantity);
        values.put(COLUMN_PRODUCT_IMAGE, productImage);

        int rowsAffected = db.update(TABLE_PRODUCTS, values, COLUMN_PRODUCT_ID + "=?", new String[]{String.valueOf(productId)});
        return rowsAffected > 0;
    }

    // Delete a product by name
    public boolean deleteProduct(String productName) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_PRODUCTS, COLUMN_PRODUCT_NAME + "=?", new String[]{productName});
        return rowsAffected > 0;
    }

    // Get all products
    public Cursor getAllProducts() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT product_id AS _id, product_name, product_price, product_quantity, product_image FROM " + TABLE_PRODUCTS, null);
    }

    // Get a specific product by name
    public Cursor getProductByName(String productName) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_PRODUCTS,
                null,
                COLUMN_PRODUCT_NAME + "=?",
                new String[]{productName}, null, null, null);
    }

    // Get product by ID
    public Cursor getProductById(int productId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_PRODUCTS,
                null,
                COLUMN_PRODUCT_ID + "=?",
                new String[]{String.valueOf(productId)}, null, null, null);
    }

    // Get all products as orders
    public boolean insertOrder(String productName, double price, int quantity, int tableNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ORDER_PRODUCT_NAME, productName);
        values.put(COLUMN_ORDER_PRICE, price);
        values.put(COLUMN_ORDER_QUANTITY, quantity);
        values.put(COLUMN_TABLE_NUMBER, tableNumber);

        // Insert order and check if insertion was successful
        long result = db.insert(TABLE_ORDERS, null, values);
        if (result == -1) {
            Log.e("Database Insert", "Failed to insert order");
        } else {
            Log.d("Database Insert", "Order inserted successfully");
        }
        return false;
    }




    public Cursor getAllOrders() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query("orders", new String[]{"order_id", "product_name", "o_price", "o_quantity", "table_number"},
                null, null, null, null, null);
    }


}
