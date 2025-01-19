package com.example.menumade;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class PersonalizedRecommendationsActivity extends AppCompatActivity {

    private TextView recommendationsTextView;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalized_recommendations);

        recommendationsTextView = findViewById(R.id.recommendationsTextView);
        backButton = findViewById(R.id.btn_back10);

        // Access the static food list from AdminFoodName activity
        ArrayList<String> foodList = AdminFoodName.foodList;

        // Build a single string from the list of food names
        if (foodList != null && !foodList.isEmpty()) {
            StringBuilder foodDisplay = new StringBuilder();
            for (String food : foodList) {
                foodDisplay.append(food).append("\n\n");
            }
            recommendationsTextView.setText(foodDisplay.toString());
        } else {
            recommendationsTextView.setText("No food recommendations available.");
        }

        // Back button to navigate back to AdminFoodNameActivity
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonalizedRecommendationsActivity.this, CustomerConnectionActivity.class);
                startActivity(intent);
            }
        });
    }
}
