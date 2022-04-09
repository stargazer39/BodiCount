package com.bodicount.timeslot;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bodicount.R;

public class TimeslotAdaptor extends BaseAdapter {
    Context ctx;
    String[] data;

    public TimeslotAdaptor(Context ctx, String[] data) {
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
            if(data[i] == ""){
                view = LayoutInflater.from(this.ctx).
                        inflate(R.layout.fragment_add_new_list_item, viewGroup, false);
                return view;
            }

            view = LayoutInflater.from(this.ctx).
                    inflate(R.layout.fragment_time_slot, viewGroup, false);
        }

        String currentItem = (String) getItem(i);

        TextView name = (TextView) view.findViewById(R.id.timeslot_name);
        name.setText(data[i]);

        TextView button = (TextView) view.findViewById(R.id.edit_time_slot);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(ctx, view);

                // Add menu item click listener
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        Intent intent = new Intent(ctx,TimeslotEditActivity.class);
                        ctx.startActivity(intent);
                        Log.d("Timeslots", "Onclicktriggered");
                        return true;
                    }
                });
                popupMenu.inflate(R.menu.timeslot_option_overflow_menu);
                popupMenu.show();
            }
        });

        return view;
    }
}
