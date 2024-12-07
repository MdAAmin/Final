package com.example.menumade;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewOrderAdapter extends RecyclerView.Adapter<ViewOrderAdapter.OrderViewHolder> {

    private Context context;
    private Cursor cursor;

    // Constructor
    public ViewOrderAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout for each order
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        // Move the cursor to the appropriate position
        if (cursor != null && cursor.moveToPosition(position)) {
            // Access columns with correct names from the database
            @SuppressLint("Range")
            String name = cursor.getString(cursor.getColumnIndex("product_name")); // Ensure this matches your database column
            @SuppressLint("Range")
            double price = cursor.getDouble(cursor.getColumnIndex("product_price"));
            @SuppressLint("Range")
            int quantity = cursor.getInt(cursor.getColumnIndex("product_quantity"));

            // Bind data to views
            holder.tvName.setText("Name: " + name);
            holder.tvPrice.setText("Price: $" + price);
            holder.tvQuantity.setText("Quantity: " + quantity);
        }
    }

    @Override
    public int getItemCount() {
        return cursor != null ? cursor.getCount() : 0; // Return the number of items in the cursor
    }

    // Optional: Update the cursor with new data and notify the adapter
    public void swapCursor(Cursor newCursor) {
        if (cursor != null) {
            cursor.close();
        }
        cursor = newCursor;
        notifyDataSetChanged();
    }

    // ViewHolder class for the RecyclerView
    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice, tvQuantity;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            // Find views in the layout
            tvName = itemView.findViewById(R.id.tv_order_name);
            tvPrice = itemView.findViewById(R.id.tv_order_price);
            tvQuantity = itemView.findViewById(R.id.tv_order_quantity);
        }
    }
}
