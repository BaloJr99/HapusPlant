package com.example.hapusplant.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class ProfileModel {

    @SerializedName("idPersonalData")
    @Expose
    private String IdPersonalData;
    @SerializedName("name")
    @Expose
    private String Name;
    @SerializedName("lastName")
    @Expose
    private String LastName;
    @SerializedName("photo")
    @Expose
    private String Photo;
    @SerializedName("birthday")
    @Expose
    private String Birthday;

    public ProfileModel(String idPersonalData, String name, String lastName, String photo, String birthday) {
        IdPersonalData = idPersonalData;
        Name = name;
        LastName = lastName;
        Photo = photo;
        Birthday = birthday;
    }

    public ProfileModel(String name, String lastName, String photo, String birthday) {
        Name = name;
        LastName = lastName;
        Photo = photo;
        Birthday = birthday;
    }
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

    public String getBirthday() {
        return Birthday;
    }

    public void setBirthday(String birthday) {
        Birthday = birthday;
    }
}
