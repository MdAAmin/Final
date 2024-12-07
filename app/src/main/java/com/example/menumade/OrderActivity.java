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

        // Initialize DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Get the product details passed from ProductsDisplay activity
        Intent intent = getIntent();
        String productName = intent.getStringExtra("productName");
        double productPrice = intent.getDoubleExtra("productPrice", 0);
        int productQuantity = intent.getIntExtra("productQuantity", 0);

        productNameEditText.setText(productName);
        productPriceEditText.setText(String.valueOf(productPrice));
        productQuantityEditText.setText(String.valueOf(productQuantity));

        placeOrderButton.setOnClickListener(v -> placeOrder());

        btnBack.setOnClickListener(v -> {
            Intent backIntent = new Intent(OrderActivity.this, ProductsDisplay.class);
            startActivity(backIntent);
        });
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
