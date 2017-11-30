

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

    private Object userid;
    @SerializedName("username")

    private String username;
    @SerializedName("place")

    private Object place;
    @SerializedName("placelat")

    private Object placelat;
    @SerializedName("placelon")

    private Object placelon;
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

    public Object getUserid() {
        return userid;
    }

    public void setUserid(Object userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Object getPlace() {
        return place;
    }

    public void setPlace(Object place) {
        this.place = place;
    }

    public Object getPlacelat() {
        return placelat;
    }

    public void setPlacelat(Object placelat) {
        this.placelat = placelat;
    }

    public Object getPlacelon() {
        return placelon;
    }

    public void setPlacelon(Object placelon) {
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