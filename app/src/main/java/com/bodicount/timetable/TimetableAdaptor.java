package com.bodicount.timetable;

import android.content.ClipData;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.content.Context;
import android.widget.TextView;

import com.bodicount.R;

public class TimetableAdaptor extends BaseAdapter {
    Context ctx;
    String[] data;

    public TimetableAdaptor(Context ctx, String[] data) {
        this.ctx = ctx;
        this.data = data;
    }
    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public Object getItem(int i) {
        return data[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(this.ctx).
                    inflate(R.layout.timetable_fragment, viewGroup, false);
        }

        String currentItem = (String) getItem(i);

        TextView name = (TextView) view.findViewById(R.id.timetable_name);
        name.setText(data[i]);


        return view;
    }
}
