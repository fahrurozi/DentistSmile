package com.gemastik.dentistsmile.data.model.checkup_dentist;

import com.google.gson.annotations.SerializedName;

public class ResponseCheckupDentist {
    @SerializedName("messages")
    private String messages;

    @SerializedName("data")
    private DataCheckupDentist data;

    public String getMessages() {
        return messages;
    }

    public void setMessage(String messages) {
        this.messages = messages;
    }

    public DataCheckupDentist getData() {
        return data;
    }

    public void setData(DataCheckupDentist data) {
        this.data = data;
    }
}
