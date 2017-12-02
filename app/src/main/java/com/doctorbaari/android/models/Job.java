

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

    public String getDeadline() {
        return deadline;
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

    public void setPostDatetime(String postDatetime) {
        this.postDatetime = postDatetime;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
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

    public void setUsername(String username) {
        this.username = username;
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

    public void setInstitute(String institute) {
        this.institute = institute;
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