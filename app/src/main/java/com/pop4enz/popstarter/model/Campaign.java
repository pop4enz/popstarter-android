package com.pop4enz.popstarter.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Campaign {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("categoryName")
    @Expose
    private String categoryName;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("videoLink")
    @Expose
    private String videoLink;
    @SerializedName("goal")
    @Expose
    private float goal;
    @SerializedName("currentMoney")
    @Expose
    private float currentMoney;
    @SerializedName("backersCount")
    @Expose
    private int backersCount;
    @SerializedName("expiresAt")
    @Expose
    private String expiresAt;
    @SerializedName("creator")
    @Expose
    private User creator;
    @SerializedName("images")
    @Expose
    private List<CampaignImage> images;

    public Campaign() {
    }

    public Campaign(Integer id, String title, String categoryName,
                    String description, String videoLink, float goal,
                    float currentMoney, int backersCount, String expiresAt,
                    User creator, List<CampaignImage> images) {
        this.id = id;
        this.title = title;
        this.categoryName = categoryName;
        this.description = description;
        this.videoLink = videoLink;
        this.goal = goal;
        this.currentMoney = currentMoney;
        this.backersCount = backersCount;
        this.expiresAt = expiresAt;
        this.creator = creator;
        this.images = images;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public float getGoal() {
        return goal;
    }

    public void setGoal(float goal) {
        this.goal = goal;
    }

    public float getCurrentMoney() {
        return currentMoney;
    }

    public void setCurrentMoney(float currentMoney) {
        this.currentMoney = currentMoney;
    }

    public int getBackersCount() {
        return backersCount;
    }

    public void setBackersCount(int backersCount) {
        this.backersCount = backersCount;
    }

    public String getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(String expiresAt) {
        this.expiresAt = expiresAt;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public List<CampaignImage> getImages() {
        return images;
    }

    public void setImages(List<CampaignImage> images) {
        this.images = images;
    }
}
