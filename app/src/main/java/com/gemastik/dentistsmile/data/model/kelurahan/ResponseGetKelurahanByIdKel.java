package com.gemastik.dentistsmile.data.model.kelurahan;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseGetKelurahanByIdKel {
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private List<DataKelurahan> data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataKelurahan> getData() {
        return data;
    }

    public void setData(List<DataKelurahan> data) {
        this.data = data;
    }
}
