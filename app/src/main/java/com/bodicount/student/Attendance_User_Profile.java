package com.bodicount.student;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.bodicount.Helpers4Nithula;
import com.bodicount.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Attendance_User_Profile extends AppCompatActivity {

    private FirebaseAuth sAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_user_profile);
        getData();
    }


    private void getData(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        sAuth = FirebaseAuth.getInstance();
        user = sAuth.getCurrentUser();

//        Attendance_User_Profile that = this;

        db.collection("user").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                DocumentSnapshot snap = task.getResult();
                Student student = snap.toObject(Student.class);

                String email = student.getEmail();
                String fName = student.getfName();
                String lName = student.getlName();
                String phone = student.getPhone();
                String gender = student.getGender();

                TextView userName = (TextView) findViewById(R.id.userProfile_name);
                userName.setText(fName+ " " + lName);
                TextView userEmail = (TextView) findViewById(R.id.userProfileEmail);
                userEmail.setText(email);
                TextView userPhone = (TextView) findViewById(R.id.userProfilePhone);
                userPhone.setText(Helpers4Nithula.checkIfEmpty(phone));
                TextView userGender = (TextView) findViewById(R.id.userProfileGender);
                userGender.setText(Helpers4Nithula.checkIfEmpty(gender));


                //other stuff
            }else{
                //deal with error
                task.getException().printStackTrace();
            }

        });
    }

} // rUN KRLA BALANNA OONE BNS tell when you done