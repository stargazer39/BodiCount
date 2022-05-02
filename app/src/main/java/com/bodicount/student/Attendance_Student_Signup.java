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
import com.bodicount.organizer.OrganizerLogin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class Attendance_Student_Signup extends AppCompatActivity {
    private FirebaseAuth sAuth;
    private FirebaseFirestore db;
    private String TAG = "OrgSignUp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_student_signup);

        sAuth = FirebaseAuth.getInstance();

        FirebaseUser user = sAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        if(user != null) {
            showToast("You are already logged in.", Toast.LENGTH_LONG);
            // TODO - Redirect user to login activity
            return;
        }
    }

    private void showToast(String message, int duration){
        Toast toast = Toast.makeText(this, message, duration);
        toast.show();
    }

    public void onClickSignUpBtn(View view) {
        Attendance_Student_Signup that = this;
        Student student = new Student();
        String re_password;

        // Validate the string with helpers implemented by Dehemi
        try {
            student.setfName(Helpers4Dehemi.vaildateString((EditText) findViewById(R.id.stuSignUpFirstName), true, "First Name"));
            student.setlName(Helpers4Dehemi.vaildateString((EditText) findViewById(R.id.stuSignUpLastName), true, "Last Name"));
            student.setEmail(Helpers4Dehemi.vaildateString((EditText) findViewById(R.id.stuSignUpEmail), true, "Email"));
            student.setPassword(Helpers4Dehemi.vaildateString((EditText) findViewById(R.id.stuSignUpPasswordSignUp), false, "Password"));
            re_password = Helpers4Dehemi.vaildateString((EditText) findViewById(R.id.stuSignUpPasswordReSignUp), false, "Repeat Password");
        }catch (Exception e){
            showToast(e.getMessage(), Toast.LENGTH_SHORT);
            return;
        }

        if(re_password.compareTo(student.getPassword()) != 0){
            showToast("Passwords should Match", Toast.LENGTH_SHORT);
            return;
        }
        // Database document
        sAuth.createUserWithEmailAndPassword(student.getEmail(), student.getPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = sAuth.getCurrentUser();

                            if(user == null) {
                                Log.e(TAG, "FATAL ERROR");
                                return;
                            }

                            // Add User to database
                            try{
                                db.collection("user")
                                        .document(user.getUid())
                                        .set(student)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()) {
                                                    showToast("Sign-up success", Toast.LENGTH_SHORT);
                                                    Intent intent = new Intent(that, Attendance_homepage.class);
                                                    startActivity(intent);
                                                }else {
                                                    user.delete();
                                                    showToast("Sign-up failed", Toast.LENGTH_SHORT);
                                                }
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                user.delete();
                                                showToast("Sign-up failed", Toast.LENGTH_SHORT);
                                            }
                                        });
                            }catch (Exception e){
                                e.printStackTrace();
                                user.delete();
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            showToast("Authentication failed.",
                                    Toast.LENGTH_SHORT);
                        }
                    }
                });
    }

}