package com.example.menumade;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AdminAndOldTable extends AppCompatActivity {

    private EditText tableNumberEditText, adminPasswordEditText;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_and_old_table);

        // Inflate custom toast layout
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) View layout = inflater.inflate(R.layout.toaster, findViewById(R.id.go));

        // Custom toast setup
        Toast customToast = new Toast(getApplicationContext());
        customToast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        customToast.setDuration(Toast.LENGTH_SHORT);
        customToast.setView(layout);

        // Initialize DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Bind views
        tableNumberEditText = findViewById(R.id.pass_ed);
        adminPasswordEditText = findViewById(R.id.ed_password);  // Admin password field

        // Add Table button logic
        Button addTableButton = findViewById(R.id.BTN_Add_Table);
        addTableButton.setOnClickListener(v -> {
            String tableNumberStr = tableNumberEditText.getText().toString();
            String adminPassword = adminPasswordEditText.getText().toString();

            // Check if the inputs are valid
            if (!tableNumberStr.isEmpty() && !adminPassword.isEmpty()) {
                if (databaseHelper.checkAdmin("Admin", adminPassword)) {
                    try {
                        int tableNumber = Integer.parseInt(tableNumberStr); // Convert tableNumber to int

                        // Add table to database (only storing table number)
                        boolean isInserted = databaseHelper.insertTable(tableNumber);
                        if (isInserted) {
                            Toast.makeText(AdminAndOldTable.this, "Table Added Successfully", Toast.LENGTH_SHORT).show();

                            // Clear input fields
                            tableNumberEditText.setText("");
                            adminPasswordEditText.setText("");
                        } else {
                            Toast.makeText(AdminAndOldTable.this, "Failed to add table", Toast.LENGTH_SHORT).show();
                        }

                    } catch (NumberFormatException e) {
                        Toast.makeText(AdminAndOldTable.this, "Table Number must be numeric", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AdminAndOldTable.this, "Invalid Admin Password", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(AdminAndOldTable.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            }
        });

        // Admin button logic
        Button adminButton = findViewById(R.id.BTN_ADMIN);
        adminButton.setOnClickListener(v -> {
            customToast.show();
            Intent intent = new Intent(AdminAndOldTable.this, AdminHome.class);
            startActivity(intent);
        });

        // Back button logic
        Button backButton = findViewById(R.id.BTN_Back);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(AdminAndOldTable.this, MainActivity.class);
            startActivity(intent);
        });
    }
}
