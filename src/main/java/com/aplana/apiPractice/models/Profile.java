package com.aplana.apiPractice.models;

import com.aplana.apiPractice.ProfileManager;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Profile_Type", propOrder = {
        "id",
        "surName",
        "name",
        "secondName",
        "birthday",
        "phone",
        "email",
        "position",
        "startDate",
        "isProbationFinished",
        "projectList"

})
@XmlRootElement(name = "Profile")
public class Profile implements Serializable{

    private long id;
    /** Имя сотрудника*/
    @XmlElement(name = "Name", required = true)
    private String name;
    /** Фамилия сотрудника*/
    @XmlElement(name = "SurName", required = true)
    private String surName;
    /** Отчество сотрудника*/
    @XmlElement(name = "SecondName")
    private String secondName;
    /** Дата рождения*/
    @XmlElement(name = "Birthday")
    private Date birthday;
    /** Телефон*/
    @XmlElement(name = "Phone", type = Long.class)
    private String phone;
    /** email*/
    @XmlElement(name = "E-mail", required = true)
    private String email;
    /** должность*/
    @XmlElement(name = "Position")
    private String position;
    /** Дата начала работы в компании*/
    @XmlElement(name = "StartDate")
    private Date startDate;
    /** Сотрудник завершил испытательный срок*/
    @XmlElement(name = "ProbationFinishedFlag")
    private Boolean isProbationFinished;
    /** Проекты*/
    @XmlElement(name = "Projects")
    private List<Project> projectList = new ArrayList<>();
    /** Id записи*/

    public Profile() {
        id = ProfileManager.getInstance().getNewId();
        startDate = Calendar.getInstance().getTime();
        updateDynamicParams();
    }

    public Profile(AddProfileRq profileRq) {
        if (profileRq == null) return;
        this.name = profileRq.getName();
        this.surName = profileRq.getSurName();
        this.secondName = profileRq.getSecondName();
        this.birthday = profileRq.getBirthday();
        this.email = profileRq.getEmail();
        this.phone = profileRq.getPhone();
        this.position = profileRq.getPosition();
        this.projectList = profileRq.getProjectList() != null
                ? profileRq.getProjectList()
                : projectList;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Profile withName(String name) {
        this.name = name;
        return this;
    }

    public String getSurName() {
        return surName;
    }
    public void setSurName(String surName) {
        this.surName = surName;
    }
    public Profile withSurName(String surName) {
        this.surName = surName;
        return this;
    }

    public String getSecondName() {
        return secondName;
    }
    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }
    public Profile withSecondName(String secondName) {
        this.secondName = secondName;
        return this;
    }

    public Date getBirthday() {
        return birthday;
    }
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
    public Profile withBirthday(Date birthday) {
        this.birthday = birthday;
        return this;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public Profile withPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public Profile withEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPosition() {
        return position;
    }
    public void setPosition(String position) {
        this.position = position;
    }
    public Profile withPosition(String position) {
        this.position = position;
        return this;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Boolean getProbationFinished() {
        if (startDate != null) {
            Calendar probationEnd = Calendar.getInstance();
            probationEnd.setTime(startDate);
            probationEnd.add(Calendar.MONTH, 3);
            return probationEnd.before(Calendar.getInstance());
        }
        return false;
    }

    public List<Project> getProjectList() {
        return projectList;
    }
    public void addProject(Project project) {
        this.projectList.add(project);
        updateDynamicParams();
    }
    public Profile withProject(Project project) {
        this.projectList.add(project);
        updateDynamicParams();
        return this;
    }

    public long getId() {
        return id;
    }

    private void updateStartDate() {
        startDate = projectList.stream()
                .filter(project -> project.getStartDate() != null)
                .map(Project::getStartDate)
                .min(Comparator.nullsLast(Comparator.naturalOrder()))
                .orElse(startDate);
    }

    public void updateDynamicParams() {
        updateStartDate();
        isProbationFinished = getProbationFinished();
    }

    public String getFullName() {
        return surName + " " + name + (secondName != null ? "" + secondName : "");
    }
}
