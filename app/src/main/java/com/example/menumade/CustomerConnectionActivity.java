package com.example.menumade;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CustomerConnectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_connection);

        Button btnProductDisplay = findViewById(R.id.BTN_Product_Display);
        Button btnPersonalizedRecommendations = findViewById(R.id.BTN_Personalized_Recommendations);
        Button btnPayment = findViewById(R.id.Payment);


        btnProductDisplay.setOnClickListener(v -> {
            Toast.makeText(CustomerConnectionActivity.this, "Navigating to Product Display Activity", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(CustomerConnectionActivity.this, ProductsDisplay.class));
        });


        btnPayment.setOnClickListener(v -> {
            Toast.makeText(CustomerConnectionActivity.this, "Navigating to Payment Activity", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(CustomerConnectionActivity.this, Payment.class));
        });
    }
}
