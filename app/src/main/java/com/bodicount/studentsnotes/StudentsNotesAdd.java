package com.bodicount.studentsnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;
import android.util.Log;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_notes_add);

        sAuth = FirebaseAuth.getInstance();
        FirebaseUser user = sAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        db.collection("user")
                .document(sAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            Student s = task.getResult().toObject(Student.class);
                            Log.i("poop", s.getOrganizerID());
                            db.collection("user")
                                    .document(s.getOrganizerID())
                                    .collection("timetables")
                                    .document(s.getTimetableID())
                                    .collection("timeslots")
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if(task.isSuccessful()){
                                                List<Timeslot> list = new ArrayList<>();
                                                List<String> subjects = new ArrayList<>();
                                                for(DocumentSnapshot doc : task.getResult()) {
                                                    Timeslot t = doc.toObject(Timeslot.class);
                                                    t.setId(doc.getId());
                                                    list.add(t);
                                                    subjects.add(t.getSlotName());
                                                }

                                            }else{
                                                task.getException().printStackTrace();
                                            }
                                        }
                                    });
                        }
                    }
                });
    }
}