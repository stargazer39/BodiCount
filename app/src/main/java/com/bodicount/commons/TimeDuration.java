package com.bodicount.commons;

public class TimeDuration {
    private int minutes;
    private int hours;

    public TimeDuration(String durationString){
        String range[] = durationString.split(":");

        hours = Integer.parseInt(range[0]);
        minutes = Integer.parseInt(range[1]);
    }

    public TimeDuration(int hours, int minutes){
        this.hours = hours;
        this.minutes = minutes;
    }

    @Override
    public String toString() {
        return String.format("%02d", hours) + ":" + String.format("%02d", minutes);
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }
}
