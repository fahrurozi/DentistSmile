package com.gemastik.dentistsmile.data.model.checkup_dentist;

import com.google.gson.annotations.SerializedName;

public class ResponseCheckupDentist {
    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private DataCheckupDentist data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataCheckupDentist getData() {
        return data;
    }

    public void setData(DataCheckupDentist data) {
        this.data = data;
    }
}
