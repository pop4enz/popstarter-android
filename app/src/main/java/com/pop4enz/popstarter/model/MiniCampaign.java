package com.pop4enz.popstarter.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class MiniCampaign {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("categoryName")
    @Expose
    private String categoryName;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("createdAt")
    @Expose
    private Date createdAt;
    @SerializedName("currentMoney")
    @Expose
    private float currentMoney;
    @SerializedName("goal")
    @Expose
    private float goal;
    @SerializedName("avgrating")
    @Expose
    private double avgrating;

    public MiniCampaign() {
    }

    public MiniCampaign(Integer id, String title,
                        String categoryName, String image, Date createdAt,
                        float currentMoney, float goal, double avgrating) {
        this.id = id;
        this.title = title;
        this.categoryName = categoryName;
        this.image = image;
        this.createdAt = createdAt;
        this.currentMoney = currentMoney;
        this.goal = goal;
        this.avgrating = avgrating;
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public float getCurrentMoney() {
        return currentMoney;
    }

    public void setCurrentMoney(float currentMoney) {
        this.currentMoney = currentMoney;
    }

    public float getGoal() {
        return goal;
    }

    public void setGoal(float goal) {
        this.goal = goal;
    }

    public double getAvgrating() {
        return avgrating;
    }

    public void setAvgrating(double avgrating) {
        this.avgrating = avgrating;
    }

}
