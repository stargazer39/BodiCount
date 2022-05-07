package com.bodicount.timeslot;

import com.google.type.DateTime;

public class Timeslot {
    private String slotName;
    private String startTime;
    private String endTime;
    private boolean heldOnline;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getSlotName() {
        return slotName;
    }

    public void setSlotName(String slotName) {
        this.slotName = slotName;
    }

    public boolean isHeldOnline() {
        return heldOnline;
    }

    public void setHeldOnline(boolean heldOnline) {
        this.heldOnline = heldOnline;
    }
}
