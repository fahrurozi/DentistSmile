package com.gemastik.dentistsmile.data.model.review;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseReview {
    @SerializedName("reviews")
    private List<DataReview> reviews;

    @SerializedName("review_count")
    private Integer reviewCount;

    @SerializedName("count_per_rating")
    private DataPerRating dataPerRating;

    public List<DataReview> getReviews() {
        return reviews;
    }

    public void setReviews(List<DataReview> reviews) {
        this.reviews = reviews;
    }

    public Integer getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(Integer reviewCount) {
        this.reviewCount = reviewCount;
    }

    public DataPerRating getDataPerRating() {
        return dataPerRating;
    }

    public void setDataPerRating(DataPerRating dataPerRating) {
        this.dataPerRating = dataPerRating;
    }
}
