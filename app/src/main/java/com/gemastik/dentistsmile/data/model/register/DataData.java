package com.gemastik.dentistsmile.data.model.register;

import com.google.gson.annotations.SerializedName;

public class DataData {

    @SerializedName("user")
    private DataUser user;

    @SerializedName("token")
    private String token;

    public DataUser getUser() {
        return user;
    }

    public void setUser(DataUser user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
