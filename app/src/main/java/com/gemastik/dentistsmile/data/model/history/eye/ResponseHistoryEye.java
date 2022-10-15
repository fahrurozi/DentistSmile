package com.gemastik.dentistsmile.data.model.history.eye;

import com.google.gson.annotations.SerializedName;

public class ResponseHistoryEye {
    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private DataHistoryEye data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataHistoryEye getData() {
        return data;
    }

    public void setData(DataHistoryEye data) {
        this.data = data;
    }
}
