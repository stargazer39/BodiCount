package com.bodicount.timeslot;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bodicount.R;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

public class TimeslotEditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeslot_edit);

        Button button = (Button) findViewById(R.id.pick_start_time_btn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialTimePicker.Builder tp = new MaterialTimePicker.Builder();
                tp.setTimeFormat(TimeFormat.CLOCK_12H)
                        .setHour(12)
                        .setMinute(10)
                        .setTheme(R.style.CustomThemeOverlay_MaterialCalendar_Fullscreen)
                        .build()
                .show(getSupportFragmentManager(), "Time");
            }
        });

    }
}