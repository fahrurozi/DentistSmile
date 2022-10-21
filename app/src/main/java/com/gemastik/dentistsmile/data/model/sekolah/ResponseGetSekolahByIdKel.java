package com.gemastik.dentistsmile.data.model.sekolah;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseGetSekolahByIdKel {
    @SerializedName("messages")
    private String messages;
    @SerializedName("data")
    private List<DataSekolah> data;

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public List<DataSekolah> getData() {
        return data;
    }

    public void setData(List<DataSekolah> data) {
        this.data = data;
    }
}
