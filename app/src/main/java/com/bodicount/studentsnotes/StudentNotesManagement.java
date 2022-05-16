package com.bodicount.studentsnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bodicount.R;
import com.bodicount.student.Student;
import com.bodicount.timeslot.Timeslot;
import com.bodicount.timetable.Timetable;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class StudentNotesManagement extends AppCompatActivity {
    private FirebaseAuth sAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_notes_management);

        sAuth = FirebaseAuth.getInstance();
        FirebaseUser user = sAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        refresh();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    public void refresh() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.subjectsList);
        db.collection("user")
                .document(sAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            Student s = task.getResult().toObject(Student.class);

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

                                                Timeslot other = new Timeslot();
                                                other.setSlotName("My notes");
                                                other.setId("NOTES");

                                                list.add(other);
                                                for(DocumentSnapshot doc : task.getResult()) {
                                                    Timeslot t = doc.toObject(Timeslot.class);
                                                    t.setId(doc.getId());
                                                    list.add(t);
                                                }

                                                StudentNotesSubjectAdapter adapter = new StudentNotesSubjectAdapter(list);
                                                recyclerView.setAdapter(adapter);
                                                recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(), 2));
                                            }else{
                                                task.getException().printStackTrace();
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    public void addNote(View view){
        Intent intent = new Intent(this, StudentsNotesAdd.class);
        startActivity(intent);
    }
}