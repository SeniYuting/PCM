package com.sjtu.pcm.entity;

/**
 * Created by julia on 12/14/16.
 */

public class CardEntity {
    private Long id;
    private Long user_id;
    private String name;
    private String company;
    private String job;
    private String c_number;
    private String c_address;
    private String c_fax;
    private String c_email;

    public CardEntity(Long user_id, String name, String company, String job, String c_number, String c_address, String c_fax, String c_email) {
        this.user_id = user_id;
        this.name = name;
        this.company = company;
        this.job = job;
        this.c_number = c_number;
        this.c_address = c_address;
        this.c_fax = c_fax;
        this.c_email = c_email;
    }

    public CardEntity() {
        this.id = new Long(0);
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
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

    public String getC_number() {
        return c_number;
    }

    public void setC_number(String c_number) {
        this.c_number = c_number;
    }

    public String getC_address() {
        return c_address;
    }

    public void setC_address(String c_address) {
        this.c_address = c_address;
    }

    public String getC_fax() {
        return c_fax;
    }

    public void setC_fax(String c_fax) {
        this.c_fax = c_fax;
    }

    public String getC_email() {
        return c_email;
    }

    public void setC_email(String c_email) {
        this.c_email = c_email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
