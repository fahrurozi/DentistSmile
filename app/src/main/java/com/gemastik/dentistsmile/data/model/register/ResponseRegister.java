package com.gemastik.dentistsmile.data.model.register;

import com.google.gson.annotations.SerializedName;

public class ResponseRegister {
    @SerializedName("message")
    private String message;

    @SerializedName("user")
    private DataUser user;

    @SerializedName("orangtua")
    private DataOrangTua orangtua;

    @SerializedName("token")
    private String token;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataUser getUser() {
        return user;
    }

    public void setUser(DataUser user) {
        this.user = user;
    }

    public DataOrangTua getOrangtua() {
        return orangtua;
    }

    public void setOrangtua(DataOrangTua orangtua) {
        this.orangtua = orangtua;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
