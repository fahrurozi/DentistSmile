package com.gemastik.dentistsmile.data.model.history.eye;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseHistoryEye {
    @SerializedName("messages")
    private String messages;

    @SerializedName("data")
    private List<DataHistoryEye> data;

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public List<DataHistoryEye> getData() {
        return data;
    }

    public void setData(List<DataHistoryEye> data) {
        this.data = data;
    }
}
