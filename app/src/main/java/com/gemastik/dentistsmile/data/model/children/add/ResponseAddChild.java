package com.gemastik.dentistsmile.data.model.children.add;

import com.google.gson.annotations.SerializedName;

public class ResponseAddChild {
    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private DataAddChild data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataAddChild getData() {
        return data;
    }

    public void setData(DataAddChild data) {
        this.data = data;
    }
}
