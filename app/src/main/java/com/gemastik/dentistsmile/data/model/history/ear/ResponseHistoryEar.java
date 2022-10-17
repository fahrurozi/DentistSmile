package com.gemastik.dentistsmile.data.model.history.ear;

import com.google.gson.annotations.SerializedName;

public class ResponseHistoryEar {
    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private DataHistoryEar data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataHistoryEar getData() {
        return data;
    }

    public void setData(DataHistoryEar data) {
        this.data = data;
    }
}
