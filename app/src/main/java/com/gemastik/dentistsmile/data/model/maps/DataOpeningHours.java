package com.gemastik.dentistsmile.data.model.maps;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataOpeningHours {
    @SerializedName("open_now")
    private Boolean openNow;

    @SerializedName("weekday_text")
    private List<String> weekday_text;

    public Boolean getOpenNow() {
        return openNow;
    }

    public void setOpenNow(Boolean openNow) {
        this.openNow = openNow;
    }

    public List<String> getWeekday_text() {
        return weekday_text;
    }

    public void setWeekday_text(List<String> weekday_text) {
        this.weekday_text = weekday_text;
    }
}
