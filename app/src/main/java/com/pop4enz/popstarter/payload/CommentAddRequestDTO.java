package com.pop4enz.popstarter.payload;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommentAddRequestDTO {
    @SerializedName("content")
    @Expose
    private String content;

    public CommentAddRequestDTO() {}

    public CommentAddRequestDTO(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
