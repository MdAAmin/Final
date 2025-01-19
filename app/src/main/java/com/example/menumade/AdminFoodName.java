package com.example.menumade;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class AdminFoodName extends AppCompatActivity {

    private EditText foodNameEditText;
    public static final ArrayList<String> foodList = new ArrayList<>(); // Static list for easy access between activities

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_food_name);

        foodNameEditText = findViewById(R.id.foodNameEditText);
        Button sendButton = findViewById(R.id.sendButton);
        Button clearButton = findViewById(R.id.clearButton);
        Button backButton = findViewById(R.id.backButton);

        // Button to send the food list to PersonalizedRecommendationsActivity
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String foodName = foodNameEditText.getText().toString().trim();

                // Add the new food name to the list
                if (!foodName.isEmpty()) {
                    foodList.add(foodName);
                    foodNameEditText.setText(""); // Clear the input field
                    Toast.makeText(AdminFoodName.this, "Food added!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AdminFoodName.this, "Enter a food name first!", Toast.LENGTH_SHORT).show();
                }

                // Navigate to PersonalizedRecommendationsActivity to display the food list
                Intent intent = new Intent(AdminFoodName.this, PersonalizedRecommendationsActivity.class);
                startActivity(intent);
            }
        });

        // Button to clear the food list
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodList.clear(); // Clear the list
                Toast.makeText(AdminFoodName.this, "Food list cleared!", Toast.LENGTH_SHORT).show();
            }
        });

        // Back button to navigate to Admin home
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminFoodName.this, AdminHome.class);
                startActivity(intent);
            }
        });
    }
}
