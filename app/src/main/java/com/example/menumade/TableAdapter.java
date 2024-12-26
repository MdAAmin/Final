package com.example.menumade;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import android.database.Cursor;

public class TableAdapter extends BaseAdapter {

    private Context context;
    private Cursor cursor;

    public TableAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public Object getItem(int position) {
        return cursor.moveToPosition(position) ? cursor : null;
    }

    @Override
    public long getItemId(int position) {
        return cursor.moveToPosition(position) ? cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_TABLE_ID)) : -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.table_item, parent, false);
        }

        TextView tvTableNumber = convertView.findViewById(R.id.tv_table_number);

        if (cursor.moveToPosition(position)) {
            int tableNumber = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_TABLE_NUMBER));
            tvTableNumber.setText("Table " + tableNumber);
        }

        return convertView;
    }
}
