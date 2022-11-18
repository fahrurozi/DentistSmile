package com.gemastik.dentistsmile.data.model.reminder;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseReminder {
    @SerializedName("messages")
    private String messages;
    @SerializedName("data")
    private List<DataReminder> data;

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public List<DataReminder> getData() {
        return data;
    }

    public void setData(List<DataReminder> data) {
        this.data = data;
    }
}
