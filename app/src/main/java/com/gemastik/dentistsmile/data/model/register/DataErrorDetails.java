package com.gemastik.dentistsmile.data.model.register;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataErrorDetails {
    @SerializedName("email")
    private List<String> email;
    @SerializedName("password")
    private List<String> password;

    public List<String> getEmail() {
        return email;
    }

    public void setEmail(List<String> email) {
        this.email = email;
    }

    public List<String> getPassword() {
        return password;
    }

    public void setPassword(List<String> password) {
        this.password = password;
    }
}
