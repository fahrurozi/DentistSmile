package com.gemastik.dentistsmile.data.model.profil;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseGetProfile {
    @SerializedName("messages")
    private String messages;

    @SerializedName("data")
    private List<DataProfil> data;

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public List<DataProfil> getData() {
        return data;
    }

    public void setData(List<DataProfil> data) {
        this.data = data;
    }
}
