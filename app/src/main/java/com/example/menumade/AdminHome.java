package com.example.menumade;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AdminHome extends AppCompatActivity {

    private Button btnInsertProduct;
    private Button btnViewProduct;
    private Button btnUpdateProduct;
    private Button btnDeleteProduct;
    private Button btnViewOrders;
    private Button btnSpecializedItem;  // Corrected button ID
    private Button btnTable;
    private Button btnBack;
    private TextView tvTableList;
    private ListView lvTableList; // ListView to show tables
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        // Initialize views
        btnSpecializedItem = findViewById(R.id.btn_specialized_item);  // Corrected ID
        btnInsertProduct = findViewById(R.id.btn_insert_product);
        btnViewProduct = findViewById(R.id.btn_view_product);
        btnUpdateProduct = findViewById(R.id.btn_update_product);
        btnDeleteProduct = findViewById(R.id.btn_delete_product);
        btnViewOrders = findViewById(R.id.btn_view_orders);
        btnTable = findViewById(R.id.btn_table);
        btnBack = findViewById(R.id.btn_back4);
        lvTableList = findViewById(R.id.lv_table_list);  // ListView
        tvTableList = findViewById(R.id.tv_table_list);

        // Initialize DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Set listeners for buttons
        btnInsertProduct.setOnClickListener(v -> startActivity(new Intent(AdminHome.this, InsertTableItemActivity.class)));
        btnViewProduct.setOnClickListener(v -> startActivity(new Intent(AdminHome.this, ViewTableActivity.class)));
        btnUpdateProduct.setOnClickListener(v -> startActivity(new Intent(AdminHome.this, UpdateTableItemActivity.class)));
        btnDeleteProduct.setOnClickListener(v -> startActivity(new Intent(AdminHome.this, DeleteTableItemActivity.class)));
        btnViewOrders.setOnClickListener(v -> startActivity(new Intent(AdminHome.this, AdminOrderDetailsActivity.class)));
        btnBack.setOnClickListener(v -> startActivity(new Intent(AdminHome.this, AdminAndOldTable.class)));

        // Set click listener for Specialized Item button
        btnSpecializedItem.setOnClickListener(v -> startActivity(new Intent(AdminHome.this, AdminFoodName.class)));

        // Set click listener for Num of Table button
        btnTable.setOnClickListener(v -> showTables());
    }

    // Method to display all tables in a ListView
    @SuppressLint("SetTextI18n")
    private void showTables() {
        // Hide the
        btnSpecializedItem.setVisibility(View.GONE);
        btnTable.setVisibility(View.GONE);

        btnInsertProduct.setVisibility(View.GONE);
        btnViewProduct.setVisibility(View.GONE);
        btnUpdateProduct.setVisibility(View.GONE);
        btnDeleteProduct.setVisibility(View.GONE);
        btnViewOrders.setVisibility(View.GONE);
        btnBack.setVisibility(View.GONE);

        // Get all tables from the database
        Cursor cursor = databaseHelper.getAllTables();

        if (cursor != null && cursor.getCount() > 0) {
            TableAdapter tableAdapter = new TableAdapter(this, cursor);
            lvTableList.setAdapter(tableAdapter);  // Set the custom adapter
            tvTableList.setVisibility(View.GONE);  // Hide TextView since we are using ListView now
            lvTableList.setVisibility(View.VISIBLE);  // Show the ListView
        } else {
            tvTableList.setText("No tables in the database.");
            tvTableList.setVisibility(View.VISIBLE);
            lvTableList.setVisibility(View.GONE);  // Hide ListView if no tables are found
        }
    }
}
