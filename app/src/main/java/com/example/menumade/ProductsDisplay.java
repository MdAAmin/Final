package com.example.menumade;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProductsDisplay extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private LinearLayout productLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_display);

        Button btnBack = findViewById(R.id.btn_back5);
        dbHelper = new DatabaseHelper(this);
        productLayout = findViewById(R.id.productLayout);

        displayProducts();

        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(ProductsDisplay.this, AdminAndOldTable.class);
            startActivity(intent);
        });
    }

    private void displayProducts() {
        Cursor cursor = dbHelper.getAllProducts();
        if (cursor == null || cursor.getCount() == 0) {
            return;
        }

        LayoutInflater inflater = LayoutInflater.from(this);

        while (cursor.moveToNext()) {
            View productView = inflater.inflate(R.layout.activity_product_list_item, null);

            TextView nameTextView = productView.findViewById(R.id.productName);
            TextView priceTextView = productView.findViewById(R.id.productPrice);
            TextView quantityTextView = productView.findViewById(R.id.productQuantity);
            ImageView productImageView = productView.findViewById(R.id.productImage);

            String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PRODUCT_NAME));
            double price = cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.COLUMN_PRODUCT_PRICE));
            int quantity = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_PRODUCT_QUANTITY));
            byte[] imageBytes = cursor.getBlob(cursor.getColumnIndex(DatabaseHelper.COLUMN_PRODUCT_IMAGE));

            nameTextView.setText(name);
            priceTextView.setText(String.valueOf(price));
            quantityTextView.setText(String.valueOf(quantity));

            if (imageBytes != null && imageBytes.length > 0) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                productImageView.setImageBitmap(bitmap);
            } else {
                productImageView.setImageResource(R.drawable.img); // Placeholder image
            }

            productImageView.setOnClickListener(v -> {
                Intent intent = new Intent(ProductsDisplay.this, OrderActivity.class);
                intent.putExtra("productName", name);
                intent.putExtra("productPrice", price);
                intent.putExtra("productQuantity", quantity);
                startActivity(intent);
            });

            productLayout.addView(productView);
        }

        cursor.close();
    }
}
