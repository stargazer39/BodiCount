package com.bodicount.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bodicount.Attendance_homepage;
import com.bodicount.R;
import com.bodicount.commons.Attendance;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.joda.time.DateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class attendanceMarkingOnline extends AppCompatActivity {
    private FirebaseAuth sAuth;
    private FirebaseUser user;
    private FirebaseFirestore db;

//    private Button btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_marking_online);

        db = FirebaseFirestore.getInstance();
        sAuth = FirebaseAuth.getInstance();
        user = sAuth.getCurrentUser();

        // Update time thread
        getDateTime2();
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try{
                    getDateTime2();
                }catch (Exception e){
                    e.printStackTrace();
                }

                handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(runnable, 1000);

//        btn = (Button) findViewById(R.id.timeInBtn);
//        btn.setOnClickListener((View.OnClickListener) this);
    }


    private void showToast(String message, int duration){
        Toast toast = Toast.makeText(this, message, duration);
        toast.show();
    }


    public void onTimeInCLick(View view){
        try{

            Attendance currentAttendance = new Attendance();
            currentAttendance.setDateTime(DateTime.now().toString());
            currentAttendance.setUserID(user.getUid());

            db.collection("user")
            .document(sAuth.getUid())
            .get()
            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        Student currentStudent = task.getResult().toObject(Student.class);
                        currentAttendance.setTimeTableID(currentStudent.getTimetableID());
                        currentAttendance.setOrganizerID(currentStudent.getOrganizerID());
                        //currentAttendance.setTimeSlotID();


                        db.collection("attendance")
                                .document()
                                .set(currentAttendance)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            showToast("Successfully Attended", Toast.LENGTH_SHORT);
                                        }else{
                                            showToast("Attendance failed", Toast.LENGTH_SHORT);
                                        }
                                    }
                                });

                    }else{

                    }
                }
            });




    }catch(Exception e){
        showToast(e.getMessage(), Toast.LENGTH_SHORT);
        e.printStackTrace();
    }
    view.setEnabled(false);
    Button btn = (Button) view;
        btn.setText("Attending class");
    }

    public void getDateTime2(){
        DateFormat df = new SimpleDateFormat("d 'th' EEEE',' MMM yyyy");
        DateFormat dt = new SimpleDateFormat(" h:mm:ss a");

        String date = df.format(Calendar.getInstance().getTime());
        String time = dt.format(Calendar.getInstance().getTime());

        TextView day = (TextView) findViewById(R.id.homepageDate2);
        day.setText(date);
        TextView time1 = (TextView) findViewById(R.id.homepageTime2);
        time1.setText(time);
    }

    public void backBtn(View view){
        Intent intent = new Intent(this, Attendance_homepage.class);
        startActivity(intent);

    }

    public void userProfileBtn(View view){
        Intent intent = new Intent(this, Attendance_User_Profile.class);
        startActivity(intent);
    }
}