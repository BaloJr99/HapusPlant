package com.example.hapusplant.models;

public class SucculentType {
    private String idSucculent, kind, documentsLink, photoLink, idUser, idSucculentFamily;

    private Boolean isEndemic, hasDocuments, isAlive;

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
