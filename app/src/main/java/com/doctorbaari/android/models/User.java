package com.doctorbaari.android.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Nowfel Mashnoor on 11/26/2017.
 */


public class User {

    @SerializedName("id")

    private Integer id;
    @SerializedName("created_at")

    private String createdAt;
    @SerializedName("username")

    private String username;
    @SerializedName("email")

    private String email;

    @SerializedName("phone")

    private String phone;
    @SerializedName("fb_profile")

    private String fb_profile;

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


    @SerializedName("available")
    private String available;

    @SerializedName("from_date")
    private String fromDate;

    @SerializedName("to_date")
    private String toDate;

    public Integer getId() {
        return id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getFb_profile() {
        return fb_profile;
    }

    public String getPpUrl() {
        return ppUrl;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public String getCollege() {
        return college;
    }

    public String getPlace() {
        return place;
    }

    public String getMbbsReg() {
        return mbbsReg;
    }

    public String getDegree() {
        return degree;
    }

    public String getAvailable() {
        return available;
    }

    public String getFromDate() {
        return fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public boolean isAvailable() {
        return available.equals("1");
    }


}