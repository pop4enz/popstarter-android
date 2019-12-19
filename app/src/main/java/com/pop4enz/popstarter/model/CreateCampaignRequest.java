package com.pop4enz.popstarter.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.time.Instant;
import java.util.Date;
import java.util.List;

public class CreateCampaignRequest {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("videoLink")
    @Expose
    private String videoLink;
    @SerializedName("goal")
    @Expose
    private float goal;
    @SerializedName("expiresAt")
    @Expose
    private String expiresAt;
    @SerializedName("images")
    @Expose
    private List<String> images;

    public CreateCampaignRequest() {
    }

    public CreateCampaignRequest(String title, String category,
                                 String description, String videoLink,
                                 float goal, String expiresAt, List<String> images) {
        this.title = title;
        this.category = category;
        this.description = description;
        this.videoLink = videoLink;
        this.goal = goal;
        this.expiresAt = expiresAt;
        this.images = images;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public String getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(String expiresAt) {
        this.expiresAt = expiresAt;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
