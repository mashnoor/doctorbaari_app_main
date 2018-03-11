

package com.doctorbaari.android.models;


import com.google.gson.annotations.SerializedName;

public class Job {

    @SerializedName("id")

    private Integer id;
    @SerializedName("post_datetime")

    private String postDatetime;
    @SerializedName("date_to")

    private String dateTo;
    @SerializedName("date_from")

    private String dateFrom;
    @SerializedName("duration")

    private Integer duration;
    @SerializedName("userid")

    private String userid;
    @SerializedName("username")

    private String username;
    @SerializedName("place")

    private String place;
    @SerializedName("placelat")

    private String placelat;
    @SerializedName("placelon")

    private String placelon;
    @SerializedName("institute")

    private String institute;
    @SerializedName("details")

    private String details;
    @SerializedName("available")
    private Integer available;

    @SerializedName("deadline")
    private String deadline;

    @SerializedName("type")
    private String type;

    @SerializedName("degree")
    private String degree;

    @SerializedName("user")
    private User user;

    @SerializedName("imagelink")
    private String imageLink;

    @SerializedName("distance")
    private String distance;

    public String getDistance() {
        return distance;
    }

    public String getImageLink() {
        return imageLink;
    }

    public User getUser() {
        return user;
    }

    public String getDeadline() {
        String[] dateParts = deadline.split("-");
        return dateParts[2] + "/" + dateParts[1] + "/" + dateParts[0];
    }

    public String getType() {
        return type;
    }

    public String getDegree() {
        return degree;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPostDatetime() {
        return postDatetime;
    }


    public String getDateTo() {
        String[] dateParts = dateTo.split("-");
        return dateParts[2] + "/" + dateParts[1] + "/" + dateParts[0];
    }


    public String getDateFrom() {
        return dateFrom;
    }


    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }


    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getPlacelat() {
        return placelat;
    }

    public void setPlacelat(String placelat) {
        this.placelat = placelat;
    }

    public String getPlacelon() {
        return placelon;
    }

    public void setPlacelon(String placelon) {
        this.placelon = placelon;
    }

    public String getInstitute() {
        return institute;
    }


    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Integer getAvailable() {
        return available;
    }

    public void setAvailable(Integer available) {
        this.available = available;
    }

}