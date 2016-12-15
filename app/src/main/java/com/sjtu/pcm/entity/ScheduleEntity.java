package com.sjtu.pcm.entity;

/**
 * Created by cao on 2016/12/15.
 */

public class ScheduleEntity {
    Long id;
    Long suer_id;
    Long partner_id;
    String date;
    String place;
    String topic;
    String user_note;
    String partner_note;

    public ScheduleEntity(Long suer_id, Long partner_id, String date, String place, String topic, String user_note, String partner_note) {
        this.suer_id = suer_id;
        this.partner_id = partner_id;
        this.date = date;
        this.place = place;
        this.topic = topic;
        this.user_note = user_note;
        this.partner_note = partner_note;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSuer_id() {
        return suer_id;
    }

    public void setSuer_id(Long suer_id) {
        this.suer_id = suer_id;
    }

    public Long getPartner_id() {
        return partner_id;
    }

    public void setPartner_id(Long partner_id) {
        this.partner_id = partner_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getUser_note() {
        return user_note;
    }

    public void setUser_note(String uNote) {
        this.user_note = user_note;
    }

    public String getPartner_note() {
        return partner_note;
    }

    public void setPartner_note(String pNote) {
        this.partner_note = partner_note;
    }
}
