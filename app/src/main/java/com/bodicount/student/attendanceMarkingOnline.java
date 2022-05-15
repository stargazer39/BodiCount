package com.bodicount.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bodicount.R;
import com.bodicount.commons.Attendance;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import org.joda.time.DateTime;

public class attendanceMarkingOnline extends AppCompatActivity {
    private FirebaseAuth sAuth;
    private FirebaseUser user;
    private FirebaseFirestore db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_marking_online);

        db = FirebaseFirestore.getInstance();
        sAuth = FirebaseAuth.getInstance();
        user = sAuth.getCurrentUser();
    }


    private void showToast(String message, int duration){
        Toast toast = Toast.makeText(this, message, duration);
        toast.show();
    }


    public void onTimeInCLick(View view){
        try{
            Attendance currentAttendance = new Attendance();
            currentAttendance.setDateTime(DateTime.now().toString());

        db.collection("attendance")
                .document(sAuth.getCurrentUser().getUid())
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
    }catch(Exception e){
        showToast(e.getMessage(), Toast.LENGTH_SHORT);
        e.printStackTrace();
    }
    }
}