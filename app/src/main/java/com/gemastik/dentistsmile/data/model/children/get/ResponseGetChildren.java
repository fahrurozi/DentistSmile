package com.gemastik.dentistsmile.data.model.children.get;

import com.gemastik.dentistsmile.data.model.children.DataChildren;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseGetChildren {
    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private List<DataChildren> data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataChildren> getData() {
        return data;
    }

    public void setData(List<DataChildren> data) {
        this.data = data;
    }
}
