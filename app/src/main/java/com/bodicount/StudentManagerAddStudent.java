package com.bodicount;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bodicount.student.Student;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.nio.charset.Charset;
import java.util.Random;

public class StudentManagerAddStudent extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseAuth mStudentAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_manager_add_student);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
    }

    public void addStudent(View view){
        try{
            Student student = new Student();
            try{
                String fName = "";
                String lName = "";
                String phone = "";
                String email = "";

                student.setfName(Helpers4Dehemi.vaildateString((EditText) findViewById(R.id.stdFirstNameSignUp), true, "First Name"));
                student.setlName(Helpers4Dehemi.vaildateString((EditText) findViewById(R.id.stdLastNameSignUp), true, "Last Name"));
                student.setPhone(((EditText) findViewById(R.id.stdManagerPhone)).getText().toString());
                student.setEmail(Helpers4Dehemi.vaildateString((EditText) findViewById(R.id.stdManagerEmail), true, "Email"));

                // Generate new password for the student
                byte[] array = new byte[10];
                new Random().nextBytes(array);
                String generatedString = new String(array, Charset.forName("UTF-8"));

                student.setPassword(generatedString);

                mStudentAuth.createUserWithEmailAndPassword(student.getEmail(), generatedString)
                        .addOnCompleteListener(task -> {
                            if(task.isSuccessful()){
                                try{
                                    student.setId(mStudentAuth.getCurrentUser().getUid());
                                    mStudentAuth.signOut();

                                    db.collection("user")
                                            .document(student.getId())
                                            .set(student)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    db.collection("user")
                                                            .document(mAuth.getCurrentUser().getUid())
                                                            .update("studentList", FieldValue.arrayUnion(student.getId()))
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if(task.isSuccessful()){
                                                                        finish();
                                                                    }else{
                                                                        task.getException().printStackTrace();
                                                                        showToast("Adding User failed", Toast.LENGTH_SHORT);
                                                                    }
                                                                }
                                                            });
                                                }
                                            });
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }else{
                                task.getException().printStackTrace();
                            }
                        });
            }catch (Exception e){
                e.printStackTrace();
                showToast(e.getMessage(), Toast.LENGTH_SHORT);
            }
        }catch (Exception e){
            e.printStackTrace();
            showToast("Adding User failed", Toast.LENGTH_SHORT);
        }
    }

    private void showToast(String message, int duration){
        Toast toast = Toast.makeText(this, message, duration);
        toast.show();
    }
}