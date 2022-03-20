package com.bodicount;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class SimpleInputDialog extends DialogFragment {
    public static String TAG = "NewTimeSlotFragment";
    private String title;
    private String doneBtnString;
    private String textInputValue;
    public SimpleInputDialog(String title, String doneBtnString, String textInputValue) {
        super(R.layout.simple_input_dialog_fragment);

        this.title = title;
        this.doneBtnString = doneBtnString;
        this.textInputValue = textInputValue;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.simple_input_dialog_fragment, container, false);

        TextView titleView = (TextView) view.findViewById(R.id.simple_input_title);
        titleView.setText(title);

        EditText editText = (EditText) view.findViewById(R.id.simple_input_textinput);
        editText.setText(textInputValue);

        SimpleInputDialog that = this;
        Button done = (Button) view.findViewById(R.id.simple_input_successbtn_text);
        done.setText(doneBtnString);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                that.dismiss();
            }
        });
        return view;
    }
}