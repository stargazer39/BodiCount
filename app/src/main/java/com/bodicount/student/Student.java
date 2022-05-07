package com.bodicount.student;

import com.bodicount.commons.User;
import com.bodicount.commons.UserTypes;

public class Student extends User {
    private String organizerID;
    private String image;
    public Student () {
        super();
        this.type = UserTypes.STUDENT;
    }
    @Override
    public String getType() {
        return UserTypes.STUDENT;
    }

    public String getImage() { return image; }

    public void setImage(String image) {
        this.image = image;
    }

    public String getOrganizerID() {
        return organizerID;
    }

    public void setOrganizerID(String organizerID) {
        this.organizerID = organizerID;
    }
}
