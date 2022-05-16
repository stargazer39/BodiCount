package com.bodicount.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bodicount.Attendance_Marking;
import com.bodicount.Attendance_homepage;
import com.bodicount.Helpers4Nithula;
import com.bodicount.MainActivity;
import com.bodicount.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Attendance_User_Profile extends AppCompatActivity {

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
        db.collection("user").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                DocumentSnapshot snap = task.getResult();
                Student student = snap.toObject(Student.class);
                //Get the data from the database
                String email = student.getEmail();
                String fName = student.getfName();
                String lName = student.getlName();
                String phone = student.getPhone();
                String gender = student.getGender();
                //Set the data in the textview
                TextView userName = (TextView) findViewById(R.id.userProfile_name);
                userName.setText(fName+ " " + lName);
                TextView userEmail = (TextView) findViewById(R.id.userProfileEmail);
                userEmail.setText(email);
                TextView userPhone = (TextView) findViewById(R.id.userProfilePhone);
                userPhone.setText(Helpers4Nithula.checkIfEmpty(phone));
                TextView userGender = (TextView) findViewById(R.id.userProfileGender);
                userGender.setText(Helpers4Nithula.checkIfEmpty(gender));

            }else{
                //deals with error
                task.getException().printStackTrace();
            }
        });
    }


    private void showToast(String message, int duration){
        Toast toast = Toast.makeText(this, message, duration);
        toast.show();
    }


    public void deleteData(View view){

        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(Attendance_User_Profile.this);

        builder.setMessage("Do you want to Unsubscribe ?");
        builder.setTitle("Alert !");
        builder.setCancelable(false);

        builder
                .setPositiveButton(
                        "Yes",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                sAuth = FirebaseAuth.getInstance();
                                String user = sAuth.getCurrentUser().getUid();

                                Map<String, Object> unsubscribe = new HashMap<>();
                                unsubscribe.put("organizerID", FieldValue.delete());
                                FirebaseFirestore.getInstance()
                                        .collection("user")
                                        .document(user)
                                        .update(unsubscribe).addOnCompleteListener(task -> {
                                    if(task.isSuccessful()){
                                        showToast("Successfully unsubscribed from Organization", Toast.LENGTH_SHORT);
                                        Intent intent = new Intent(getApplicationContext(), StudentOrganiserSubscribe.class);
                                        startActivity(intent);
                                    }else{
                                        showToast("Unsubscription Unsuccessful", Toast.LENGTH_SHORT);
                                    }
                                });
                            }
                        });

        builder
                .setNegativeButton(
                        "No",
                        new DialogInterface
                                .OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {
                                dialog.cancel();
                            }
                        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();




    }

    public void goToEdit(View view){
        Intent intent = new Intent(this , Attendance_user_profile_edit.class);
        startActivity(intent);
    }


    public void logout(View view){

        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(Attendance_User_Profile.this);

        builder.setMessage("Do you want to Log Out ?");
        builder.setTitle("Alert !");
        builder.setCancelable(false);

        builder
                .setPositiveButton(
                        "Yes",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {
                                sAuth.getInstance().signOut();
                                showToast("Successfully Signed Out", Toast.LENGTH_SHORT);
                                Intent intent = new Intent(getApplicationContext(), Attendance_Student_Login.class);
                                startActivity(intent);
                            }
                        });

        builder
                .setNegativeButton(
                        "No",
                        new DialogInterface
                                .OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {
                                dialog.cancel();
                            }
                        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void goHomeBtn(View view){
        Intent intent = new Intent(this, Attendance_homepage.class);
        startActivity(intent);

    }

}