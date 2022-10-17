package com.gemastik.dentistsmile.data.model.profil;

import com.google.gson.annotations.SerializedName;

public class ResponseEditProfile {
    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private DataEditProfile data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataEditProfile getData() {
        return data;
    }

    public void setData(DataEditProfile data) {
        this.data = data;
    }
}
