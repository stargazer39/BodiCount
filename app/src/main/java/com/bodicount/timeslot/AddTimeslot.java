package com.bodicount.timeslot;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bodicount.Helpers4Dehemi;
import com.bodicount.R;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.type.Date;
import com.google.type.DateTime;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalTime;

import java.util.ArrayList;

public class AddTimeslot extends DialogFragment implements AdapterView.OnItemSelectedListener {
    private OnTimeSlotSetHandler handler;
    private Timeslot currentEditedTimeslot = new Timeslot();

    public void setOnTimeSlotsetHandler(OnTimeSlotSetHandler handler){
        this.handler = handler;
    }

    private TextView startTime;
    private TextView endTime;
    private Switch online;
    private Spinner daysOfWeek;
    private int currentDate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.timeslot_add, container);
        getDialog().requestWindowFeature(STYLE_NO_TITLE);

        Button fromBtn = (Button) view.findViewById(R.id.pick_start_time_btn);
        fromBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getStartTime();
            }
        });

        Button toBtn = (Button) view.findViewById(R.id.pick_end_time_btn);
        toBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getEndTime();
            }
        });

        Button cancel = (Button) view.findViewById(R.id.timeslotAddCancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.dismiss();
            }
        });

        EditText name = (EditText) view.findViewById(R.id.timeSlotName);
        Button okBtn = (Button) view.findViewById(R.id.timeslotAddOk);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String slotName = "";

                try{
                    slotName = Helpers4Dehemi.vaildateString(name, true, "Slot name");
                }catch (Exception e){
                    showToast(e.getMessage(), Toast.LENGTH_SHORT);
                }

                currentEditedTimeslot.setSlotName(slotName);
                currentEditedTimeslot.setHeldOnline(online.isChecked());
                currentEditedTimeslot.setDate(currentDate);

                handler.onTimeSlotAdd(currentEditedTimeslot);
            }
        });

        startTime = (TextView) view.findViewById(R.id.timeSlotStartTime);
        endTime = (TextView) view.findViewById(R.id.timeSlotEndTime);
        online = (Switch) view.findViewById(R.id.onlineSwitch);
        daysOfWeek = (Spinner) view.findViewById(R.id.daysOfWeekSpinner);

        ArrayAdapter adapter
                = new ArrayAdapter(
                getContext(),
                android.R.layout.simple_spinner_item,
                Helpers4Dehemi.days);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daysOfWeek.setOnItemSelectedListener(this);
        daysOfWeek.setAdapter(adapter);
        return view;
    }

    private void showToast(String message, int duration){
        Toast toast = Toast.makeText(getContext(), message, duration);
        toast.show();
    }

    public void getStartTime(){
        MaterialTimePicker.Builder tp = new MaterialTimePicker.Builder();
        MaterialTimePicker picker = tp.setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(0)
                .build();

        picker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocalTime time = LocalTime.fromMillisOfDay(Helpers4Dehemi.convertToMillis(picker.getHour(), picker.getMinute()));
                currentEditedTimeslot.setStartTime(time.toString());

                startTime.setText(time.toString());
            }
        });

        picker.show(getChildFragmentManager(), "Time");
    }

    public void getEndTime(){
        MaterialTimePicker.Builder tp = new MaterialTimePicker.Builder();
        MaterialTimePicker picker = tp.setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(0)
                .build();

        picker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocalTime time = LocalTime.fromMillisOfDay(Helpers4Dehemi.convertToMillis(picker.getHour(), picker.getMinute()));
                currentEditedTimeslot.setEndTime(time.toString());

                endTime.setText(time.toString());
            }
        });

        picker.show(getChildFragmentManager(), "Time");
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        currentDate = Helpers4Dehemi.dayConstant[pos];
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}
