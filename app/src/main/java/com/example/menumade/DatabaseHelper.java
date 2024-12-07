package com.example.menumade;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Info
    public static final String DATABASE_NAME = "MenuMade.db";
    public static final int DATABASE_VERSION = 2;

    // Table Names
    public static final String TABLE_ADMIN = "admin";
    public static final String TABLE_PRODUCTS = "products";

    // Admin Table Columns
    public static final String COLUMN_ADMIN_ID = "admin_id";
    public static final String COLUMN_ADMIN_NAME = "admin_name";
    public static final String COLUMN_PASSWORD = "password";

    // Products Table Columns
    public static final String COLUMN_PRODUCT_ID = "product_id";
    public static final String COLUMN_PRODUCT_NAME = "product_name";
    public static final String COLUMN_PRODUCT_PRICE = "product_price";
    public static final String COLUMN_PRODUCT_QUANTITY = "product_quantity"; // Added quantity
    public static final String COLUMN_PRODUCT_IMAGE = "product_image";  // Optional, to store images as blobs

    // SQL Queries
    private static final String CREATE_ADMIN_TABLE = "CREATE TABLE " + TABLE_ADMIN + " (" +
            COLUMN_ADMIN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_ADMIN_NAME + " TEXT NOT NULL UNIQUE, " +
            COLUMN_PASSWORD + " TEXT NOT NULL);";

    private static final String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_PRODUCTS + " (" +
            COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_PRODUCT_NAME + " TEXT NOT NULL UNIQUE, " +
            COLUMN_PRODUCT_PRICE + " REAL NOT NULL, " +
            COLUMN_PRODUCT_QUANTITY + " INTEGER NOT NULL, " + // Added quantity
            COLUMN_PRODUCT_IMAGE + " BLOB);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ADMIN_TABLE);
        db.execSQL(CREATE_PRODUCTS_TABLE);

        // Insert default admin
        ContentValues defaultAdmin = new ContentValues();
        defaultAdmin.put(COLUMN_ADMIN_NAME, "Admin");
        defaultAdmin.put(COLUMN_PASSWORD, "12345Aa@");
        db.insert(TABLE_ADMIN, null, defaultAdmin);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Handle migration here if needed (e.g., renaming old tables or adding new columns)
            db.execSQL("ALTER TABLE products ADD COLUMN " + COLUMN_PRODUCT_QUANTITY + " INTEGER NOT NULL DEFAULT 0;");
        }
    }

    // Check Admin Credentials
    public boolean checkAdmin(String adminName, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ADMIN,
                null,
                COLUMN_ADMIN_NAME + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{adminName, password}, null, null, null);

        boolean result = cursor != null && cursor.getCount() > 0;
        if (cursor != null) {
            cursor.close();
        }
        return result;
    }
    // Insert a new admin into the database
    public boolean insertAdmin(String adminName, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ADMIN_NAME, adminName);
        values.put(COLUMN_PASSWORD, password);

        long result = db.insert(TABLE_ADMIN, null, values);
        return result != -1;  // Return true if insertion succeeded
    }

    // Insert a new product
    public boolean insertProduct(String productName, double productPrice, int productQuantity, byte[] productImage) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_NAME, productName);
        values.put(COLUMN_PRODUCT_PRICE, productPrice);
        values.put(COLUMN_PRODUCT_QUANTITY, productQuantity);  // Quantity added
        values.put(COLUMN_PRODUCT_IMAGE, productImage);

        long result = db.insert(TABLE_PRODUCTS, null, values);
        return result != -1;  // Return true if insertion succeeded
    }

    // Update an existing product
    public boolean updateProduct(int productId, String productName, double productPrice, int productQuantity, byte[] productImage) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_NAME, productName);
        values.put(COLUMN_PRODUCT_PRICE, productPrice);
        values.put(COLUMN_PRODUCT_QUANTITY, productQuantity);  // Quantity added
        values.put(COLUMN_PRODUCT_IMAGE, productImage);

        int rowsAffected = db.update(TABLE_PRODUCTS, values, COLUMN_PRODUCT_ID + "=?", new String[]{String.valueOf(productId)});
        return rowsAffected > 0;  // Return true if update succeeded
    }

    // Delete a product by name
    public boolean deleteProduct(String productName) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_PRODUCTS, COLUMN_PRODUCT_NAME + "=?", new String[]{productName});
        return rowsAffected > 0;  // Return true if deletion succeeded
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

    public void addTable(String tableName, int tableNumber, int tableCapacity) {
    }

    public void insertOrder(String productName, double productPrice, int productQuantity) {
    }

    // Get all products as orders
    public Cursor getAllOrders() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT product_id AS _id, product_name, product_price, product_quantity FROM " + TABLE_PRODUCTS + " WHERE product_quantity > 0", null);
    }


}

