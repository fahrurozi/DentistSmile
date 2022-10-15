package com.gemastik.dentistsmile.data.model.children.show;

import com.gemastik.dentistsmile.data.model.children.DataChildren;
import com.google.gson.annotations.SerializedName;

public class ResponseShowChildren {
    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private DataChildren data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataChildren getData() {
        return data;
    }

    public void setData(DataChildren data) {
        this.data = data;
    }
}
