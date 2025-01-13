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



        btnProductDisplay.setOnClickListener(v -> {
            Toast.makeText(CustomerConnectionActivity.this, "Navigating to Product Display Activity", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(CustomerConnectionActivity.this, ProductsDisplay.class));
        });

        btnPersonalizedRecommendations.setOnClickListener(v -> {
            Toast.makeText(CustomerConnectionActivity.this, "Navigating to Personalized Recommendations Activity", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(CustomerConnectionActivity.this, PersonalizedRecommendationsActivity.class));
        });


    }
}
