package com.example.menumade;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class OrderActivity extends AppCompatActivity {

    private EditText productNameEditText;
    private EditText productPriceEditText;
    private EditText productQuantityEditText;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        productNameEditText = findViewById(R.id.productNameEditText);
        productPriceEditText = findViewById(R.id.productPriceEditText);
        productQuantityEditText = findViewById(R.id.productQuantityEditText);

        Button placeOrderButton = findViewById(R.id.placeOrderButton);
        Button btnBack = findViewById(R.id.btn_back8);

        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(OrderActivity.this, ProductListActivity.class);
            startActivity(intent);
        });

        // Initialize DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Handle order placement
        placeOrderButton.setOnClickListener(v -> placeOrder());
    }

    private void placeOrder() {
        String productName = productNameEditText.getText().toString();
        double productPrice = Double.parseDouble(productPriceEditText.getText().toString());
        String quantityStr = productQuantityEditText.getText().toString();

        if (productName.isEmpty() || productPrice <= 0 || quantityStr.isEmpty()) {
            customToast("Please enter valid order details");
            return;
        }

        int productQuantity;
        try {
            productQuantity = Integer.parseInt(quantityStr);
            if (productQuantity <= 0) {
                customToast("Quantity must be greater than zero");
                return;
            }
        } catch (NumberFormatException e) {
            customToast("Please enter a valid quantity");
            return;
        }

        // Insert order into the database
        databaseHelper.insertOrder(productName, productPrice, productQuantity);

        // Show success message
        customToast("Order placed successfully");

        productNameEditText.setText("");
        productPriceEditText.setText("");
        productQuantityEditText.setText("");
    }

    private void customToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
