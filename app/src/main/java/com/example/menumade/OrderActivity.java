package com.example.menumade;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class OrderActivity extends AppCompatActivity {

    private EditText tableNumberEditText;
    private EditText productNameEditText;
    private EditText productPriceEditText;
    private EditText productQuantityEditText;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        // Initialize UI elements
        tableNumberEditText = findViewById(R.id.tableNumberEditText);
        productNameEditText = findViewById(R.id.productNameEditText);
        productPriceEditText = findViewById(R.id.productPriceEditText);
        productQuantityEditText = findViewById(R.id.productQuantityEditText);
        Button placeOrderButton = findViewById(R.id.placeOrderButton);

        // Back button setup
        Button buttonBack = findViewById(R.id.btn_back8);
        buttonBack.setOnClickListener(v -> {
            Intent intent = new Intent(OrderActivity.this, CustomerConnectionActivity.class);
            startActivity(intent);
        });

        // Initialize DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Get intent extras
        Intent intent = getIntent();
        if (intent != null) {
            int tableNumber = intent.getIntExtra("tableNumber", -1); // Default -1 if no table number provided
            String productName = intent.getStringExtra("productName");
            double productPrice = intent.getDoubleExtra("productPrice", 0.0);
            int productQuantity = intent.getIntExtra("productQuantity", 0);

            // Set values to EditTexts
            if (tableNumber != -1) {
                tableNumberEditText.setText(String.valueOf(tableNumber));
                tableNumberEditText.setEnabled(false); // Make it read-only
            }
            productNameEditText.setText(productName);
            productPriceEditText.setText(String.valueOf(productPrice));
            productQuantityEditText.setText(String.valueOf(productQuantity));

            // Disable productName and productPrice fields (display only)
            productNameEditText.setEnabled(false);
            productPriceEditText.setEnabled(false);
        }

        // Set click listener for the place order button
        placeOrderButton.setOnClickListener(v -> placeOrder());
    }

    private void placeOrder() {
        String tableNumberStr = tableNumberEditText.getText().toString();
        String productName = productNameEditText.getText().toString();
        String productPriceStr = productPriceEditText.getText().toString();
        String quantityStr = productQuantityEditText.getText().toString();

        // Validate input
        if (tableNumberStr.isEmpty() || productName.isEmpty() || productPriceStr.isEmpty() || quantityStr.isEmpty()) {
            Toast.makeText(this, "Please enter valid order details", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int tableNumber = Integer.parseInt(tableNumberStr);
            double productPrice = Double.parseDouble(productPriceStr);
            int productQuantity = Integer.parseInt(quantityStr);

            // Validate input values
            if (tableNumber <= 0 || productPrice <= 0 || productQuantity <= 0) {
                Toast.makeText(this, "Values must be greater than zero", Toast.LENGTH_SHORT).show();
                return;
            }

            // Insert order into the database
            boolean isInserted = databaseHelper.insertOrder(productName, productPrice, productQuantity, tableNumber);

            if (isInserted) {
                // Show success message
                Toast.makeText(this, "Order placed successfully", Toast.LENGTH_SHORT).show();

                // Clear input fields
                tableNumberEditText.setText("");
                productNameEditText.setText("");
                productPriceEditText.setText("");
                productQuantityEditText.setText("");

            } else {
                Toast.makeText(this, "Error placing order", Toast.LENGTH_SHORT).show();
            }

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numbers", Toast.LENGTH_SHORT).show();
        }
    }
}
