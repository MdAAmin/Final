package com.example.menumade;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
public class AdminLogin extends AppCompatActivity {

    private EditText adminNameEditText, passwordEditText, confirmPasswordEditText, emailEditText, phoneEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        adminNameEditText = findViewById(R.id.ed_admin_name);
        passwordEditText = findViewById(R.id.ed_password);
        confirmPasswordEditText = findViewById(R.id.ed_confirm_password);
        emailEditText = findViewById(R.id.ed_email);
        phoneEditText = findViewById(R.id.ed_phone);

        Button loginButton = findViewById(R.id.btn_login);
        loginButton.setOnClickListener(v -> {
            String adminName = adminNameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String confirmPassword = confirmPasswordEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String phone = phoneEditText.getText().toString().trim();

            // Regex patterns
            String adminNamePattern = "^[a-zA-Z0-9]{3,20}$";  // Alphanumeric, 3-20 characters
            String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8}$";  // Exactly 8 chars with uppercase, lowercase, and digit
            String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";  // Standard email format
            String bangladeshPhonePattern = "^01[3-9]\\d{8}$";  // Starts with 01, valid for Bangladesh

            // Check if default admin is used
            boolean isDefaultAdmin = adminName.equals("Admin") && password.equals("12345Aa@");

            if (isDefaultAdmin) {
                // Hide email and phone fields if default admin is used
                emailEditText.setVisibility(View.GONE);
                phoneEditText.setVisibility(View.GONE);
            } else {
                // Show email and phone fields if not default admin
                emailEditText.setVisibility(View.VISIBLE);
                phoneEditText.setVisibility(View.VISIBLE);
            }

            // Validate admin name
            if (!adminName.matches(adminNamePattern)) {
                adminNameEditText.setError("Invalid Admin Name. Use 3-20 alphanumeric characters.");
                adminNameEditText.requestFocus();
                return;
            }

            // Validate password
            if (!password.matches(passwordPattern)) {
                passwordEditText.setError("Password must be exactly 8 characters with uppercase, lowercase, and a digit.");
                passwordEditText.requestFocus();
                return;
            }

            // Confirm password match
            if (!password.equals(confirmPassword)) {
                confirmPasswordEditText.setError("Passwords do not match.");
                confirmPasswordEditText.requestFocus();
                return;
            }

            // Validate email and phone only if not default admin
            if (!isDefaultAdmin) {
                if (email.isEmpty() || !email.matches(emailPattern)) {
                    emailEditText.setError("Invalid email format.");
                    emailEditText.requestFocus();
                    return;
                }

                if (phone.isEmpty() || !phone.matches(bangladeshPhonePattern)) {
                    phoneEditText.setError("Invalid phone number. Enter a valid Bangladeshi phone number starting with 01.");
                    phoneEditText.requestFocus();
                    return;
                }
            }

            // Check if the admin already exists in the database (for login)
            if (databaseHelper.checkAdmin(adminName, password)) {
                // Proceed to AdminHome activity if login is successful
                Intent intent = new Intent(AdminLogin.this, AdminHome.class);
                startActivity(intent);
                finish();
            } else {
                // If admin doesn't exist, save as new admin
                if (!isDefaultAdmin) {
                    // Save the new admin details to the database (for registration)
                    boolean isAdminCreated = databaseHelper.insertAdmin(adminName, password);

                    if (isAdminCreated) {
                        // Successfully registered, now login the user
                        Intent intent = new Intent(AdminLogin.this, AdminHome.class);
                        startActivity(intent);
                        finish();
                    } else {
                        adminNameEditText.setError("Failed to register new Admin.");
                        adminNameEditText.requestFocus();
                    }
                } else {
                    adminNameEditText.setError("Invalid Admin Name or Password");
                    adminNameEditText.requestFocus();
                }
            }
        });
    }
}

