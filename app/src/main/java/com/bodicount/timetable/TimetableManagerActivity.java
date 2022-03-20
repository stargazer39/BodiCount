package com.bodicount.timetable;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.bodicount.R;
import com.bodicount.timeslot.TimeslotAdaptor;
import com.bodicount.timetable.TimetableAdaptor;

public class TimetableManagerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable_manager);

        String timetables[] = { "Chemistry", "Maths", "Physics" };

        TimetableAdaptor adaptor = new TimetableAdaptor(this, timetables);

        ListView tablelist = (ListView) findViewById(R.id.timetablelist);
        tablelist.setAdapter(adaptor);
    }
}