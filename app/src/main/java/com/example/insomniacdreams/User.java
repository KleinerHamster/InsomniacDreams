package com.example.insomniacdreams;

import java.io.Serializable;

public class User implements Serializable {
    private String userId;
    private String name;
    private String surname;
    private String photo;

    //конструктор
    public User(String userId, String name, String surname, String photo) {
        this.userId = userId;
        this.name = name;
        this.surname = surname;
        this.photo = photo;
    }

    //геттеры и сеттеры
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
