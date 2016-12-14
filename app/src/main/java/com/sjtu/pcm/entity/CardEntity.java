package com.sjtu.pcm.entity;

/**
 * Created by julia on 12/14/16.
 */

public class CardEntity {
    private String id;
    private String name;
    private String company;
    private String job;
    private String number;
    private String address;
    private String fax;
    private String email;

    public CardEntity(String name, String company, String job, String number, String address, String fax, String email) {
        this.name = name;
        this.company = company;
        this.job = job;
        this.number = number;
        this.address = address;
        this.fax = fax;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
