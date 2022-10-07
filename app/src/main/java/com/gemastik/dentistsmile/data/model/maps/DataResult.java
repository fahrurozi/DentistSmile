package com.gemastik.dentistsmile.data.model.maps;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataResult {
    @SerializedName("formatted_address")
    private String formattedAddress;
    @SerializedName("formatted_phone_number")
    private String formatted_phone_number;
    @SerializedName("geometry")
    private DataGeometry geometry;
    @SerializedName("name")
    private String name;
    @SerializedName("opening_hours")
    private DataOpeningHours opening_hours;
    @SerializedName("photos")
    private List<DataPhoto> photos;
    @SerializedName("types")
    private List<String> types;
    @SerializedName("url")
    private String url;
    @SerializedName("vicinity")
    private String vicinity;
    @SerializedName("website")
    private String website;

    public DataResult(String formattedAddress, String formatted_phone_number, DataGeometry geometry, String name, DataOpeningHours opening_hours, List<DataPhoto> photos, List<String> types, String url, String vicinity, String website) {
        this.formattedAddress = formattedAddress;
        this.formatted_phone_number = formatted_phone_number;
        this.geometry = geometry;
        this.name = name;
        this.opening_hours = opening_hours;
        this.photos = photos;
        this.types = types;
        this.url = url;
        this.vicinity = vicinity;
        this.website = website;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    public String getFormatted_phone_number() {
        return formatted_phone_number;
    }

    public void setFormatted_phone_number(String formatted_phone_number) {
        this.formatted_phone_number = formatted_phone_number;
    }

    public DataGeometry getGeometry() {
        return geometry;
    }

    public void setGeometry(DataGeometry geometry) {
        this.geometry = geometry;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DataOpeningHours getOpening_hours() {
        return opening_hours;
    }

    public void setOpening_hours(DataOpeningHours opening_hours) {
        this.opening_hours = opening_hours;
    }

    public List<DataPhoto> getPhotos() {
        return photos;
    }

    public void setPhotos(List<DataPhoto> photos) {
        this.photos = photos;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
