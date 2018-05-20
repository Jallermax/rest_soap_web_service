package com.aplana.apiPractice.models;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.*;

@XmlRootElement(name = "Profile")
//@XmlType(propOrder = {"id", "name", "secondName", "surName", "birthday", "email", "phone",
//        "startDate", "position", "isProbationFinished", "projectList"})
public class Profile implements Serializable{

    /** Имя сотрудника*/
//    @XmlElement(required = true)
    private String name;
    /** Фамилия сотрудника*/
//    @XmlElement(required = true)
    private String surName;
    /** Отчество сотрудника*/
    private String secondName;
    /** Дата рождения*/
    private Date birthday;
    /** Телефон*/
//    @XmlElement(type = Long.class)
    private String phone;
    /** email*/
//    @XmlElement(required = true)
    private String email;
    /** должность*/
    private String position;
    /** Дата начала работы в компании*/
    private Date startDate;
    /** Сотрудник завершил испытательный срок*/
    private Boolean isProbationFinished;
//    /** Проекты*/
//    private Map<Long, Project> projects;
    /** Проекты*/
//    private String projects;
    private List<Project> projectList = new ArrayList<>();
    /** Id записи*/
    private long id;

    public Profile() {
        Random random = new Random();
        id = random.nextLong();
        startDate = Calendar.getInstance().getTime();
        updateDynamicParams();
    }

    public Profile(long id, Profile profile) {
        this.name = profile.name;
        this.surName = profile.surName;
        this.secondName = profile.secondName;
        this.birthday = profile.birthday;
        this.phone = profile.phone;
        this.email = profile.email;
        this.position = profile.position;
        this.startDate = profile.startDate;
        this.projectList = profile.projectList;

        this.id = id;
        startDate = Calendar.getInstance().getTime();
        updateDynamicParams();
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

    private void updateStartDate() {
        startDate = projectList.stream().map(Project::getStartDate)
                .min(Comparator.nullsLast(Comparator.naturalOrder()))
                .orElse(startDate);
    }

    public long getId() {
        return id;
    }

    public void updateDynamicParams() {
        updateStartDate();
        isProbationFinished = getProbationFinished();
    }

    public String getFullName() {
        return surName + " " + name + (secondName != null ? " " + secondName : "");
    }

    public boolean valid() {
        return name != null && name.matches("[A-z,А-я]{2,}")
                && surName != null && surName.matches("[A-z,А-я]{2,}")
                && (secondName == null || secondName.matches("[A-z,А-я]{2,}"))
                && (birthday == null || birthday.before(Calendar.getInstance().getTime()))
                && (phone == null || phone.matches("[\\d,\\W]{10,18}"))
                && (email == null || email.matches("\\S+@\\S+\\.\\S{2,}"))
                && (startDate == null || startDate.before(Calendar.getInstance().getTime()));
    }
}
