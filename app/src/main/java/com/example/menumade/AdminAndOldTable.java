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

    private EditText tableNameEditText, tableNumberEditText, tableCapacityEditText, adminPasswordEditText;
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
        tableNameEditText = findViewById(R.id.AD_ed);
        tableNumberEditText = findViewById(R.id.pass_ed);
        tableCapacityEditText = findViewById(R.id.table_capacity);
        adminPasswordEditText = findViewById(R.id.ed_password);  // Admin password field

        // Add Table button logic
        Button addTableButton = findViewById(R.id.BTN_Add_Table);
        addTableButton.setOnClickListener(v -> {
            String tableName = tableNameEditText.getText().toString();
            String tableNumberStr = tableNumberEditText.getText().toString();
            String tableCapacityStr = tableCapacityEditText.getText().toString();
            String adminPassword = adminPasswordEditText.getText().toString();

            if (!tableName.isEmpty() && !tableNumberStr.isEmpty() && !tableCapacityStr.isEmpty() && !adminPassword.isEmpty()) {
                if (databaseHelper.checkAdmin("Admin", adminPassword)) {
                    try {
                        int tableNumber = Integer.parseInt(tableNumberStr); // Convert tableNumber to int
                        int tableCapacity = Integer.parseInt(tableCapacityStr); // Convert tableCapacity to int

                        // Add table to database
                        databaseHelper.addTable(tableName, tableNumber, tableCapacity);
                        Toast.makeText(AdminAndOldTable.this, "Table Added Successfully", Toast.LENGTH_SHORT).show();

                        // Clear input fields
                        tableNameEditText.setText("");
                        tableNumberEditText.setText("");
                        tableCapacityEditText.setText("");
                        adminPasswordEditText.setText("");
                    } catch (NumberFormatException e) {
                        Toast.makeText(AdminAndOldTable.this, "Table Number and Capacity must be numeric", Toast.LENGTH_SHORT).show();
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
