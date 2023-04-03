package com.example.hapusplant.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserModel {
    @SerializedName("idUser")
    @Expose
    private String idUser;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("jwt")
    @Expose
    private String jwt;
    @SerializedName("role")
    @Expose
    private String role;
    @SerializedName("idPersonalData")
    @Expose
    private String idPersonalData;
    @SerializedName("isActive")
    @Expose
    private Boolean isActive;

    public UserModel(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public UserModel(String username, String password, String role, Boolean isActive) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.isActive = isActive;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getIdPersonalData() {
        return idPersonalData;
    }

    public void setIdPersonalData(String idPersonalData) {
        this.idPersonalData = idPersonalData;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
