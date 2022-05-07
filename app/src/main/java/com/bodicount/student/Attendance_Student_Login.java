package com.bodicount.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bodicount.Attendance_homepage;
import com.bodicount.Helpers4Dehemi;
import com.bodicount.R;
import com.bodicount.organizer.OrganizerSignUpActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Attendance_Student_Login extends AppCompatActivity {
    private FirebaseAuth sAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_student_login);

        sAuth = FirebaseAuth.getInstance();
        FirebaseUser user = sAuth.getCurrentUser();

        if(user != null){
            Intent intent = new Intent(this, Attendance_homepage.class);
            showToast("Login Success", Toast.LENGTH_SHORT);
            startActivity(intent);
            return;
        }

    }

    public void login(View view) {
        String email = "";
        String password = "";

        try{
            email = Helpers4Dehemi.vaildateString((EditText) findViewById(R.id.stuLogInEmail),true, "Email");
            password = Helpers4Dehemi.vaildateString((EditText) findViewById(R.id.stuLoginPassword), false, "Password");
        }catch(Exception e){
            showToast("Authentication failed.",
                    Toast.LENGTH_SHORT);
        }

        Attendance_Student_Login that = this;

        sAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            showToast("Login Success", Toast.LENGTH_SHORT);
                            Intent intent = new Intent(that, Attendance_homepage.class);
                            startActivity(intent);
                        }else{
                            showToast("Login Failed", Toast.LENGTH_SHORT);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showToast("Login Failed", Toast.LENGTH_SHORT);
            }
        });
    }

    public void redirectToSignUp(View view){
        Intent intent = new Intent(this, Attendance_Student_Signup.class);
        startActivity(intent);
    }

    private void showToast(String message, int duration){
        Toast toast = Toast.makeText(this, message, duration);
        toast.show();
    }
}