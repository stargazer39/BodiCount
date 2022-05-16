package com.bodicount.commons;

public class Attendance {
    private String dateTime;
    private String UserID;
    private String TimeTableID;
    private String TimeSlotID;
    private String OrganizerID;

    public String getDateTime() { return dateTime; }

    public void setDateTime(String dateTime) { this.dateTime = dateTime; }

    public String getUserID() { return UserID; }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getTimeTableID() { return TimeTableID; }

    public void setTimeTableID(String timeTableID) { TimeTableID = timeTableID; }

    public String getTimeSlotID() { return TimeSlotID; }

    public void setTimeSlotID(String timeSlotID) { TimeSlotID = timeSlotID; }

    public String getOrganizerID() { return OrganizerID; }

    public void setOrganizerID(String organizerID) { OrganizerID = organizerID; }
}
