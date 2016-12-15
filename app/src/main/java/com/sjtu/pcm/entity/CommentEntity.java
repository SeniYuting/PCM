package com.sjtu.pcm.entity;

/**
 * Created by cao on 2016/12/15.
 */

public class CommentEntity {
    String id;
    String user_id;
    String star;
    String content;

    public CommentEntity(String user_id, String star, String content) {
        this.user_id = user_id;
        this.star = star;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
