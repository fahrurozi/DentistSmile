package com.gemastik.dentistsmile.data.model.checkup_physic;

import com.google.gson.annotations.SerializedName;

public class ResponseCheckupPhysic {
    @SerializedName("messages")
    private String messages;

    @SerializedName("pemeriksaan_fisik")
    private DataPhysics pemeriksaan_fisik;

    @SerializedName("pemeriksaan_mata")
    private DataEye pemeriksaan_mata;

    @SerializedName("pemeriksaan_telinga")
    private DataEar pemeriksaan_telinga;

    public String getMessage() {
        return messages;
    }

    public void setMessage(String message) {
        this.messages = message;
    }

    public DataPhysics getPemeriksaan_fisik() {
        return pemeriksaan_fisik;
    }

    public void setPemeriksaan_fisik(DataPhysics pemeriksaan_fisik) {
        this.pemeriksaan_fisik = pemeriksaan_fisik;
    }

    public DataEye getPemeriksaan_mata() {
        return pemeriksaan_mata;
    }

    public void setPemeriksaan_mata(DataEye pemeriksaan_mata) {
        this.pemeriksaan_mata = pemeriksaan_mata;
    }

    public DataEar getPemeriksaan_telinga() {
        return pemeriksaan_telinga;
    }

    public void setPemeriksaan_telinga(DataEar pemeriksaan_telinga) {
        this.pemeriksaan_telinga = pemeriksaan_telinga;
    }
}
