package com.bodicount.timeslot;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bodicount.R;
import com.bodicount.SimpleInputDialog;

public class TimeSlotFragment extends Fragment {
    public TimeSlotFragment() {
        super(R.layout.fragment_time_slot);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_time_slot, container, false);
        TextView edit = (TextView) view.findViewById(R.id.edit_time_slot);
        return view;
    }
}