package com.gemastik.dentistsmile.data.model.children.edit;

import com.gemastik.dentistsmile.data.model.children.add.DataAddChild;
import com.google.gson.annotations.SerializedName;

public class ResponseEditChild {
    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private DataEditChild data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataEditChild getData() {
        return data;
    }

    public void setData(DataEditChild data) {
        this.data = data;
    }
}
