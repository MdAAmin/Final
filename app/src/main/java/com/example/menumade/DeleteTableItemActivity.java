package com.example.menumade;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DeleteTableItemActivity extends AppCompatActivity {

    private EditText editTextName;
    private TextView textViewProductPrice;
    private TextView textViewProductQuantity;
    private ImageView imageViewProduct;
    private TextView textViewProductId;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_table);

        editTextName = findViewById(R.id.text_view_product_name);
        textViewProductPrice = findViewById(R.id.text_view_product_price);
        textViewProductQuantity = findViewById(R.id.text_view_product_quantity);
        textViewProductId = findViewById(R.id.text_view_product_id);
        imageViewProduct = findViewById(R.id.image_view_product);
        Button buttonDelete = findViewById(R.id.button_delete);
        Button buttonSearch = findViewById(R.id.button_search);
        Button btnBack = findViewById(R.id.btn_back6);

        databaseHelper = new DatabaseHelper(this);

        buttonSearch.setOnClickListener(view -> searchProduct());
        buttonDelete.setOnClickListener(view -> deleteProduct());
        btnBack.setOnClickListener(v -> {
            Intent intent1 = new Intent(DeleteTableItemActivity.this, AdminHome.class);
            startActivity(intent1);
        });
    }

    @SuppressLint("SetTextI18n")
    private void searchProduct() {
        String productName = editTextName.getText().toString().trim();
        if (productName.isEmpty()) {
            Toast.makeText(this, "Please enter a product name to search", Toast.LENGTH_SHORT).show();
            return;
        }

        Cursor cursor = databaseHelper.getProductByName(productName);
        if (cursor != null && cursor.moveToFirst()) {
            int productId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_ID));
            double price = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_PRICE));
            int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_QUANTITY));
            byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_IMAGE));

            textViewProductPrice.setText(String.valueOf(price));
            textViewProductQuantity.setText(String.valueOf(quantity));
            textViewProductId.setText("Product ID: " + productId);
            textViewProductId.setVisibility(View.VISIBLE);

            if (image != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                imageViewProduct.setImageBitmap(bitmap);
            }
            cursor.close();
        } else {
            Toast.makeText(this, "Product not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteProduct() {
        String productName = editTextName.getText().toString().trim();
        if (productName.isEmpty()) {
            Toast.makeText(this, "Please enter a product name to delete", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isDeleted = databaseHelper.deleteProduct(productName);
        if (isDeleted) {
            // Product deletion successful
            Toast.makeText(this, "Product deleted successfully", Toast.LENGTH_SHORT).show();
            clearFields();
        } else {
            // Product deletion failed
            Toast.makeText(this, "Product deletion failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearFields() {
        editTextName.setText("");
        textViewProductPrice.setText("");
        textViewProductQuantity.setText("");
        textViewProductId.setText("");
        textViewProductId.setVisibility(View.GONE);
        imageViewProduct.setImageResource(0);
    }
}