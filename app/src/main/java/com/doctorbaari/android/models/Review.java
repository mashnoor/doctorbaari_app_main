
package com.doctorbaari.android.models;

import com.google.gson.annotations.SerializedName;

public class Review {

    @SerializedName("id")
    
    private Integer id;
    @SerializedName("post_datetime")
    
    private String postDatetime;
    @SerializedName("review")
    
    private String review;
    @SerializedName("reviewed_from")
    
    private String reviewedFrom;
    @SerializedName("reviewed_to")
    
    private String reviewedTo;
    @SerializedName("reviewer_name")
    
    private String reviewerName;
    @SerializedName("rating")
    
    private String rating;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPostDatetime() {
        return postDatetime;
    }

    public void setPostDatetime(String postDatetime) {
        this.postDatetime = postDatetime;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getReviewedFrom() {
        return reviewedFrom;
    }

    public void setReviewedFrom(String reviewedFrom) {
        this.reviewedFrom = reviewedFrom;
    }

    public String getReviewedTo() {
        return reviewedTo;
    }

    public void setReviewedTo(String reviewedTo) {
        this.reviewedTo = reviewedTo;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

}