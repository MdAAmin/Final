package com.example.menumade;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private Context context;
    private Cursor cursor;

    public OrderAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the layout for each item in the RecyclerView
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        if (cursor != null && cursor.moveToPosition(position)) {
            // Extract data from the cursor and bind to the views in OrderViewHolder
            @SuppressLint("Range")
            String orderDetails = cursor.getString(cursor.getColumnIndex("orderDetails"));
            holder.orderDetailsTextView.setText(orderDetails);
        }
    }

    @Override
    public int getItemCount() {
        return cursor != null ? cursor.getCount() : 0;
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        // Reference to the TextView in item_order.xml
        TextView orderDetailsTextView;

        public OrderViewHolder(View itemView) {
            super(itemView);
            // Find the TextView by ID
            orderDetailsTextView = itemView.findViewById(R.id.orderDetailsTextView);
        }
    }
}
