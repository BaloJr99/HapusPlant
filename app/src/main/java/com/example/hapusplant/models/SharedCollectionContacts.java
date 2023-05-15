package com.example.hapusplant.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SharedCollectionContacts {
    @SerializedName("idUser")
    @Expose
    private String idUser;
    @SerializedName("fullName")
    @Expose
    private String fullName;
    @SerializedName("photo")
    @Expose
    private String photo;

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
