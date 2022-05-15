package com.bodicount;

import android.view.View;
import android.widget.EditText;

import com.bodicount.timeslot.Timeslot;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Helpers4Dehemi {
    public static String[] days = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" };
    public static int[] dayConstant = { DateTimeConstants.MONDAY, DateTimeConstants.TUESDAY, DateTimeConstants.WEDNESDAY, DateTimeConstants.THURSDAY, DateTimeConstants.FRIDAY, DateTimeConstants.SATURDAY, DateTimeConstants.SUNDAY };
    private static DateTimeFormatter fmt = DateTimeFormat.forPattern("HH:mm:ss.SSSSS");
    private static DateTimeFormatter timeFormatter = DateTimeFormat.forPattern("hh:mm a ");

    public static String vaildateString(String str, boolean trim, String tag) throws Exception {
        String new_string;

        if(trim)
            new_string = str.trim();
        else
            new_string = str;

        if(str.length() <= 0)
            throw new Exception(tag + " Cannot be empty");

        return new_string;
    }

    public  static String vaildateString(EditText editText, boolean trim, String tag) throws Exception {
        String str = editText.getText().toString();
        return vaildateString(str, trim, tag);
    }

    public static long convertToMillis(int hour, int minute){
        return  (hour*60*60 + minute*60)*1000;
    }

    public static LocalTime parseLocalTime(String timeString){
        return fmt.parseLocalTime(timeString);
    }

    public static String timeslotToString(Timeslot timeslot){
        LocalTime sTime = parseLocalTime(timeslot.getStartTime());
        LocalTime eTime = parseLocalTime(timeslot.getEndTime());

        return "From - " + sTime.toString(timeFormatter) + " To " + eTime.toString(timeFormatter).toString();
    }
}
