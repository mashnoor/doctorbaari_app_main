package com.doctorbaari.android.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Nowfel Mashnoor on 11/26/2017.
 */


public class User {

    @SerializedName("id")

    private Integer id;
    @SerializedName("create_date")

    private String createDate;
    @SerializedName("fullname")

    private String fullname;
    @SerializedName("email")

    private String email;
    @SerializedName("password")

    private String password;
    @SerializedName("username")

    private String username;
    @SerializedName("phone")

    private String phone;
    @SerializedName("fb_name")

    private String fbName;
    @SerializedName("fb_profile")

    private String fbProfile;
    @SerializedName("pp_url")

    private String ppUrl;
    @SerializedName("birthdate")

    private String birthdate;
    @SerializedName("college")

    private String college;
    @SerializedName("work1")

    private String work1;
    @SerializedName("street")

    private String street;
    @SerializedName("work_division")

    private String workDivision;
    @SerializedName("work_thana")

    private String workThana;
    @SerializedName("work_zilla")

    private String workZilla;
    @SerializedName("work2")

    private String work2;
    @SerializedName("mbbs_reg")

    private String mbbsReg;
    @SerializedName("designation")

    private String designation;
    @SerializedName("pass_change")

    private String passChange;
    @SerializedName("doctor")
    private Integer doctor;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFbName() {
        return fbName;
    }

    public void setFbName(String fbName) {
        this.fbName = fbName;
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

    public String getWork1() {
        return work1;
    }

    public void setWork1(String work1) {
        this.work1 = work1;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getWorkDivision() {
        return workDivision;
    }

    public void setWorkDivision(String workDivision) {
        this.workDivision = workDivision;
    }

    public String getWorkThana() {
        return workThana;
    }

    public void setWorkThana(String workThana) {
        this.workThana = workThana;
    }

    public String getWorkZilla() {
        return workZilla;
    }

    public void setWorkZilla(String workZilla) {
        this.workZilla = workZilla;
    }

    public String getWork2() {
        return work2;
    }

    public void setWork2(String work2) {
        this.work2 = work2;
    }

    public String getMbbsReg() {
        return mbbsReg;
    }

    public void setMbbsReg(String mbbsReg) {
        this.mbbsReg = mbbsReg;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getPassChange() {
        return passChange;
    }

    public void setPassChange(String passChange) {
        this.passChange = passChange;
    }

    public Integer getDoctor() {
        return doctor;
    }

    public void setDoctor(Integer doctor) {
        this.doctor = doctor;
    }

}