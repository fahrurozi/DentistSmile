package com.gemastik.dentistsmile.data.model.kecamatan;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseGetKecamatanAll {
    @SerializedName("messages")
    private String messages;
    @SerializedName("data")
    private List<DataKecamatan> data;

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public List<DataKecamatan> getData() {
        return data;
    }

    public void setData(List<DataKecamatan> data) {
        this.data = data;
    }
}
