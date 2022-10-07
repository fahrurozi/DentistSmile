package com.gemastik.dentistsmile.data.model.review;

import com.google.gson.annotations.SerializedName;

public class ResponseAddReview {
    @SerializedName("success")
    private Boolean success;

    @SerializedName("review")
    private DataReview review;

    @SerializedName("new_avg_rating")
    private String newAvgRating;

    public ResponseAddReview(Boolean success, DataReview review, String newAvgRating) {
        this.success = success;
        this.review = review;
        this.newAvgRating = newAvgRating;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public DataReview getReview() {
        return review;
    }

    public void setReview(DataReview review) {
        this.review = review;
    }

    public String getNewAvgRating() {
        return newAvgRating;
    }

    public void setNewAvgRating(String newAvgRating) {
        this.newAvgRating = newAvgRating;
    }
}
