package com.bodicount;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bodicount.commons.Attendance;
import com.bodicount.student.Attendance_User_Profile;
import com.bodicount.student.Student;
import com.bodicount.student.attendanceMarkingOnline;
import com.bodicount.studentsnotes.StudentNotesManagement;
import com.bodicount.timeslot.Timeslot;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class Attendance_homepage extends AppCompatActivity {
    private FirebaseAuth sAuth;
    private FirebaseUser user;
    private FirebaseFirestore db;
    private Button nowButton;
    private Button nextButton;
    private Button nextAfterButton;

    private Student currentStudent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_homepage);

        db = FirebaseFirestore.getInstance();
        sAuth = FirebaseAuth.getInstance();
        user = sAuth.getCurrentUser();

        // Update time thread
        getDateTime();
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try{
                    getDateTime();
                }catch (Exception e){
                    e.printStackTrace();
                }

                handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(runnable, 1000);

        nowButton = findViewById(R.id.nowTslot);
        nextButton = findViewById(R.id.nextTslot);
        nextAfterButton = findViewById(R.id.nextAfterTslot);

        db.collection("user")
                .document(sAuth.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            Student currentStudent = task.getResult().toObject(Student.class);
                            db.collection("user")
                                    .document(currentStudent.getOrganizerID())
                                    .collection("timetables")
                                    .document(currentStudent.getTimetableID())
                                    .collection("timeslots")
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if(task.isSuccessful()){
                                                List<Timeslot> timeslotList = task.getResult().toObjects(Timeslot.class);

                                                DateTime tNow = DateTime.now();
                                                LocalTime timeNow = tNow.toLocalTime();

                                                int today = tNow.getDayOfWeek();

                                                Iterator<Timeslot> i = timeslotList.iterator();
                                                while (i.hasNext()) {
                                                    Timeslot tThis = i.next();
                                                    if(tThis.getDate() != today)
                                                        i.remove();
                                                }

                                                // Sort by time
                                                Collections.sort(timeslotList, new Comparator<Timeslot>() {
                                                    @Override
                                                    public int compare(Timeslot t1, Timeslot t2) {
                                                        LocalTime startT1 = Helpers4Dehemi.parseLocalTime(t1.getStartTime());
                                                        LocalTime startT2 = Helpers4Dehemi.parseLocalTime(t2.getStartTime());
                                                        return startT1.compareTo(startT2);
                                                    }
                                                });

                                                // Get current timeslot
                                                Iterator<Timeslot> k = timeslotList.iterator();
                                                Timeslot before = null;
                                                Timeslot now = null;
                                                Timeslot later = null;
                                                Timeslot laterAfter = null;

                                                while (k.hasNext()) {
                                                    Timeslot tThis = k.next();
                                                    LocalTime start = Helpers4Dehemi.parseLocalTime(tThis.getStartTime());
                                                    LocalTime end = Helpers4Dehemi.parseLocalTime(tThis.getEndTime());

                                                    if(start.isEqual(timeNow) || (start.isBefore(timeNow) && end.isAfter(timeNow))) {
                                                        now = tThis;

                                                        if(k.hasNext()){
                                                            later = k.next();

                                                            if(k.hasNext()){
                                                                laterAfter = k.next();
                                                            }
                                                        }
                                                        break;
                                                    }

                                                    before = tThis;
                                                }

                                                // Show it in the UI
                                                if(now != null){
                                                    nowButton.setText(now.getSlotName() + "\n" + Helpers4Dehemi.timeslotToString(now));
                                                }else{
                                                    nowButton.setText("Nothing to panic right now.");
                                                }

                                                if(later != null){
                                                    nextButton.setText(later.getSlotName() + "\n" + Helpers4Dehemi.timeslotToString(later));
                                                }else{
                                                    nextButton.setText("Nothing next.");
                                                }

                                                if(laterAfter != null){
                                                    nextAfterButton.setText(laterAfter.getSlotName() + "\n" + Helpers4Dehemi.timeslotToString(laterAfter));
                                                }else{
                                                    nextAfterButton.setText("Nothing later.");
                                                }

                                            }else{
                                                task.getException().printStackTrace();
                                            }
                                        }
                                    });
                        }else{
                            task.getException().printStackTrace();
                        }
                    }
                });
    }

    public void goToUserProfile(View view) {
        Intent intent = new Intent(this, Attendance_User_Profile.class);
        startActivity(intent);
    }

    public void getDateTime(){
        DateFormat df = new SimpleDateFormat("d 'th' EEEE',' MMM yyyy");
        DateFormat dt = new SimpleDateFormat(" h:mm:ss a");

        String date = df.format(Calendar.getInstance().getTime());
        String time = dt.format(Calendar.getInstance().getTime());

        TextView day = (TextView) findViewById(R.id.homepageDate);
        day.setText(date);
        TextView time1 = (TextView) findViewById(R.id.homepageTime);
        time1.setText(time);
    }

    public void goToNotes(View view){
        Intent intent = new Intent(this, StudentNotesManagement.class);
        startActivity(intent);
    }
    public void goTomarking(View view){

        Intent intent = new Intent(this , attendanceMarkingOnline.class);
        startActivity(intent);
    }


}