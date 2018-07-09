package com.skyapps.bennyapp.Objects;

public class Tender {
    String name, masad, project;
    long time;
    String email, phone;



    public Tender(String masad, String name, String project, long time) {
        this.masad = masad;
        this.name = name;
        this.project = project;
        this.time = time;
    }

    public Tender(String name, long time, String email, String phone) {
        this.name = name;
        this.time = time;
        this.email = email;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getMasad() {
        return masad;
    }

    public void setMasad(String masad) {
        this.masad = masad;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
