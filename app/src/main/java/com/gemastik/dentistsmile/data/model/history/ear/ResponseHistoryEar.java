package com.gemastik.dentistsmile.data.model.history.ear;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseHistoryEar {
    @SerializedName("messages")
    private String messages;

    @SerializedName("data")
    private List<DataHistoryEar> data;

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public List<DataHistoryEar> getData() {
        return data;
    }

    public void setData(List<DataHistoryEar> data) {
        this.data = data;
    }
}
