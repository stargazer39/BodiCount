package com.bodicount.organizer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bodicount.Helpers4Dehemi;
import com.bodicount.R;
import com.bodicount.timetable.TimetableManagerActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class OrganizerSignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String TAG = "OrgSignUp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_signup);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        if(user != null) {
            showToast("You are already logged in.", Toast.LENGTH_LONG);
            Intent intent = new Intent(this, TimetableManagerActivity.class);
            startActivity(intent);
            return;
        }
    }

    private void showToast(String message, int duration){
        Toast toast = Toast.makeText(this, message, duration);
        toast.show();
    }

    public void onClickSignUpBtn(View view) {
        OrganizerSignUpActivity that = this;
        Organizer organizer = new Organizer();
        String re_password;

        // Sign up the user
        try {
            organizer.setfName(Helpers4Dehemi.vaildateString((EditText) findViewById(R.id.orgFirstNameSignUp), true, "First Name"));
            organizer.setlName(Helpers4Dehemi.vaildateString((EditText) findViewById(R.id.orgLastNameSignUp), true, "Last Name"));
            organizer.setEmail(Helpers4Dehemi.vaildateString((EditText) findViewById(R.id.orgEmailSignUp), true, "Email"));
            organizer.setPassword(Helpers4Dehemi.vaildateString((EditText) findViewById(R.id.orgPasswordSignUp), false, "Password"));
            re_password = Helpers4Dehemi.vaildateString((EditText) findViewById(R.id.orgPasswordReSignUp), false, "Repeat Password");
        }catch (Exception e){
            showToast(e.getMessage(), Toast.LENGTH_SHORT);
            return;
        }

        if(re_password.compareTo(organizer.getPassword()) != 0){
            showToast("Passwords should Match", Toast.LENGTH_SHORT);
            return;
        }
        // Database document
        mAuth.createUserWithEmailAndPassword(organizer.getEmail(), organizer.getPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            if(user == null) {
                                Log.e(TAG, "FATAL ERROR");
                                return;
                            }

                            // Add User to database
                            try{
                                db.collection("user")
                                        .document(user.getUid())
                                        .set(organizer)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()) {
                                                    showToast("Sign-up success", Toast.LENGTH_SHORT);
                                                    Intent intent = new Intent(that, OrganizerLogin.class);
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