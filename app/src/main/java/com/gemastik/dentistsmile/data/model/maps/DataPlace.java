package com.gemastik.dentistsmile.data.model.maps;

import com.google.gson.annotations.SerializedName;

public class DataPlace {
    @SerializedName("db_data")
    private DataDbData dbData;

    @SerializedName("place_detail")
    private DataPlaceDetail placeDetail;

    public DataPlaceDetail getPlaceDetail() {
        return placeDetail;
    }

    public void setPlaceDetail(DataPlaceDetail placeDetail) {
        this.placeDetail = placeDetail;
    }

    public DataDbData getDbData() {
        return dbData;
    }

    public void setDbData(DataDbData dbData) {
        this.dbData = dbData;
    }
}
