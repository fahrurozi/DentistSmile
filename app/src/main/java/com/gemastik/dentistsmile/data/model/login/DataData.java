package com.gemastik.dentistsmile.data.model.login;

import com.google.gson.annotations.SerializedName;

public class DataData {
    @SerializedName("email")
    private String email;

    @SerializedName("email")
    private String token;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
