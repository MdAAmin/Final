package com.example.menumade;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class InsertTableActivity extends AppCompatActivity {

    private EditText tableNameEditText;
    private EditText tableNumberEditText;
    private EditText tableCapacityEditText;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_table);

        // Initialization of UI components
        tableNameEditText = findViewById(R.id.et_table_name);
        tableNumberEditText = findViewById(R.id.et_table_number);
        tableCapacityEditText = findViewById(R.id.et_table_capacity);
        Button insertTableButton = findViewById(R.id.btn_insert_table);
        Button goBackButton = findViewById(R.id.back_button);

        databaseHelper = new DatabaseHelper(this);

        // Setting up button listeners
        insertTableButton.setOnClickListener(view -> insertTable());

        // Navigation back to AdminHome
        goBackButton.setOnClickListener(v -> {
            Intent intent = new Intent(InsertTableActivity.this, AdminHome.class);
            startActivity(intent);
        });
    }

    private void insertTable() {
        String tableName = tableNameEditText.getText().toString();
        String tableNumber = tableNumberEditText.getText().toString();
        String tableCapacityText = tableCapacityEditText.getText().toString();

        if (tableName.isEmpty() || tableNumber.isEmpty() || tableCapacityText.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int tableCapacity = Integer.parseInt(tableCapacityText);

        // Insert table details into the database
        boolean isInserted = databaseHelper.addTable(tableName, tableNumber, tableCapacity);

        if (isInserted) {
            Toast.makeText(this, "Table inserted successfully", Toast.LENGTH_SHORT).show();
            clearFields();
        } else {
            Toast.makeText(this, "Failed to insert table", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearFields() {
        tableNameEditText.setText("");
        tableNumberEditText.setText("");
        tableCapacityEditText.setText("");
    }
}
