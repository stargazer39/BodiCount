
package com.bodicount.organizer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bodicount.R;
import com.bodicount.StudentManagerActivity;
import com.bodicount.timetable.TimetableManagerActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class OrganizerDashboard extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_dashboard);

        auth = FirebaseAuth.getInstance();

        if(auth.getCurrentUser() == null){
            finish();
        }

        db = FirebaseFirestore.getInstance();
        db.collection("user")
                .document(auth.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            Organizer organizer = task.getResult().toObject(Organizer.class);
                            /*if(organizer.getLocation() == null || organizer.getLocation().length() <= 0){
                                // Get location
                                Intent intent = new Intent(getApplicationContext(), SelectLocationActivity.class);
                                startActivity(intent);
                            }*/
                        }else{
                            task.getException().printStackTrace();
                        }
                    }
                });
    }

    public void goTimetableManagement(View view){
        Intent intent = new Intent(this, TimetableManagerActivity.class);
        startActivity(intent);
    }

    public void goStudentManagement(View view){
        Intent intent = new Intent(this, StudentManagerActivity.class);
        startActivity(intent);
    }

    public void goMyProfile(View view){

    }

    public void goToAddStudent(View view){
        Intent intent = new Intent(this, OrganizerStudentSubscribe.class);
        intent.putExtra("id", auth.getCurrentUser().getUid());
        startActivity(intent);
    }

    public void logout(View view){
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")

                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        auth.signOut();
                        Intent intent = new Intent(getApplicationContext(), OrganizerLogin.class);
                        startActivity(intent);
                    }
                })

                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}