
package com.bodicount.organizer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bodicount.R;
import com.bodicount.StudentManagerActivity;
import com.bodicount.timetable.TimetableManagerActivity;
import com.google.firebase.auth.FirebaseAuth;

public class OrganizerDashboard extends AppCompatActivity {
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_dashboard);

        auth = FirebaseAuth.getInstance();

        if(auth.getCurrentUser() == null){
            finish();
        }
    }

    public void goTimetableManagement(View view){
        Intent intent = new Intent(this, TimetableManagerActivity.class);
        startActivity(intent);
    }

    public void goStudentManagement(View view){
        Intent intent = new Intent(this, StudentManagerActivity.class);
        startActivity(intent);
    }

    public void goMyProfile(View view){

    }

    public void goToAddStudent(View view){
        Intent intent = new Intent(this, OrganizerStudentSubscribe.class);
        intent.putExtra("id", auth.getCurrentUser().getUid());
        startActivity(intent);
    }
}