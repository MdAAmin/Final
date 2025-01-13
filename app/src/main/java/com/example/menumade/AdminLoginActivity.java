package com.example.menumade;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AdminLoginActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        databaseHelper = new DatabaseHelper(this);

        // Initialize UI components
        EditText adminNameEditText = findViewById(R.id.et_admin_name);
        EditText passwordEditText = findViewById(R.id.et_admin_password);
        Button loginButton = findViewById(R.id.btn_login); // Single Login Button

        // Login Button click listener
        loginButton.setOnClickListener(v -> {
            String adminName = adminNameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            // Validate inputs
            if (adminName.isEmpty()) {
                adminNameEditText.setError("Admin Name is required.");
                adminNameEditText.requestFocus();
            } else if (password.isEmpty()) {
                passwordEditText.setError("Password is required.");
                passwordEditText.requestFocus();
            } else {
                // Check if the credentials match any user in the database
                boolean isValidUser = databaseHelper.checkUser(adminName, password);

                if (isValidUser) {
                    // If the credentials are valid, proceed to Admin Home
                    Toast.makeText(AdminLoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AdminLoginActivity.this, AdminHome.class);
                    startActivity(intent);
                    finish(); // Close the login activity
                } else {
                    // If the credentials are invalid
                    Toast.makeText(AdminLoginActivity.this, "Invalid credentials. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
