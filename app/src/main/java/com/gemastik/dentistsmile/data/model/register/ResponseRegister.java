package com.gemastik.dentistsmile.data.model.register;

import com.google.gson.annotations.SerializedName;

public class ResponseRegister {
    @SerializedName("code")
    private Integer code;

    @SerializedName("data")
    private DataData data;

    @SerializedName("message")
    private String message;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public DataData getData() {
        return data;
    }

    public void setData(DataData data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
