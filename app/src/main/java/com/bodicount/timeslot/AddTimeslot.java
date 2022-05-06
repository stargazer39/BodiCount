package com.bodicount.timeslot;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bodicount.Helpers4Dehemi;
import com.bodicount.R;
import com.bodicount.commons.TimeDuration;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

public class AddTimeslot extends DialogFragment {
    private OnTimeSlotSetHandler handler;
    private Timeslot currentEditedTimeslot = new Timeslot();

    public void setOnTimeSlotsetHandler(OnTimeSlotSetHandler handler){
        this.handler = handler;
    }



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
                handler.onTimeSlotAdd(currentEditedTimeslot);
            }
        });

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
                String text = new TimeDuration(picker.getHour(), picker.getMinute()).toString();
                currentEditedTimeslot.setStartTime(text);
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
                String text = new TimeDuration(picker.getHour(), picker.getMinute()).toString();
                currentEditedTimeslot.setEndTime(text);
            }
        });

        picker.show(getChildFragmentManager(), "Time");
    }
}
