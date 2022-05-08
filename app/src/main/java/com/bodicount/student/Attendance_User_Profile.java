package com.bodicount.student;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bodicount.Helpers4Nithula;
import com.bodicount.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Locale;

public class Attendance_User_Profile extends AppCompatActivity {

//    private static final String TAG = Deleted;
    private FirebaseAuth sAuth;
    private FirebaseUser user;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_user_profile);
        sAuth = FirebaseAuth.getInstance();
        user = sAuth.getCurrentUser();

        if(user == null){
            Intent intent = new Intent(this,Attendance_Student_Login.class);
            startActivity(intent);
        }
        db = FirebaseFirestore.getInstance();
        getData();
    }


    private void getData(){



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



    public void deleteData(View view){
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        sAuth = FirebaseAuth.getInstance();
//        user = sAuth.getCurrentUser();

//        new AlertDialog.Builder(getApplicationContext())
//                .setTitle("Delete Account".toUpperCase(Locale.ROOT))
//                .setMessage("Do you really want to delete your account?")
//                .setIcon(android.R.drawable.ic_dialog_alert)
//                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
        db.collection("user").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .delete().addOnCompleteListener(task -> {

                    if(task.isSuccessful()){
//
//                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
        });

    }

    public void goToEdit(View view){
        Intent intent = new Intent(this , Attendance_user_profile_edit.class);
        startActivity(intent);
    }

}