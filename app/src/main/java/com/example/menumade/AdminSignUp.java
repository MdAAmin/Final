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
    private EditText adminNameEditText, passwordEditText, confirmPasswordEditText, emailEditText, phoneEditText;

    // Regex patterns for validation
    private static final String NAME_REGEX = "^[a-zA-Z0-9]{3,20}$"; // Alphanumeric, 3-20 characters
    private static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$"; // Min 8 characters, uppercase, lowercase, and digit
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"; // Standard email format
    private static final String PHONE_REGEX = "^01[3-9]\\d{8}$"; // Valid Bangladeshi phone numbers

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_signup);
        Button loginButton = findViewById(R.id.btn_login2);
        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(AdminSignUp.this, AdminLoginActivity.class);
            startActivity(intent);
        });


        databaseHelper = new DatabaseHelper(this);


        // Initialize EditText fields
        adminNameEditText = findViewById(R.id.ed_admin_name);
        passwordEditText = findViewById(R.id.ed_password);
        confirmPasswordEditText = findViewById(R.id.ed_confirm_password);
        emailEditText = findViewById(R.id.ed_email);
        phoneEditText = findViewById(R.id.ed_phone);

        // SignUp button
        Button signUpButton = findViewById(R.id.btn_signup);
        signUpButton.setOnClickListener(v -> validateForm());
    }

    private void validateForm() {
        String adminName = adminNameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();

        // Clear previous errors
        clearErrors();

        // Validate input for new admin
        if (!Pattern.matches(NAME_REGEX, adminName)) {
            adminNameEditText.setError("Invalid Admin Name. Use 3-20 alphanumeric characters.");
            adminNameEditText.requestFocus();
        } else if (!Pattern.matches(PASSWORD_REGEX, password)) {
            passwordEditText.setError("Password must be at least 8 characters, including uppercase, lowercase, and a digit.");
            passwordEditText.requestFocus();
        } else if (!password.equals(confirmPassword)) {
            confirmPasswordEditText.setError("Passwords do not match.");
            confirmPasswordEditText.requestFocus();
        } else if (!Pattern.matches(EMAIL_REGEX, email)) {
            emailEditText.setError("Invalid email format.");
            emailEditText.requestFocus();
        } else if (!Pattern.matches(PHONE_REGEX, phone)) {
            phoneEditText.setError("Invalid phone number. Must start with 01 and be 11 digits.");
            phoneEditText.requestFocus();
        } else {
            // Attempt to register the admin in the database
            boolean isAdminCreated = databaseHelper.registerAdmin(adminName, password, email, phone);
            if (isAdminCreated) {
                Toast.makeText(this, "Registration Successful! Logging in...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AdminSignUp.this, AdminHome.class);
                startActivity(intent);
                finish();
            } else {
                adminNameEditText.setError("Registration failed. Admin name might already exist.");
                adminNameEditText.requestFocus();
            }
        }
    }

    private void clearErrors() {
        adminNameEditText.setError(null);
        passwordEditText.setError(null);
        confirmPasswordEditText.setError(null);
        emailEditText.setError(null);
        phoneEditText.setError(null);
    }
}
