package com.bodicount;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bodicount.organizer.OrganizerLogin;
import com.bodicount.organizer.OrganizerSignUpActivity;
import com.bodicount.student.Attendance_Student_Login;

public class Organizer_Student_Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_student_login);
    }

    public void studentLogin(View view) {
        Intent intent = new Intent(this, Attendance_Student_Login.class);
        startActivity(intent);
    }

    public void organizerLogin(View view) {
        Intent intent = new Intent(this, OrganizerLogin.class);
        startActivity(intent);
    }

    public void organizerSignUp(View view) {
        Intent intent = new Intent(this, OrganizerSignUpActivity.class);
        startActivity(intent);
    }
}