package com.gemastik.dentistsmile.data.model.history.physic;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseHistoryPhysic {
    @SerializedName("messages")
    private String messages;

    @SerializedName("data")
    private List<DataHistoryPhysic> data;

    public String getMessages() {
        return messages;
    }

    public void setMessages(String message) {
        this.messages = message;
    }

    public List<DataHistoryPhysic> getData() {
        return data;
    }

    public void setData(List<DataHistoryPhysic> data) {
        this.data = data;
    }
}
