package com.aplana.apiPractice.models;

import java.io.Serializable;
import java.util.Date;
import java.util.Random;

public class Project implements Serializable{

    /** Название проекта*/
    private String name;
    /** Описание проекта*/
    private String description;
    /** Дата начала работы на проекте*/
    private Date startDate;
    /** Дата окончания работы на проекте*/
    private Date endDate;
    /** Id записи*/
    private long id;

    public Project() {
//        this.id = ProfileManager.getInstance("soapSession").getNewId();
        Random random = new Random();
        this.id = random.nextLong();
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Project withPhone(String phone) {
        this.name = phone;
        return this;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Project withDescription(String description) {
        this.description = description;
        return this;
    }

    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public Project withStartDate(Date startDate) {
        this.startDate = startDate;
        return this;
    }

    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public Project withEndDate(Date endDate) {
        this.endDate = endDate;
        return this;
    }


    public long getId() {
        return id;
    }
}
