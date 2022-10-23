package com.gemastik.dentistsmile.data.model.register;

import com.google.gson.annotations.SerializedName;

public class ResponseRegister {
    @SerializedName("code")
    private Integer code;

    @SerializedName("message")
    private String message;

    @SerializedName("user")
    private DataUser user;

    @SerializedName("orangtua")
    private DataOrangTua orangtua;

    @SerializedName("token")
    private String token;


    @SerializedName("error")
    private String error;
    @SerializedName("errorDetails")
    private DataErrorDetails errorDetails;

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


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public DataErrorDetails getErrorDetails() {
        return errorDetails;
    }

    public void setErrorDetails(DataErrorDetails errorDetails) {
        this.errorDetails = errorDetails;
    }
}
