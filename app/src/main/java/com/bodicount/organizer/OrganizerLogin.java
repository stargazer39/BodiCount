package com.bodicount.organizer;

import static com.bodicount.Helpers4Dehemi.vaildateString;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bodicount.Helpers4Dehemi;
import com.bodicount.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class OrganizerLogin extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_login);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if(user == null){
            Intent intent = new Intent(this, OrganizerSignUpActivity.class);
            startActivity(intent);
            return;
        }
    }

    public void login(View view) {
        String email = "";
        String password = "";

        try{
            email = Helpers4Dehemi.vaildateString((EditText) findViewById(R.id.orgEmailSignUp),true, "Email");
            password = Helpers4Dehemi.vaildateString((EditText) findViewById(R.id.orgPasswordSignUp), false, "Password");
        }catch(Exception e){
            showToast("Authentication failed.",
                    Toast.LENGTH_SHORT);
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            showToast("Login Success", Toast.LENGTH_SHORT);
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

    private void showToast(String message, int duration){
        Toast toast = Toast.makeText(this, message, duration);
        toast.show();
    }
}