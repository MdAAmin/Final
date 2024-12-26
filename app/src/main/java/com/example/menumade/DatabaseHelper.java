package com.example.menumade;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Info
    public static final String DATABASE_NAME = "MenuMade.db";
    public static final int DATABASE_VERSION = 3; // Incremented version

    // Table Names
    public static final String TABLE_ADMIN = "admin";
    public static final String TABLE_PRODUCTS = "products";
    public static final String TABLE_TABLES = "tables"; // New table for storing table info

    // Admin Table Columns
    public static final String COLUMN_ADMIN_ID = "admin_id";
    public static final String COLUMN_ADMIN_NAME = "admin_name";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PHONE = "phone";

    // Products Table Columns
    public static final String COLUMN_PRODUCT_ID = "product_id";
    public static final String COLUMN_PRODUCT_NAME = "product_name";
    public static final String COLUMN_PRODUCT_PRICE = "product_price";
    public static final String COLUMN_PRODUCT_QUANTITY = "product_quantity"; // Added quantity
    public static final String COLUMN_PRODUCT_IMAGE = "product_image"; // Optional, to store images as blobs

    // Tables Table Columns (New)
    public static final String COLUMN_TABLE_ID = "table_id"; // Primary Key for Table
    public static final String COLUMN_TABLE_NAME = "table_name";
    public static final String COLUMN_TABLE_NUMBER = "table_number";
    public static final String COLUMN_TABLE_CAPACITY = "table_capacity";

    // Create Admin Table
    private static final String CREATE_ADMIN_TABLE = "CREATE TABLE " + TABLE_ADMIN + " (" +
            COLUMN_ADMIN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_ADMIN_NAME + " TEXT NOT NULL UNIQUE, " +
            COLUMN_PASSWORD + " TEXT NOT NULL, " +
            COLUMN_EMAIL + " TEXT, " +
            COLUMN_PHONE + " TEXT);";

    // Create Products Table
    private static final String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_PRODUCTS + " (" +
            COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_PRODUCT_NAME + " TEXT NOT NULL UNIQUE, " +
            COLUMN_PRODUCT_PRICE + " REAL NOT NULL, " +
            COLUMN_PRODUCT_QUANTITY + " INTEGER NOT NULL, " +
            COLUMN_PRODUCT_IMAGE + " BLOB);";

    // Create Tables Table (New)
    private static final String CREATE_TABLES_TABLE = "CREATE TABLE " + TABLE_TABLES + " (" +
            COLUMN_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_TABLE_NUMBER + " INTEGER NOT NULL UNIQUE);";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ADMIN_TABLE);
        db.execSQL(CREATE_PRODUCTS_TABLE);
        db.execSQL(CREATE_TABLES_TABLE); // Create new tables table

        // Insert default admin
        ContentValues defaultAdmin = new ContentValues();
        defaultAdmin.put(COLUMN_ADMIN_NAME, "Admin");
        defaultAdmin.put(COLUMN_PASSWORD, "12345Aa@");
        db.insert(TABLE_ADMIN, null, defaultAdmin);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_ADMIN + " ADD COLUMN " + COLUMN_EMAIL + " TEXT;");
            db.execSQL("ALTER TABLE " + TABLE_ADMIN + " ADD COLUMN " + COLUMN_PHONE + " TEXT;");
        }
        if (oldVersion < 3) {
            db.execSQL(CREATE_TABLES_TABLE); // Add new table creation for version 3
        }
    }

    // Check Admin Credentials
    public boolean checkAdmin(String adminName, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        boolean result = false;
        try {
            cursor = db.query(TABLE_ADMIN,
                    null,
                    COLUMN_ADMIN_NAME + "=? AND " + COLUMN_PASSWORD + "=?",
                    new String[]{adminName, password}, null, null, null);
            result = cursor != null && cursor.getCount() > 0;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return result;
    }

    // Register new Admin
    public boolean registerAdmin(String adminName, String password, String email, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_ADMIN,
                    new String[]{COLUMN_ADMIN_NAME},
                    COLUMN_ADMIN_NAME + "=?",
                    new String[]{adminName},
                    null, null, null);

            if (cursor != null && cursor.getCount() > 0) {
                return false; // Admin already exists
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        ContentValues values = new ContentValues();
        values.put(COLUMN_ADMIN_NAME, adminName);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PHONE, phone);

        long result = db.insert(TABLE_ADMIN, null, values);
        return result != -1;
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
    public Cursor getAllOrders() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT product_id AS _id, product_name, product_price, product_quantity FROM " + TABLE_PRODUCTS + " WHERE product_quantity > 0", null);
    }

    public void insertOrder(String productName, double productPrice, int productQuantity) {
    }
}
