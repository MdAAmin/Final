package com.example.menumade;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.Locale;

public class ViewOrderAdapter extends RecyclerView.Adapter<ViewOrderAdapter.OrderViewHolder> {

    private Context context;
    private Cursor cursor;

    public ViewOrderAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_list_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        if (cursor != null && cursor.moveToPosition(position)) {
            String productName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ORDER_PRODUCT_NAME));
            double price = cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.COLUMN_ORDER_PRICE));
            int quantity = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ORDER_QUANTITY));
            int tableNumber = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_TABLE_NUMBER));

            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault());
            String formattedPrice = currencyFormat.format(price);

            holder.productNameTextView.setText(productName);
            holder.priceTextView.setText(formattedPrice);
            holder.quantityTextView.setText(String.valueOf(quantity));
            holder.tableNumberTextView.setText("Table #: " + tableNumber);
        }
    }

    @Override
    public int getItemCount() {
        return cursor != null ? cursor.getCount() : 0;
    }

    // Optional: Update the cursor with new data and notify the adapter
    public void swapCursor(Cursor newCursor) {
        if (cursor != null) {
            cursor.close();
        }
        cursor = newCursor;
        notifyDataSetChanged();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView productNameTextView;
        TextView priceTextView;
        TextView quantityTextView;
        TextView tableNumberTextView;

        public OrderViewHolder(View itemView) {
            super(itemView);
            productNameTextView = itemView.findViewById(R.id.tv_order_name);
            priceTextView = itemView.findViewById(R.id.tv_order_price);
            quantityTextView = itemView.findViewById(R.id.tv_order_quantity);
            tableNumberTextView = itemView.findViewById(R.id.orderTableNumberTextView);
        }
    }
}
