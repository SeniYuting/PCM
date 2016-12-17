package com.sjtu.pcm.entity;

/**
 * Created by Victor_Zhou on 2016-12-17.
 */

public class UserTagEntity {
    private Long id;
    private Long user_id;
    private Long tag_id;

    public UserTagEntity(Long user_id, Long tag_id) {
        this.user_id = user_id;
        this.tag_id = tag_id;
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

    public Long getTag_id() {
        return tag_id;
    }

    public void setTag_id(Long tag_id) {
        this.tag_id = tag_id;
    }
}
