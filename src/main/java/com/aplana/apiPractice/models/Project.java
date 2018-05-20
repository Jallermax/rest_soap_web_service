package com.aplana.apiPractice.models;

import com.aplana.apiPractice.ProfileManager;
import com.aplana.apiPractice.exceptions.DataValidation;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Date;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Project", propOrder = {
        "name",
        "description",
        "startDate",
        "endDate"
})
public class Project implements Serializable{

    /** Название проекта*/
    @XmlElement(name = "Name", required = true)
    private String name;
    /** Описание проекта*/
    @XmlElement(name = "Description")
    private String description;
    /** Дата начала работы на проекте*/
    @XmlElement(name = "StartDate")
    private Date startDate;
    /** Дата окончания работы на проекте*/
    @XmlElement(name = "EndDate")
    private Date endDate;
    /** Id записи*/
    private long id;

    public Project() {
        this.id = ProfileManager.getInstance().getNewId();
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

    public void validate() throws DataValidation {
        try {
            for (Field field : this.getClass().getDeclaredFields()) {
                if (field.get(this) == null && field.getAnnotation(XmlElement.class).required()) {
                    throw new DataValidation("Required field " + field.getName() + " = null");
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
