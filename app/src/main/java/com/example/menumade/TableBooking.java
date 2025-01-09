package com.example.menumade;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class TableBooking extends AppCompatActivity {

    private Spinner spinnerTableNumbers;
    private Button btnProceed;
    private DatabaseHelper databaseHelper;
    private ArrayList<Integer> tableNumbers; // To hold table numbers

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_table_booking);

        // Initialize DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Find views
        spinnerTableNumbers = findViewById(R.id.spinnerTableNumbers);
        btnProceed = findViewById(R.id.btnProceed);

        // Fetch table numbers from the database and populate the spinner
        populateSpinner();

        // Set OnClickListener for the Proceed button
        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedPosition = spinnerTableNumbers.getSelectedItemPosition();
                if (selectedPosition == AdapterView.INVALID_POSITION || tableNumbers.isEmpty()) {
                    Toast.makeText(TableBooking.this, "Please select a table before proceeding.", Toast.LENGTH_SHORT).show();
                } else {
                    int selectedTableNumber = tableNumbers.get(selectedPosition);
                    // Pass the selected table number to the next activity
                    Intent intent = new Intent(TableBooking.this, CustomerConnectionActivity.class);
                    intent.putExtra("TABLE_NUMBER", selectedTableNumber);
                    startActivity(intent);
                }
            }
        });
    }

    private void populateSpinner() {
        Cursor cursor = databaseHelper.getAllTables();
        tableNumbers = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int tableNumber = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_TABLE_NUMBER));
                tableNumbers.add(tableNumber);
            } while (cursor.moveToNext());
            cursor.close();
        }

        if (tableNumbers.isEmpty()) {
            tableNumbers.add(-1); // Add placeholder for no tables
        }

        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tableNumbers);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTableNumbers.setAdapter(adapter);

        if (tableNumbers.size() == 1 && tableNumbers.get(0) == -1) {
            spinnerTableNumbers.setSelection(AdapterView.INVALID_POSITION);
            Toast.makeText(this, "No tables available. Please add tables first.", Toast.LENGTH_LONG).show();
        }
    }
}
