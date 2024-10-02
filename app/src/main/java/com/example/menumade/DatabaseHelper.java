package com.example.menumade;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MenuMade.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_ADMIN = "admin";
    private static final String TABLE_TABLES = "tables";

    private static final String COLUMN_ADMIN_ID = "admin_id";
    private static final String COLUMN_ADMIN_NAME = "adminname";
    private static final String COLUMN_PASSWORD = "password";

    private static final String COLUMN_TABLE_ID = "table_id";
    private static final String COLUMN_TABLE_NAME = "table_name";
    private static final String COLUMN_TABLE_NUMBER = "table_number";
    private static final String COLUMN_TABLE_CAPACITY = "table_capacity";

    private static final String CREATE_ADMIN_TABLE = "CREATE TABLE " + TABLE_ADMIN + " ("
            + COLUMN_ADMIN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_ADMIN_NAME + " TEXT NOT NULL, "
            + COLUMN_PASSWORD + " TEXT NOT NULL);";

    private static final String CREATE_TABLES_TABLE = "CREATE TABLE " + TABLE_TABLES + " ("
            + COLUMN_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_TABLE_NAME + " TEXT NOT NULL, "
            + COLUMN_TABLE_NUMBER + " TEXT NOT NULL, "
            + COLUMN_TABLE_CAPACITY + " INTEGER NOT NULL);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ADMIN_TABLE);
        db.execSQL(CREATE_TABLES_TABLE);

        ContentValues defaultAdmin = new ContentValues();
        defaultAdmin.put(COLUMN_ADMIN_NAME, "Admin");
        defaultAdmin.put(COLUMN_PASSWORD, "11");
        db.insert(TABLE_ADMIN, null, defaultAdmin);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 1) {

            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADMIN);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TABLES);
            onCreate(db);
        }
    }

    public boolean checkAdmin(String adminname, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ADMIN, null, COLUMN_ADMIN_NAME + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{adminname, password}, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Method to add a new admin
    public void addAdmin(String adminname, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ADMIN_NAME, adminname);
        values.put(COLUMN_PASSWORD, password);
        db.insert(TABLE_ADMIN, null, values);
    }

    // Method to add a new table
    public boolean addTable(String tableName, String tableNumber, int tableCapacity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TABLE_NAME, tableName);
        values.put(COLUMN_TABLE_NUMBER, tableNumber);
        values.put(COLUMN_TABLE_CAPACITY, tableCapacity);

        long result = db.insert(TABLE_TABLES, null, values);
        return result != -1;
    }

    public Cursor getAllTables() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_TABLES, null, null, null, null, null, null);
    }
}
