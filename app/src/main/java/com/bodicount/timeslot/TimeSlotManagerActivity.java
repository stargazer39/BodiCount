package com.bodicount.timeslot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.bodicount.SimpleInputDialog;
import com.bodicount.R;
import com.bodicount.timetable.TimetableAdaptor;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

public class TimeSlotManagerActivity extends AppCompatActivity {
    private String timetables[] = { "pp" };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_slot_manager);

        LinearLayout empty = (LinearLayout) findViewById(R.id.emptyElement);
        if(timetables.length == 0){
            empty.setVisibility(View.VISIBLE);
        }else{
            empty.setVisibility(View.GONE);
        }

        TimeslotAdaptor adaptor = new TimeslotAdaptor(this, timetables);
        ListView tablelist = (ListView) findViewById(R.id.timetablelist);
        tablelist.setAdapter(adaptor);
    }

    public void showAddNew(View view) {
        FragmentManager fm = getSupportFragmentManager();

        SimpleInputDialog slot = new SimpleInputDialog("New Timeslot name :","Done", "");
        slot.show(fm, SimpleInputDialog.TAG);
    }

    public void updateList(String timetables){

    }
}