package com.example.celebrities.model;

import java.io.Serializable;

public class Profile implements Serializable {
    private String title;
    private String profession;
    private String birthday;
    private String birthPlace;
    private int age;
    private String sign;
    private String description;
    private String imageURL;
    private String detailPageURL;
    private String name;


    public Profile(){
    }

    @Override
    public String toString() {
        return "Profile{" +
                "title='" + title + '\'' +
                ", profession='" + profession + '\'' +
                ", birthday='" + birthday + '\'' +
                ", birthPlace='" + birthPlace + '\'' +
                ", age=" + age +
                ", sign='" + sign + '\'' +
                ", description='" + description + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", detailPageURL='" + detailPageURL + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getTitle() {
        return title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getDetailPageURL() {
        return detailPageURL;
    }

    public void setDetailPageURL(String detailPageURL) {
        this.detailPageURL = detailPageURL;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}