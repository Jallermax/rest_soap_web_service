package com.aplana.apiPractice.models;

import com.aplana.apiPractice.exceptions.DataValidation;

import javax.xml.bind.annotation.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProfileRq_Type", propOrder = {
        "surName",
        "name",
        "secondName",
        "birthday",
        "phone",
        "email",
        "position",
        "projectList"

})
@XmlRootElement(name = "AddProfileRq")
public class AddProfileRq {

    @XmlElement(name = "Name", required = true)
    private String name;
    @XmlElement(name = "SurName", required = true)
    private String surName;
    @XmlElement(name = "SecondName")
    private String secondName;
    @XmlElement(name = "Birthday", type = Date.class)
    private Date birthday;
    @XmlElement(name = "Phone")
    private String phone;
    @XmlElement(name = "E-mail", required = true)
    private String email;
    @XmlElement(name = "Position")
    private String position;
    @XmlElement(name = "Projects")
    private List<Project> projectList = new ArrayList<>();

    public String getName() {
        return name;
    }

    public String getSurName() {
        return surName;
    }

    public String getSecondName() {
        return secondName;
    }

    public Date getBirthday() {
        return birthday;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getPosition() {
        return position;
    }

    public List<Project> getProjectList() {
        return projectList;
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
        for (Project project : projectList) {
            project.validate();
        }
    }
}
