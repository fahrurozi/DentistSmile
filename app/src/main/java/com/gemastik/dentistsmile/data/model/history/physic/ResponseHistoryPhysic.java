package com.gemastik.dentistsmile.data.model.history.physic;

import com.google.gson.annotations.SerializedName;

public class ResponseHistoryPhysic {
    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private DataHistoryPhysic data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataHistoryPhysic getData() {
        return data;
    }

    public void setData(DataHistoryPhysic data) {
        this.data = data;
    }
}
