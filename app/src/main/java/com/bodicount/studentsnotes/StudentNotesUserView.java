package com.bodicount.studentsnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.bodicount.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class StudentNotesUserView extends AppCompatActivity {
    private FirebaseAuth sAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_notes_user_view);

        sAuth = FirebaseAuth.getInstance();
        FirebaseUser user = sAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        db.collection("user")
                .document(user.getUid())
                .collection("notes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            List<StudentNote> studentNotes = new ArrayList<>();

                            for(DocumentSnapshot documentSnapshot: task.getResult().getDocuments()){
                                StudentNote note = documentSnapshot.toObject(StudentNote.class);
                                note.setId(documentSnapshot.getId());
                                studentNotes.add(note);
                            }

                            setData(studentNotes);
                        }else{
                            task.getException().printStackTrace();
                        }
                    }
                });
    }

    public void setData(List<StudentNote> studentNotes) {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.notesListView);
        StudentNotesListAdapter listAdapter = new StudentNotesListAdapter(studentNotes);

        recyclerView.setAdapter(listAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }
}