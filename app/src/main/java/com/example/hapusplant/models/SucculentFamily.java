package com.example.hapusplant.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SucculentFamily {
    @SerializedName("idSucculentFamily")
    @Expose
    private String idSucculentFamily;
    @SerializedName("family")
    @Expose
    private String family;
    @SerializedName("idUser")
    @Expose
    private String idUser;

    public SucculentFamily(String family) {
        this.family = family;
    }

    public String getIdSucculentFamily() {
        return idSucculentFamily;
    }

    public void setIdSucculentFamily(String idSucculentFamily) {
        this.idSucculentFamily = idSucculentFamily;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }
}
