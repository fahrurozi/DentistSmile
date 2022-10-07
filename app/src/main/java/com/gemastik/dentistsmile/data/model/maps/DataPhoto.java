package com.gemastik.dentistsmile.data.model.maps;

import com.google.gson.annotations.SerializedName;

public class DataPhoto {
    @SerializedName("height")
    private Integer height;
    @SerializedName("photo_reference")
    private String photoReference;

    public DataPhoto(Integer height, String photoReference) {
        this.height = height;
        this.photoReference = photoReference;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getPhotoReference() {
        return photoReference;
    }

    public void setPhotoReference(String photoReference) {
        this.photoReference = photoReference;
    }
}
