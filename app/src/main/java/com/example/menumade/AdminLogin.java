package com.example.menumade;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AdminLogin extends AppCompatActivity {

    private EditText adminNameEditText, passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        adminNameEditText = findViewById(R.id.ed_admin_name);
        passwordEditText = findViewById(R.id.ed_password);

        Button loginButton = findViewById(R.id.btn_login);
        loginButton.setOnClickListener(v -> {
            String adminName = adminNameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            // Define regex patterns
            String adminNamePattern = "^[a-zA-Z0-9]{3,20}$";  // Alphanumeric, 3-20 characters
            String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{2,}$";  // At least 2 chars with uppercase, lowercase, and digit

            // Check if admin name and password match the patterns
            if (!adminName.matches(adminNamePattern)) {
                Toast.makeText(AdminLogin.this, "Invalid Admin Name. Use 3-20 alphanumeric characters.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.matches(passwordPattern)) {
                Toast.makeText(AdminLogin.this, "Password must be at least 8 characters, with uppercase, lowercase, and a digit.", Toast.LENGTH_SHORT).show();
                return;
            }

            // If validation passes, check credentials with database
            if (databaseHelper.checkAdmin(adminName, password)) {
                Intent intent = new Intent(AdminLogin.this, AdminAndOldTable.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(AdminLogin.this, "Invalid Admin Name or Password", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
