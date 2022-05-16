package com.bodicount.student;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bodicount.Attendance_homepage;
import com.bodicount.Helpers4Dehemi;
import com.bodicount.Helpers4Nithula;
import com.bodicount.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class Attendance_user_profile_edit extends AppCompatActivity {
    private FirebaseAuth sAuth;
    private FirebaseUser user;
    private FirebaseFirestore db;
    private Uri currentImgUri;

    private String gender;

    private Student currentStudent;

    private ActivityResultLauncher<String> mGetContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_user_profile_edit);

        db = FirebaseFirestore.getInstance();
        sAuth = FirebaseAuth.getInstance();
        user = sAuth.getCurrentUser();

        db.collection("user").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                DocumentSnapshot snap = task.getResult();
                Student student = snap.toObject(Student.class);

                currentStudent = student;

                String email = student.getEmail();
                String fName = student.getfName();
                String lName = student.getlName();
                String phone = student.getPhone();
                String gender = student.getGender();

                EditText firstName = (EditText) findViewById(R.id.userProfileEditFname);
                firstName.setText(fName);
                EditText userName = (EditText) findViewById(R.id.userProfileEditLname);
                userName.setText(lName);
                EditText userEmail = (EditText) findViewById(R.id.userProfileEditEmail);
                userEmail.setText(email);
                EditText userPhone = (EditText) findViewById(R.id.userProfileEditPhone);
                userPhone.setText(Helpers4Nithula.checkIfEmpty(phone));
//                EditText userGender = (EditText) findViewById(R.id.userProfileEditGender);
//                userGender.setText(Helpers4Nithula.checkIfEmpty(gender));
//                RadioButton gender1 = (RadioButton) findViewById()

                //supportActionBar?.setDisplayHomeAsUpEnabled(true);
                //other stuff
            }else{
                //deal with error
                task.getException().printStackTrace();
            }

        });

        //To get image from gallery to imageview
         mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {
                        // Handle the returned Uri
                        currentImgUri = uri;
                        Log.d("img", uri.toString());

                        ImageView imageView = (ImageView) findViewById(R.id.editPic);
                        imageView.setImageURI(currentImgUri);
                    }

                });
    }


    public void getImageFromAlbum(View view){
        try{

            mGetContent.launch("image/*");
        }catch(Exception exp){
            Log.i("Error",exp.toString());
        }
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioMale:
                if (checked)
                    gender = "male";
                    break;
            case R.id.radioFemale:
                if (checked)
                    gender = "female";
                    break;
        }
    }

    private void showToast(String message, int duration){
        Toast toast = Toast.makeText(this, message, duration);
        toast.show();
    }

    public String encodeToBase64(Uri uri) throws IOException {
        // Initialize bitmap
        Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
        // initialize byte stream
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        // compress Bitmap
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
        // Initialize byte array
        byte[] bytes=stream.toByteArray();
        // get base64 encoded string
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    public void updateData(View view){
        EditText firstName = (EditText) findViewById(R.id.userProfileEditFname);
        EditText userName = (EditText) findViewById(R.id.userProfileEditLname);
        EditText userEmail = (EditText) findViewById(R.id.userProfileEditEmail);
        EditText userPhone = (EditText) findViewById(R.id.userProfileEditPhone);

        try{
            currentStudent.setfName(Helpers4Dehemi.vaildateString(firstName, true, "First Name"));
            currentStudent.setlName(Helpers4Dehemi.vaildateString(userName, true, "Last Name"));
            currentStudent.setEmail(Helpers4Dehemi.vaildateString(userEmail, true, "Email"));
            currentStudent.setPhone(Helpers4Dehemi.vaildateString(userPhone, true, "Phone"));
            currentStudent.setGender(gender);

            if(currentImgUri != null)
                currentStudent.setImage(encodeToBase64(currentImgUri));


            db.collection("user")
                    .document(sAuth.getCurrentUser().getUid())
                    .set(currentStudent)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                showToast("Successfully updated", Toast.LENGTH_SHORT);
                            }else{
                                showToast("Update failed", Toast.LENGTH_SHORT);
                            }
                        }
                    });
        }catch(Exception e){
            showToast(e.getMessage(), Toast.LENGTH_SHORT);
        }
    }

    public void backBtn2(View view){
        Intent intent = new Intent(this, Attendance_User_Profile.class);
        startActivity(intent);
    }
}