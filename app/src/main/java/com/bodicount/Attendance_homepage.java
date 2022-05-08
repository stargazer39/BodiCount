package com.bodicount;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.bodicount.student.Attendance_User_Profile;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Attendance_homepage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_homepage);


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

//        ActionBar actionBar = getActionBar();
//        if(actionBar == null)
//            //actionBar = getActionBarSupport();
//        actionBar.setDisplayHomeAsUpEnabled(true);

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


}