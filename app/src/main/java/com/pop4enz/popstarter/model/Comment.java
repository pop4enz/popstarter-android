package com.pop4enz.popstarter.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class Comment implements Serializable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("createdAt")
    @Expose
    private Date createdAt;
    @SerializedName("updatedAt")
    @Expose
    private Date updatedAt;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("countLikes")
    @Expose
    private int countLikes;
    @SerializedName("countDislikes")
    @Expose
    private int countDislikes;
    @SerializedName("state")
    @Expose
    private byte state;

    public Comment() {
    }

    public Comment(Integer id, Date createdAt, Date updatedAt, String content, User user, int countLikes, int countDislikes, byte state) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.content = content;
        this.user = user;
        this.countLikes = countLikes;
        this.countDislikes = countDislikes;
        this.state = state;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getCountLikes() {
        return countLikes;
    }

    public void setCountLikes(int countLikes) {
        this.countLikes = countLikes;
    }

    public int getCountDislikes() {
        return countDislikes;
    }

    public void setCountDislikes(int countDislikes) {
        this.countDislikes = countDislikes;
    }

    public byte getState() {
        return state;
    }

    public void setState(byte state) {
        this.state = state;
    }
}
