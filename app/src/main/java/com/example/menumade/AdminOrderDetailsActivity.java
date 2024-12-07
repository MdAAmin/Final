package com.example.menumade;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AdminOrderDetailsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewOrder;
    private DatabaseHelper databaseHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order_details);

        // Initialize views and database helper
        recyclerViewOrder = findViewById(R.id.recyclerViewOrders); // Correct reference to the RecyclerView
        databaseHelper = new DatabaseHelper(this);

        // Set RecyclerView properties
        recyclerViewOrder.setLayoutManager(new LinearLayoutManager(this));

        // Display orders when the activity is created
        displayOrders();

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(AdminOrderDetailsActivity.this, AdminHome.class);
            startActivity(intent);
        });
    }

    private void displayOrders() {
        // Fetch all orders from the database
        Cursor cursor = databaseHelper.getAllOrders();

        // Check if the cursor is valid and has data
        if (cursor != null && cursor.getCount() > 0) {
            // Create and set the adapter to display orders in RecyclerView
            ViewOrderAdapter adapter = new ViewOrderAdapter(this, cursor);
            recyclerViewOrder.setAdapter(adapter);
        } else {
            // Show a message if no orders are found
            Toast.makeText(this, "No orders available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh the displayed orders when the activity is resumed
        displayOrders();
    }
}
