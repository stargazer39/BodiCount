package com.bodicount;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bodicount.student.Student;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class StudentManagerEditStudentDetails extends AppCompatActivity {
    private FirebaseAuth sAuth;
    private FirebaseFirestore db;
    private EditText email;
    private EditText firstName;
    private EditText lastName;
    private EditText phoneNumber;
    private Student currentStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_manager_edit_student_details);

        sAuth = FirebaseAuth.getInstance();
        FirebaseUser user = sAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        email = (EditText) findViewById(R.id.studentEditEmail);
        firstName = (EditText) findViewById(R.id.studentFirstnameEdit);
        lastName = (EditText) findViewById(R.id.studentlastnameEdit);
        phoneNumber = (EditText) findViewById(R.id.studentPhoneEdit) ;

        Intent intent = getIntent();
        String studentId = intent.getExtras().getString("studentID");

        db.collection("user")
                .document(studentId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            Student student = task.getResult().toObject(Student.class);
                            currentStudent = student;
                            currentStudent.setId(task.getResult().getId());

                            email.setText(student.getEmail());
                            firstName.setText(student.getfName());
                            lastName.setText(student.getlName());
                            phoneNumber.setText(student.getPhone());

                        }else{
                            task.getException().printStackTrace();
                        }
                    }
                });
    }

    public void update(View view){
        try{
            try{
                currentStudent.setfName(Helpers4Dehemi.vaildateString(firstName, true, "Fist Name"));
                currentStudent.setlName(Helpers4Dehemi.vaildateString(lastName, true, "Last name"));
                currentStudent.setEmail(Helpers4Dehemi.vaildateString(email, true, "Email"));
                currentStudent.setPhone(Helpers4Dehemi.vaildateString(phoneNumber, true, "Phone number"));
            }catch(Exception e){
                showToast(e.getMessage(), Toast.LENGTH_SHORT);
            }

            db.collection("user")
                    .document(currentStudent.getId())
                    .set(currentStudent)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                showToast("Update success", Toast.LENGTH_SHORT);
                                finish();
                            }else{
                                task.getException().printStackTrace();
                                showToast("Failed to update", Toast.LENGTH_SHORT);
                            }
                        }
                    });
        }catch (Exception e){
            e.printStackTrace();

        }
    }

    private void showToast(String message, int duration){
        Toast toast = Toast.makeText(this, message, duration);
        toast.show();
    }
}