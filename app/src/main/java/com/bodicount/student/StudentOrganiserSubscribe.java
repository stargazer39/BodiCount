package com.bodicount.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bodicount.Attendance_homepage;
import com.bodicount.Helpers4Dehemi;
import com.bodicount.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class StudentOrganiserSubscribe extends AppCompatActivity {
    private FirebaseAuth sAuth;
    private FirebaseFirestore db;

    private Student currentStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_organiser_subscribe);
    }


    private void showToast(String message, int duration){
        Toast toast = Toast.makeText(this, message, duration);
        toast.show();
    }

    public void onCLickSubmitID(View view){
        EditText orgID = (EditText) findViewById(R.id.studentOrganiserID);

        try{
            currentStudent.setOrganizerID(Helpers4Dehemi.vaildateString(orgID, true, "Organizer ID"));

            db.collection("user")
                    .document(sAuth.getCurrentUser().getUid())
                    .set(currentStudent)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                showToast("Successfully added Organizer ID", Toast.LENGTH_SHORT);
//                                Intent intent = new Intent(this, Attendance_homepage.class);
//                                startActivity(intent);
                            }else{
                                showToast("Failed to add Organizer ID", Toast.LENGTH_SHORT);
                            }
                        }
                    });
        }catch(Exception e){
            showToast(e.getMessage(), Toast.LENGTH_SHORT);
        }
    }
}