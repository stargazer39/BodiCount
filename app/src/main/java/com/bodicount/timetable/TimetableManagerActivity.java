package com.bodicount.timetable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bodicount.Helpers4Dehemi;
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

public class TimetableManagerActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private RecyclerView timetableList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable_manager2);

        timetableList = (RecyclerView)  findViewById(R.id.timeTableList2);
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
        TimetableManagerActivity that = this;

        db.collection("user")
                .document(mAuth.getCurrentUser().getUid())
                .collection("timetables")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            List<DocumentSnapshot> docList = querySnapshot.getDocuments();

                            List<Timetable> timetables = new ArrayList<>();

                            for(DocumentSnapshot doc : docList){
                                timetables.add(doc.toObject(Timetable.class));
                            }

                            that.setData(timetables);
                        } else {
                            Log.d("onstart", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void setData(List<Timetable> data){
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.addNewTimetable);
        if(data.size() > 0){
            linearLayout.setVisibility(View.INVISIBLE);
        }else{
            linearLayout.setVisibility(View.VISIBLE);
        }

        TimetableAdaptor adaptor = new TimetableAdaptor(data, mAuth, db, new TimetableOnEventRefreshHandler() {
            @Override
            public void onEvent() {
                refeshView();
            }
        });

        timetableList.setAdapter(adaptor);
        timetableList.setLayoutManager(new LinearLayoutManager(this));
    }

    public void addTimetable(View view){
        TimetableManagerActivity that = this;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter a Subject");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String subject = "";
                try {
                    subject = Helpers4Dehemi.vaildateString(input.getText().toString(), true, "Subject") ;
                } catch (Exception e) {
                    showToast(e.getMessage(), Toast.LENGTH_SHORT);
                    return;
                }

                // Add to the database
                try{
                    Timetable timetable = new Timetable();
                    timetable.setSubject(subject);

                    FirebaseUser user = mAuth.getCurrentUser();

                    db.collection("user")
                            .document(user.getUid())
                            .collection("timetables")
                            .document(subject)
                            .set(timetable)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        showToast("Added table " + timetable.getSubject(), Toast.LENGTH_SHORT);
                                        refeshView();
                                        dialog.cancel();
                                    }else{
                                        task.getException().printStackTrace();
                                        showToast("Failed to add table " + timetable.getSubject(), Toast.LENGTH_SHORT);
                                        dialog.cancel();
                                    }
                                }
                            });
                }catch (Exception e){
                    e.printStackTrace();
                    showToast("Failed to add table.", Toast.LENGTH_SHORT);
                    dialog.cancel();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void showToast(String message, int duration){
        Toast toast = Toast.makeText(this, message, duration);
        toast.show();
    }
}