package com.example.menumade;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.regex.Pattern;

public class AdminLoginActivity extends AppCompatActivity {

    // Regex patterns
    private static final String NAME_REGEX = "^[a-zA-Z0-9]{3,20}$"; // Alphanumeric, 3-20 characters
    private static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$"; // Min 8 chars, uppercase, lowercase, digit

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        // Edge-to-edge handling
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UI components
        EditText adminNameEditText = findViewById(R.id.et_admin_name);
        EditText passwordEditText = findViewById(R.id.et_admin_password);
        Button loginButton = findViewById(R.id.btn_login); // Renamed the button to 'loginButton'

        // Set button click listener
        loginButton.setOnClickListener(v -> {
            String adminName = adminNameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            // Validate inputs for login
            if (adminName.isEmpty()) {
                adminNameEditText.setError("Admin Name is required.");
                adminNameEditText.requestFocus();
            } else if (!Pattern.matches(NAME_REGEX, adminName)) {
                adminNameEditText.setError("Name must be 3-20 alphanumeric characters.");
                adminNameEditText.requestFocus();
            } else if (password.isEmpty()) {
                passwordEditText.setError("Password is required.");
                passwordEditText.requestFocus();
            } else if (!Pattern.matches(PASSWORD_REGEX, password)) {
                passwordEditText.setError("Password must be at least 8 characters, including uppercase, lowercase, and a digit.");
                passwordEditText.requestFocus();
            } else {
                // Simulate login success (in a real case, you would check credentials from a database)
                Toast.makeText(AdminLoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                // Redirect to Admin Home after login
                Intent intent = new Intent(AdminLoginActivity.this, AdminHome.class);
                startActivity(intent);

                finish(); // Close the login activity
            }
        });
    }
}
