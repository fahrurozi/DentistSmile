package com.gemastik.dentistsmile.data.model.kelas;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseGetKelasByIdSek {
    @SerializedName("messages")
    private String messages;
    @SerializedName("data")
    private List<DataKelas> data;

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public List<DataKelas> getData() {
        return data;
    }

    public void setData(List<DataKelas> data) {
        this.data = data;
    }
}
