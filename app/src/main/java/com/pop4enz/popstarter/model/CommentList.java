package com.pop4enz.popstarter.model;

import java.io.Serializable;
import java.util.List;

public class CommentList implements Serializable {

    private List<Comment> comments;

    public CommentList() {
    }

    public CommentList(List<Comment> comments) {
        this.comments = comments;
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
