package com.gemastik.dentistsmile.data.model.maps;

import com.google.gson.annotations.SerializedName;

public class DataLocation {
    @SerializedName("lat")
    private Double lat;
    @SerializedName("lng")
    private Double lng;

    public DataLocation(Double lat, Double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }
}
