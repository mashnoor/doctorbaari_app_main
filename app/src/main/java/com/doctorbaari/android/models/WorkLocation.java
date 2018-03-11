
package com.doctorbaari.android.models;

import com.google.gson.annotations.SerializedName;

public class WorkLocation {

    @SerializedName("id")
    private Integer id;

    @SerializedName("place")
    private String place;

    @SerializedName("placelat")
    private String placelat;

    @SerializedName("placelon")
    private String placelon;

    @SerializedName("userid")
    private Integer userid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

}