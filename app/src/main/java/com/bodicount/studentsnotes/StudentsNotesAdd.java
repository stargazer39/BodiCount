package com.bodicount.studentsnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bodicount.Helpers4Dehemi;
import com.bodicount.R;
import com.bodicount.student.Student;
import com.bodicount.timeslot.Timeslot;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class StudentsNotesAdd extends AppCompatActivity {
    private FirebaseAuth sAuth;
    private FirebaseFirestore db;
    private EditText desc;
    private EditText title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_notes_add);

        sAuth = FirebaseAuth.getInstance();
        FirebaseUser user = sAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        desc = (EditText) findViewById(R.id.noteText);
        title = (EditText) findViewById(R.id.noteTitle);
    }

    public void addNote(View view){
        StudentNote note = new StudentNote();
        try{
            note.setDescription(Helpers4Dehemi.vaildateString(desc, true, "Description"));
            note.setTitle(Helpers4Dehemi.vaildateString(title, true, "Title"));
        }catch (Exception e){
            showToast(e.getMessage(), Toast.LENGTH_SHORT);
        }

        db.collection("user")
                .document(sAuth.getCurrentUser().getUid())
                .collection("notes")
                .document()
                .set(note)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            showToast("Added note", Toast.LENGTH_SHORT);
                            finish();
                        }
                    }
                });
    }

    private void showToast(String message, int duration){
        Toast toast = Toast.makeText(this, message, duration);
        toast.show();
    }
}