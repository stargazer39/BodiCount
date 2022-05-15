package com.bodicount;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.bodicount.student.Attendance_User_Profile;
import com.bodicount.student.Student;
import com.bodicount.student.attendanceMarkingOnline;
import com.bodicount.studentsnotes.StudentNotesManagement;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Attendance_homepage extends AppCompatActivity {
    private FirebaseAuth sAuth;
    private FirebaseUser user;
    private FirebaseFirestore db;

    private Student currentStudent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_homepage);

        db = FirebaseFirestore.getInstance();
        sAuth = FirebaseAuth.getInstance();
        user = sAuth.getCurrentUser();

        // Update time thread
        getDateTime();
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try{
                    getDateTime();
                }catch (Exception e){
                    e.printStackTrace();
                }

                handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(runnable, 1000);



        db.collection("user")
                .document(sAuth.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            Student currentStudent = task.getResult().toObject(Student.class);
                            db.collection("user")
                                    .document(currentStudent.getOrganizerID())
                                    .collection("timetables")
                                    .document(currentStudent.getTimetableID())
                                    .collection("timeslots")
                                    .document();
                        }else{
                            task.getException().printStackTrace();
                        }
                    }
                });
    }

    public void goToUserProfile(View view) {
        Intent intent = new Intent(this, Attendance_User_Profile.class);
        startActivity(intent);
    }

    public void getDateTime(){
        DateFormat df = new SimpleDateFormat("d 'th' EEEE',' MMM yyyy");
        DateFormat dt = new SimpleDateFormat(" h:mm:ss a");

        String date = df.format(Calendar.getInstance().getTime());
        String time = dt.format(Calendar.getInstance().getTime());

        TextView day = (TextView) findViewById(R.id.homepageDate);
        day.setText(date);
        TextView time1 = (TextView) findViewById(R.id.homepageTime);
        time1.setText(time);
    }

    public void goToNotes(View view){
        Intent intent = new Intent(this, StudentNotesManagement.class);
        startActivity(intent);
    }
    public void goTomarking(View view){
        Intent intent = new Intent(this , attendanceMarkingOnline.class);
        startActivity(intent);
    }


}