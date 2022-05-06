package com.bodicount;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bodicount.student.Attendance_User_Profile;

public class Attendance_homepage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_homepage);
    }

    public void goToUserProfile(View view) {
        Intent intent = new Intent(this, Attendance_User_Profile.class);
        startActivity(intent);
    }
}