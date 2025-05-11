package com.example.cocolocokitchen;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class UnitSpinnerAdapter extends BaseAdapter {

    private final Context context;
    private final List<UnitItem> items;

    public UnitSpinnerAdapter(Context context, List<UnitItem> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() { return items.size(); }

    @Override
    public Object getItem(int position) { return items.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public boolean isEnabled(int position) {
        // Disable headers
        return !items.get(position).isHeader;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView view = new TextView(context);
        UnitItem item = items.get(position);
        view.setText(item.name);
        view.setPadding(32, 16, 32, 16);

        if (item.isHeader) {
            view.setTypeface(null, Typeface.BOLD);
            view.setBackgroundColor(Color.LTGRAY);
        }

        return view;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Only show the selected non-header item
        UnitItem item = items.get(position);
        TextView view = new TextView(context);
        view.setText(item.name);
        view.setTextSize(18);
        view.setPadding(8, 0, 8, 0);
        return view;
    }
}

