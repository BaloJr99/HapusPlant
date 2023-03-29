package com.example.hapusplant.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewUser {
    @SerializedName("personalDatum")
    @Expose
    private ProfileModel personal;

    @SerializedName("user")
    @Expose
    private UserModel user;

    public NewUser(ProfileModel personal, UserModel user) {
        this.personal = personal;
        this.user = user;
    }

    public ProfileModel getPersonal() {
        return personal;
    }

    public void setPersonal(ProfileModel personal) {
        this.personal = personal;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }
}
