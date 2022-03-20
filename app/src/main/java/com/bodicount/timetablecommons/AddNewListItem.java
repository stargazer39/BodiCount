package com.bodicount.timetablecommons;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bodicount.R;

public class AddNewListItem extends Fragment {
    public AddNewListItem() {
        super(R.layout.fragment_add_new_list_item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_new_list_item, container, false);


        return view;
    }
}