package com.example.gym;

import java.io.Serializable;

public class User implements Serializable {
    private String email;
    private String password;
    private String fname;
    private String lname;
    private String bmi;
    private String phone;
    private String Key;
    private String level;
    private String choice;
    private String gender;



    public User() {
    }

    public User(String email, String password, String fname, String lname, String bmi, String phone, String key, String level, String choice, String gender) {
        this.email = email;
        this.password = password;
        this.fname = fname;
        this.lname = lname;
        this.bmi = bmi;
        this.phone = phone;
        this.Key = key;
        this.level = level;
        this.choice = choice;
        this.gender = gender;

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getBmi() {
        return bmi;
    }

    public void setBmi(String bmi) {
        this.bmi = bmi;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        this.Key = key;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getChoice() {
        return choice;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

}