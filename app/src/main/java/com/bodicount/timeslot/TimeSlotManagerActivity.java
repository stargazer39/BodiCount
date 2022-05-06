package com.bodicount.timeslot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bodicount.R;
import com.bodicount.timetable.TimetableManagerActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class TimeSlotManagerActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private RecyclerView timeSlotList;
    private String timetableId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_slot_manager);

        Intent intent = getIntent();
        timetableId = intent.getExtras().getString(TimetableManagerActivity.TIMETABLE_ID);

        if(timetableId.length() <= 0) {
            showToast("Timetable not set", Toast.LENGTH_SHORT);
            finish();
            return;
        }

        timeSlotList = (RecyclerView)  findViewById(R.id.timeSlotList);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        refeshView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refeshView();
    }

    private void refeshView() {
        TimeSlotManagerActivity that = this;

        db.collection("user")
                .document(mAuth.getCurrentUser().getUid())
                .collection("timetables")
                .document(timetableId)
                .collection("timeslots")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            List<DocumentSnapshot> docList = querySnapshot.getDocuments();

                            List<Timeslot> timeslots = new ArrayList<>();

                            for(DocumentSnapshot doc : docList){
                                timeslots.add(doc.toObject(Timeslot.class));
                            }

                            that.setData(timeslots);
                        } else {
                            Log.d("onstart", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void setData(List<Timeslot> data){
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.addNewTimeslot);
        Button addButton = (Button) findViewById(R.id.addTimeslotBtn2);

        if(data.size() > 0){
            linearLayout.setVisibility(View.INVISIBLE);
            addButton.setVisibility(View.VISIBLE);
        }else{
            linearLayout.setVisibility(View.VISIBLE);
            addButton.setVisibility(View.INVISIBLE);
        }

        TimeslotAdaptor adaptor = new TimeslotAdaptor(data, mAuth, db, new OnEventHandler() {
            @Override
            public void onEvent() {
                refeshView();
            }
        }, timetableId);

        timeSlotList.setAdapter(adaptor);
        timeSlotList.setLayoutManager(new LinearLayoutManager(this));
    }

    public void addTimeslot(View view){
        AddTimeslot addTimeslot = new AddTimeslot();

        addTimeslot.setOnTimeSlotsetHandler(new OnTimeSlotSetHandler() {
            @Override
            public void dismiss() {
                addTimeslot.dismiss();
            }

            @Override
            public void onTimeSlotAdd(Timeslot timeslot) {
                addToDb(timeslot);
            }
        });

        addTimeslot.show(getSupportFragmentManager().beginTransaction(), "Hello");
    }
    private void addToDb(Timeslot timeslot) {
        // Add to the database
        try{
            FirebaseUser user = mAuth.getCurrentUser();

            db.collection("user")
                    .document(user.getUid())
                    .collection("timetables")
                    .document(timetableId)
                    .collection("timeslots")
                    .document(timeslot.getSlotName())
                    .set(timeslot)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                showToast("Added table " + timeslot.getSlotName(), Toast.LENGTH_SHORT);
                                refeshView();
                            }else{
                                task.getException().printStackTrace();
                                showToast("Failed to add table " + timeslot.getSlotName(), Toast.LENGTH_SHORT);
                            }
                        }
                    });
        }catch (Exception e){
            e.printStackTrace();
            showToast("Failed to add table.", Toast.LENGTH_SHORT);
        }
    }

    private void showToast(String message, int duration){
        Toast toast = Toast.makeText(this, message, duration);
        toast.show();
    }
}