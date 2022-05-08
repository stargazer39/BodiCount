package com.bodicount.timetable;

import com.google.firebase.firestore.Exclude;

public class Timetable {
    private String tableName;

    @Exclude
    private String id;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
