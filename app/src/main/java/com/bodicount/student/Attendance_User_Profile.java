package com.bodicount.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bodicount.Helpers4Nithula;
import com.bodicount.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Attendance_User_Profile extends AppCompatActivity {

//    private static final String TAG = Deleted;
    private FirebaseAuth sAuth;
    private FirebaseUser user;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_user_profile);
        sAuth = FirebaseAuth.getInstance();
        user = sAuth.getCurrentUser();

        if(user == null){
            Intent intent = new Intent(this,Attendance_Student_Login.class);
            startActivity(intent);
        }
        db = FirebaseFirestore.getInstance();
        getData();
    }


    private void getData(){



//        Attendance_User_Profile that = this;

        db.collection("user").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                DocumentSnapshot snap = task.getResult();
                Student student = snap.toObject(Student.class);

                String email = student.getEmail();
                String fName = student.getfName();
                String lName = student.getlName();
                String phone = student.getPhone();
                String gender = student.getGender();

                TextView userName = (TextView) findViewById(R.id.userProfile_name);
                userName.setText(fName+ " " + lName);
                TextView userEmail = (TextView) findViewById(R.id.userProfileEmail);
                userEmail.setText(email);
                TextView userPhone = (TextView) findViewById(R.id.userProfilePhone);
                userPhone.setText(Helpers4Nithula.checkIfEmpty(phone));
                TextView userGender = (TextView) findViewById(R.id.userProfileGender);
                userGender.setText(Helpers4Nithula.checkIfEmpty(gender));


                //other stuff
            }else{
                //deal with error
                task.getException().printStackTrace();
            }

        });
    }


    private void showToast(String message, int duration){
        Toast toast = Toast.makeText(this, message, duration);
        toast.show();
    }


    public void deleteData(View view){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        sAuth = FirebaseAuth.getInstance();
        String user = sAuth.getCurrentUser().getUid();

//        new AlertDialog.Builder(getApplicationContext())
//                .setTitle("Delete Account".toUpperCase(Locale.ROOT))
//                .setMessage("Do you really want to delete your account?")
//                .setIcon(android.R.drawable.ic_dialog_alert)
//                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
//        db.collection("user").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                .delete("o").addOnCompleteListener(task -> {
//
//                    if(task.isSuccessful()){
////
////                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
//                    }
//        });


        Map<String, Object> unsubscribe = new HashMap<>();
        unsubscribe.put("organizerID", FieldValue.delete());
        FirebaseFirestore.getInstance()
                .collection("user")
                .document(user)
                .update(unsubscribe).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        showToast("Successfully unsubscribed from Organization", Toast.LENGTH_SHORT);
                    }else{
                        showToast("Unsubscription Unsuccessful", Toast.LENGTH_SHORT);
                    }
        });
    }

    public void goToEdit(View view){
        Intent intent = new Intent(this , Attendance_user_profile_edit.class);
        startActivity(intent);
    }

    public void logout(View view){

        sAuth.getInstance().signOut();
        Intent intent = new Intent(this, Attendance_Student_Login.class);
        startActivity(intent);

    }

}