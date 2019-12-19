package com.pop4enz.popstarter.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CampaignCategory {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;

    public CampaignCategory() {
    }

    public CampaignCategory(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
