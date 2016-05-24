package hs_mannheim.mmobile.Model;

import java.util.Locale;

public class PersonalProfile {
    private String mFirstName;
    private String mLastName;
    private String mGender;
    private int mAge;

    public PersonalProfile(String firstName, String lastName, String gender, int age) {
        setFirstName(firstName);
        setLastName(lastName);
        setGender(gender);
        setAge(age);
    }

    public String toJSON() {
        return String.format(Locale.GERMANY,
                "{'first_name':'%s','last_name':'%s','gender':'%s','age':'%d'}",
                getFirstName(), getLastName(), getGender(), getAge());
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String mFirstName) {
        this.mFirstName = mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String mLastName) {
        this.mLastName = mLastName;
    }

    public String getGender() {
        return mGender;
    }

    public void setGender(String mGender) {
        this.mGender = mGender;
    }

    public int getAge() {
        return mAge;
    }

    public void setAge(int mAge) {
        this.mAge = mAge;
    }
}
