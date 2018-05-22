package com.doctorbaari.android.models;

/**
 * Created by Nowfel Mashnoor on 12/1/2017.
 */

import com.google.gson.annotations.SerializedName;

public class DoctorSub {

    @SerializedName("id")
    
    private String id;
    @SerializedName("created_at")
    
    private String createdAt;
    @SerializedName("username")
    
    private String username;
    @SerializedName("email")
    
    private String email;
    @SerializedName("phone")
    
    private String phone;
    @SerializedName("fb_profile")
    
    private String fbProfile;
    @SerializedName("pp_url")
    
    private String ppUrl;
    @SerializedName("birthdate")
    
    private String birthdate;
    @SerializedName("college")
    
    private String college;
    @SerializedName("place")
    
    private String place;
    @SerializedName("mbbs_reg")
    
    private String mbbsReg;
    @SerializedName("degree")
    
    private String degree;
    @SerializedName("placelat")
    
    private String placelat;
    @SerializedName("placelon")
    
    private String placelon;
    @SerializedName("available")
    
    private String available;
    @SerializedName("from_date")
    
    private String fromDate;
    @SerializedName("to_date")
    
    private String toDate;
    @SerializedName("type")
    
    private String type;

    @SerializedName("distance")
    private String distance;

    public String getDistance() {
        return distance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFbProfile() {
        return fbProfile;
    }

    public void setFbProfile(String fbProfile) {
        this.fbProfile = fbProfile;
    }

    public String getPpUrl() {
        return ppUrl;
    }

    public void setPpUrl(String ppUrl) {
        this.ppUrl = ppUrl;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getMbbsReg() {
        return mbbsReg;
    }

    public void setMbbsReg(String mbbsReg) {
        this.mbbsReg = mbbsReg;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
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

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
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

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "DoctorSub{" +
                "id=" + id +
                ", createdAt='" + createdAt + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", fbProfile='" + fbProfile + '\'' +
                ", ppUrl='" + ppUrl + '\'' +
                ", birthdate='" + birthdate + '\'' +
                ", college='" + college + '\'' +
                ", place='" + place + '\'' +
                ", mbbsReg='" + mbbsReg + '\'' +
                ", degree='" + degree + '\'' +
                ", placelat='" + placelat + '\'' +
                ", placelon='" + placelon + '\'' +
                ", available='" + available + '\'' +
                ", fromDate='" + fromDate + '\'' +
                ", toDate='" + toDate + '\'' +
                ", type='" + type + '\'' +
                ", distance='" + distance + '\'' +
                '}';
    }

    public void setType(String type) {
        this.type = type;
    }


}