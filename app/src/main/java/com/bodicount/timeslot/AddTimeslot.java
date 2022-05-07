package com.bodicount.timeslot;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import org.joda.time.LocalTime;

public class AddTimeslot extends DialogFragment {
    private OnTimeSlotSetHandler handler;
    private Timeslot currentEditedTimeslot = new Timeslot();

    public void setOnTimeSlotsetHandler(OnTimeSlotSetHandler handler){
        this.handler = handler;
    }

    private TextView startTime;
    private TextView endTime;
    private Switch online;

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

                handler.onTimeSlotAdd(currentEditedTimeslot);
            }
        });

        startTime = (TextView) view.findViewById(R.id.timeSlotStartTime);
        endTime = (TextView) view.findViewById(R.id.timeSlotEndTime);
        online = (Switch) view.findViewById(R.id.onlineSwitch);

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
}
