package com.example.menumade;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Pattern;

public class AdminSignUp extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private EditText adminNameEditText, passwordEditText, confirmPasswordEditText, emailEditText, phoneEditText, defaultPasswordEditText;

    // Fixed admin password
    private String defaultPassword = "12345Aa@";

    // Regex patterns for validation
    private static final String NAME_REGEX = "^[a-zA-Z0-9]{3,20}$"; // Alphanumeric, 3-20 characters
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"; // Standard email format
    private static final String PHONE_REGEX = "^01[3-9]\\d{8}$"; // Valid Bangladeshi phone numbers

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_signup);

        databaseHelper = new DatabaseHelper(this);

        // Initialize EditText fields
        adminNameEditText = findViewById(R.id.ed_admin_name);
        passwordEditText = findViewById(R.id.ed_password);
        confirmPasswordEditText = findViewById(R.id.ed_confirm_password);
        emailEditText = findViewById(R.id.ed_email);
        phoneEditText = findViewById(R.id.ed_phone);
        defaultPasswordEditText = findViewById(R.id.ed_default_password);

        // Initialize Buttons
        Button signUpButton = findViewById(R.id.btn_signup);
        Button loginButton = findViewById(R.id.btn_login2); // Assuming btn_login2 is the Login button
        Button checkPasswordButton = findViewById(R.id.btn_check_password);

        // Initially, the password fields are hidden
        defaultPasswordEditText.setVisibility(EditText.GONE);
        checkPasswordButton.setVisibility(Button.GONE);

        // SignUp button action
        signUpButton.setOnClickListener(v -> {
            validateForm();

            // Hide other fields and buttons
            adminNameEditText.setVisibility(EditText.GONE);
            passwordEditText.setVisibility(EditText.GONE);
            confirmPasswordEditText.setVisibility(EditText.GONE);
            emailEditText.setVisibility(EditText.GONE);
            phoneEditText.setVisibility(EditText.GONE);
            signUpButton.setVisibility(Button.GONE);
            loginButton.setVisibility(Button.GONE);

            // Show the default password EditText and check button
            defaultPasswordEditText.setVisibility(EditText.VISIBLE);
            checkPasswordButton.setVisibility(Button.VISIBLE);
        });

        // Check Password button action
        checkPasswordButton.setOnClickListener(v -> {
            String enteredPassword = defaultPasswordEditText.getText().toString().trim();
            if (enteredPassword.equals(defaultPassword)) {
                // Add user to the database after successful validation
                String adminName = adminNameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String phone = phoneEditText.getText().toString().trim();

                // Add the user to the database
                if (databaseHelper.addUser(adminName, password, email, phone)) {
                    // Navigate to Admin Home
                    Intent intent = new Intent(AdminSignUp.this, AdminHome.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(AdminSignUp.this, "Error adding user. Username or email might be taken.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(AdminSignUp.this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
            }
        });

        // Login button action
        loginButton.setOnClickListener(v -> {
            // Navigate to AdminLogin activity
            Intent intent = new Intent(AdminSignUp.this, AdminLoginActivity.class);
            startActivity(intent);
            finish(); // Close the current activity
        });
    }

    private void validateForm() {
        String adminName = adminNameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();

        if (!Pattern.matches(NAME_REGEX, adminName)) {
            adminNameEditText.setError("Invalid admin name. (3-20 characters)");
            return;
        }

        if (!Pattern.matches(EMAIL_REGEX, email)) {
            emailEditText.setError("Invalid email address.");
            return;
        }

        if (!Pattern.matches(PHONE_REGEX, phone)) {
            phoneEditText.setError("Invalid phone number.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            confirmPasswordEditText.setError("Passwords do not match.");
            return;
        }

        // Proceed with saving the data to the database if validation passes.
    }
}
