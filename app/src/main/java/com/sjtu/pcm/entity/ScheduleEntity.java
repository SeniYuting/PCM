package com.sjtu.pcm.entity;

/**
 * Created by cao on 2016/12/15.
 */

public class ScheduleEntity {
    String id;
    String suer_id;
    String partner_id;
    String date;
    String place;
    String topic;
    String uNote;
    String pNote;

    public ScheduleEntity(String suer_id, String partner_id, String date, String place, String topic, String uNote, String pNote) {
        this.suer_id = suer_id;
        this.partner_id = partner_id;
        this.date = date;
        this.place = place;
        this.topic = topic;
        this.uNote = uNote;
        this.pNote = pNote;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSuer_id() {
        return suer_id;
    }

    public void setSuer_id(String suer_id) {
        this.suer_id = suer_id;
    }

    public String getPartner_id() {
        return partner_id;
    }

    public void setPartner_id(String partner_id) {
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

    public String getUNote() {
        return uNote;
    }

    public void setUNote(String uNote) {
        this.uNote = uNote;
    }

    public String getPNote() {
        return pNote;
    }

    public void setPNote(String pNote) {
        this.pNote = pNote;
    }
}
