package com.gemastik.dentistsmile.data.model.token;

import com.google.gson.annotations.SerializedName;

public class ResponseToken {
    @SerializedName("status")
    private Integer status;

    @SerializedName("token")
    private String token;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
