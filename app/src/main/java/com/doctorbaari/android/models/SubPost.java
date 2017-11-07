package com.doctorbaari.android.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Nowfel Mashnoor on 11/7/2017.
 */

public class SubPost {
    @SerializedName("post_date")
    private String postDate;
    @SerializedName("date_from")
    private String dateFrom;
    @SerializedName("date_to")
    private String dateTo;
    @SerializedName("hospital_name")
    private String hospitalName;
    @SerializedName("division")
    private String division;

    public String getPostDate() {
        return postDate;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public String getDivision() {
        return division;
    }
}
