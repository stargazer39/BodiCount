package com.bodicount.commons;

public abstract  class User {
    private String fName;
    private String lName;
    private String email;
    private String password;
    protected String type;
    private String phone;
    private String gender;
    private String dob;

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() { return phone; }

    public void setPhone(String phone) { this.phone = phone; }

    public String getGender() { return gender; }

    public void setGender(String gender) { this.gender = gender; }

    public String getDob() { return dob; }

    public void setDob(String dob) { this.dob = dob; }

    abstract public String getType();
}
