package com.example.userprofile;

import java.io.Serializable;

public class User implements Serializable {
    private String fullName;
    private String dob;
    private String gender;
    private double weightVal;
    private String weightUnit;

    public User(String fullName, String dob, String gender, double weight, String unit) {
        this.fullName = fullName;
        this.dob = dob;
        this.gender = gender;
        this.weightVal = weight;
        this.weightUnit = unit;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public double getWeightValue() {
        return weightVal;
    }

    public void setWeightValue(double weight) {
        this.weightVal = weight;
    }

    public String getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(String unit) {
        this.weightUnit = unit;
    }

    @Override
    public String toString() {
        return "User {fullName: " + fullName + ", date of birth: " + dob + ", gender: " + gender + ", weight: " + weightVal + " " + weightUnit + "}";
    }
}
