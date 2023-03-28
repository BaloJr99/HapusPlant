package com.example.hapusplant.models;

import java.util.Date;

public class ProfileModel {
    private String IdPersonalData, Name, LastName, Photo;
    private Date Birthday;

    public String getIdPersonalData() {
        return IdPersonalData;
    }

    public void setIdPersonalData(String idPersonalData) {
        IdPersonalData = idPersonalData;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }

    public Date getBirthday() {
        return Birthday;
    }

    public void setBirthday(Date birthday) {
        Birthday = birthday;
    }
}
