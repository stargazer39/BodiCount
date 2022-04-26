package com.bodicount.organizer;

import com.bodicount.commons.User;
import com.bodicount.commons.UserTypes;

public class Organizer extends User {

    @Override
    public String getType() {
        return UserTypes.ORGANIZER;
    }
}
