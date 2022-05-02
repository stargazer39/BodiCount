package com.bodicount.student;

import com.bodicount.commons.User;
import com.bodicount.commons.UserTypes;

public class Student extends User {
    public Student () {
        super();
        this.type = UserTypes.STUDENT;
    }
    @Override
    public String getType() {
        return UserTypes.STUDENT;
    }
}
