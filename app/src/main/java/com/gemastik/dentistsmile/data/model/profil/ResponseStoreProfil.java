package com.gemastik.dentistsmile.data.model.profil;

import com.google.gson.annotations.SerializedName;

public class ResponseStoreProfil {
    @SerializedName("message")
    private String messages;

    @SerializedName("data")
    private DataProfil data;

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public DataProfil getData() {
        return data;
    }

    public void setData(DataProfil data) {
        this.data = data;
    }
}
