package com.gemastik.dentistsmile.data.model.maps;

import com.google.gson.annotations.SerializedName;

public class DataDbData {
    @SerializedName("avg_rating")
    private Float avg_rating;

    @SerializedName("desc")
    private String desc;

    @SerializedName("gmap_place_id")
    private String gmap_place_id;

    @SerializedName("id")
    private Integer id;

    @SerializedName("location_lat")
    private Double location_lat;

    @SerializedName("location_lng")
    private Double location_lng;

    @SerializedName("phone")
    private String phone;

    @SerializedName("place_name")
    private String place_name;

    public DataDbData(Float avg_rating, String desc, String gmap_place_id, Integer id, Double location_lat, Double location_lng, String phone, String place_name) {
        this.avg_rating = avg_rating;
        this.desc = desc;
        this.gmap_place_id = gmap_place_id;
        this.id = id;
        this.location_lat = location_lat;
        this.location_lng = location_lng;
        this.phone = phone;
        this.place_name = place_name;
    }

    public Float getAvg_rating() {
        return avg_rating;
    }

    public void setAvg_rating(Float avg_rating) {
        this.avg_rating = avg_rating;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getGmap_place_id() {
        return gmap_place_id;
    }

    public void setGmap_place_id(String gmap_place_id) {
        this.gmap_place_id = gmap_place_id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getLocation_lat() {
        return location_lat;
    }

    public void setLocation_lat(Double location_lat) {
        this.location_lat = location_lat;
    }

    public Double getLocation_lng() {
        return location_lng;
    }

    public void setLocation_lng(Double location_lng) {
        this.location_lng = location_lng;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPlace_name() {
        return place_name;
    }

    public void setPlace_name(String place_name) {
        this.place_name = place_name;
    }
}
