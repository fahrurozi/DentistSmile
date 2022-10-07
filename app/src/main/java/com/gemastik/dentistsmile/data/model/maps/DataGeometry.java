package com.gemastik.dentistsmile.data.model.maps;

import com.google.gson.annotations.SerializedName;

public class DataGeometry {
    @SerializedName("location")
    DataLocation location;

    public DataGeometry(DataLocation location) {
        this.location = location;
    }

    public DataLocation getLocation() {
        return location;
    }

    public void setLocation(DataLocation location) {
        this.location = location;
    }
}
