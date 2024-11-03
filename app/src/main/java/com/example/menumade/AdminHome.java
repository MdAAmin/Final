package com.example.menumade;

import android.content.Intent;   //from content package importing Intent class
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AdminHome extends AppCompatActivity {

    @Override     // Overriding the onCreate method to initialize the activity

    protected void onCreate(Bundle savedInstanceState) {  //  savedInstanceState previous state saved
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home); // show ui component

        Button btnInsertProduct = findViewById(R.id.btn_insert_product); // initialize Ui component
        Button btnViewProduct = findViewById(R.id.btn_view_product);
        Button btnUpdateProduct = findViewById(R.id.btn_update_product);
        Button btnDeleteProduct = findViewById(R.id.btn_delete_product);
        Button btnViewOrders = findViewById(R.id.btn_view_orders);

        btnInsertProduct.setOnClickListener(new View.OnClickListener() { // clickable
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(AdminHome.this, InsertTableActivity.class);
                startActivity(intent1); // start the new activity
            }
        });

        btnViewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(AdminHome.this, ViewTableActivity.class);
                startActivity(intent2);
            }
        });

        btnUpdateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(AdminHome.this, UpdateTableActivity.class);
                startActivity(intent3);
            }
        });

        btnDeleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent4 = new Intent(AdminHome.this, DeleteTableActivity.class);
                startActivity(intent4);
            }
        });

        btnViewOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent5 = new Intent(AdminHome.this, AdminOrderDetailsActivity.class);
                startActivity(intent5);
            }
        });
        Button backButton = findViewById(R.id.btn_back4);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent6 = new Intent(AdminHome.this, AdminLogin.class);
                startActivity(intent6);
            }
        });
    }
}
