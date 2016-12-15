package com.sjtu.pcm.entity;

/**
 * Created by cao on 2016/12/15.
 */

public class CommentEntity {
    Long id;
    Long user_id;
    String content;
    Integer star;

    public CommentEntity(Long user_id, String content, Integer star) {
        this.user_id = user_id;
        this.star = star;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getStar() {
        return star;
    }

    public void setStar(Integer star) {
        this.star = star;
    }

}
