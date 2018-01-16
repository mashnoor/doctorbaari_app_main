
package com.doctorbaari.android.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Advertise {

    @SerializedName("id")

    private Integer id;
    @SerializedName("title")

    private String title;
    @SerializedName("body")

    private String body;
    @SerializedName("details")

    private String details;
    @SerializedName("notification_time")

    private String notificationTime;
    @SerializedName("created_at")

    private String createdAt;

    @SerializedName("company")
    private String company;

    public String getCompany() {
        return company;
    }

    public String getDetails() {
        return details;
    }

    public String getBody() {
        return body;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getHour() {
        return Integer.parseInt(notificationTime.split(":")[0]);
    }

    public int getMinute() {
        return Integer.parseInt(notificationTime.split(":")[1]);
    }

    @Override
    public String toString() {
        return "Advertise{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", details='" + details + '\'' +
                ", notificationTime='" + notificationTime + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}