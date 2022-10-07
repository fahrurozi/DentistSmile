package com.gemastik.dentistsmile.data.model.review;


import com.google.gson.annotations.SerializedName;

public class DataReview {
    @SerializedName("id")
    private Integer id;
    @SerializedName("stunt_place")
    private Integer stunt_place;
    @SerializedName("user")
    private String user;
    @SerializedName("rating")
    private Integer rating;
    @SerializedName("desc")
    private String desc;



    public DataReview(Integer id, Integer stunt_place, String user, Integer rating, String desc) {
        this.id = id;
        this.stunt_place = stunt_place;
        this.user = user;
        this.rating = rating;
        this.desc = desc;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStunt_place() {
        return stunt_place;
    }

    public void setStunt_place(Integer stunt_place) {
        this.stunt_place = stunt_place;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


}
