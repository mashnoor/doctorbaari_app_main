package com.doctorbaari.android.models;

/**
 * Created by Nowfel Mashnoor on 12/2/2017.
 */

import com.google.gson.annotations.SerializedName;

public class Avaibility {

    @SerializedName("id")
    
    private Integer id;
    @SerializedName("post_datetime")
    
    private String postDatetime;
    @SerializedName("from_date")
    
    private String fromDate;
    @SerializedName("to_date")
    
    private String toDate;
    @SerializedName("place")
    
    private String place;
    @SerializedName("placelat")
    
    private String placelat;
    @SerializedName("placelon")
    
    private String placelon;
    @SerializedName("userid")
    
    private Integer userid;
    @SerializedName("available")
    
    private String available;

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

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
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

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

}
