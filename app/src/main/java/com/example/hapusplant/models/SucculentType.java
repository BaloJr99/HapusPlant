package com.example.hapusplant.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SucculentType {
    @SerializedName("idSucculent")
    @Expose
    private String idSucculent;
    @SerializedName("kind")
    @Expose
    private String kind;
    @SerializedName("documentsLink")
    @Expose
    private String documentsLink;
    @SerializedName("photoLink")
    @Expose
    private String photoLink;
    @SerializedName("idUser")
    @Expose
    private String idUser;
    @SerializedName("idSucculentFamily")
    @Expose
    private String idSucculentFamily;

    @SerializedName("isEndemic")
    @Expose
    private Boolean isEndemic;
    @SerializedName("hasDocuments")
    @Expose
    private Boolean hasDocuments;
    @SerializedName("isAlive")
    @Expose
    private Boolean isAlive;

    public SucculentType(String kind, String documentsLink, String photoLink, String idSucculentFamily, Boolean isEndemic, Boolean hasDocuments, Boolean isAlive) {
        this.kind = kind;
        this.documentsLink = documentsLink;
        this.photoLink = photoLink;
        this.idSucculentFamily = idSucculentFamily;
        this.isEndemic = isEndemic;
        this.hasDocuments = hasDocuments;
        this.isAlive = isAlive;
    }

    public String getIdSucculent() {
        return idSucculent;
    }

    public void setIdSucculent(String idSucculent) {
        this.idSucculent = idSucculent;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getDocumentsLink() {
        return documentsLink;
    }

    public void setDocumentsLink(String documentsLink) {
        this.documentsLink = documentsLink;
    }

    public String getPhotoLink() {
        return photoLink;
    }

    public void setPhotoLink(String photoLink) {
        this.photoLink = photoLink;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdSucculentFamily() {
        return idSucculentFamily;
    }

    public void setIdSucculentFamily(String idSucculentFamily) {
        this.idSucculentFamily = idSucculentFamily;
    }

    public Boolean getEndemic() {
        return isEndemic;
    }

    public void setEndemic(Boolean endemic) {
        isEndemic = endemic;
    }

    public Boolean getHasDocuments() {
        return hasDocuments;
    }

    public void setHasDocuments(Boolean hasDocuments) {
        this.hasDocuments = hasDocuments;
    }

    public Boolean getAlive() {
        return isAlive;
    }

    public void setAlive(Boolean alive) {
        isAlive = alive;
    }
}
