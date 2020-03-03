package com.example.celebrities;

import java.io.Serializable;
import java.util.Date;

public class Profile implements Serializable {
    private String title;
    private String profession;
    private Date birthday;
    private String birthPlace;
    private int age;
    private String sign;
    private String description;
    private String imageURL;

    public Profile(){
    }

    @Override
    public String toString() {
        return "Profile{" +
                "title='" + title + '\'' +
                ", profession='" + profession + '\'' +
                ", birthday=" + birthday +
                ", birthPlace='" + birthPlace + '\'' +
                ", age=" + age +
                ", sign='" + sign + '\'' +
                ", description='" + description + '\'' +
                ", image='" + imageURL + '\'' +
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

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
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