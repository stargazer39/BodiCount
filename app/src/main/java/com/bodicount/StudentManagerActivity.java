package com.bodicount;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.bodicount.organizer.Organizer;
import com.bodicount.student.Student;
import com.bodicount.timeslot.OnEventHandler;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class StudentManagerActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_manager);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        refresh();
    }

    private void refresh(){
        try{
            db.collection("user")
                    .document(mAuth.getCurrentUser().getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                Organizer organizer = task.getResult().toObject(Organizer.class);
                                List<String> students = organizer.getStudentList();


                                for(String s: students){
                                    Log.i("student",s);
                                }

                                if(students == null || students.size() <= 0){
                                    Log.i("student", "No students");
                                    return;
                                }

                                // Update the UI
                                db.collection("user")
                                        .whereIn(FieldPath.documentId(), students)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if(task.isSuccessful()){
                                                    List<DocumentSnapshot> documentSnapshots = task.getResult().getDocuments();
                                                    List<Student> studentList = new ArrayList<>();

                                                    for(DocumentSnapshot snapshot : documentSnapshots){
                                                        Student student = snapshot.toObject(Student.class);
                                                        student.setId(snapshot.getId());
                                                        studentList.add(student);
                                                    }

                                                    setData(studentList);
                                                }else{
                                                    task.getException().printStackTrace();
                                                }
                                            }
                                        });
                            }
                        }
                    });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void addNewStudent(View view){
        /*Intent intent = new Intent(this, StudentManagerAddStudent.class);
        startActivity(intent);*/
    }

    public void setData(List<Student> studentList) {
        Log.i("student", "setData");
        RecyclerView studentListView = (RecyclerView) findViewById(R.id.student_list);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.addNewStudent);

        if(studentList.size() > 0){
            linearLayout.setVisibility(View.INVISIBLE);
            // addButton.setVisibility(View.VISIBLE);
        }else{
            linearLayout.setVisibility(View.VISIBLE);
            // addButton.setVisibility(View.INVISIBLE);
        }

        StudentAdaptor adaptor = new StudentAdaptor(studentList, mAuth, db, new OnEventHandler() {
            @Override
            public void onEvent() {
                refresh();
            }
        });

        studentListView.setAdapter(adaptor);
        studentListView.setLayoutManager(new LinearLayoutManager(this));
    }
}