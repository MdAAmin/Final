package com.example.menumade;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class PersonalizedRecommendationsActivity extends AppCompatActivity {

    private TextView recommendationsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_personalized_recommendations);

        recommendationsTextView = findViewById(R.id.recommendationsTextView);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Button backButton = findViewById(R.id.btn_back10);

        // Load and display recommendations
        loadRecommendations();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to UserConnectionActivity
                Intent intent1 = new Intent(PersonalizedRecommendationsActivity.this, CustomerConnectionActivity.class);
                startActivity(intent1);
            }
        });
    }

    private void loadRecommendations() {
        // Sample recommendations
        String[] recommendations = {
                "Try our new Spicy Chicken Burger!",
                "How about a refreshing Mango Smoothie?",
                "Have you tasted our Chocolate Lava Cake yet?",
                "Our Caesar Salad is a must-try!",
                "Enjoy a classic Margherita Pizza!"
        };

        StringBuilder recommendationsBuilder = new StringBuilder();
        for (String recommendation : recommendations) {
            recommendationsBuilder.append(recommendation).append("\n\n");
        }

        recommendationsTextView.setText(recommendationsBuilder.toString());
    }
}

