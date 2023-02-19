package com.example.insomniacdreams;

import com.google.firebase.Timestamp;

import java.io.Serializable;

public class Dream implements Serializable {
    //поля класса
    private String dreamID;
    private String title;
    private String description;
    private Timestamp date;
    private String time;
    private String sleep;
    private String emotion;

    //конструктор
    public Dream(String dreamID, String title, String description, Timestamp date, String time, String sleep, String emotion) {
        this.dreamID = dreamID;
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
        this.sleep = sleep;
        this.emotion = emotion;
    }


    //геттеры и сеттеры
    public String getDreamID() {
        return dreamID;
    }

    public void setDreamID(String dreamID) {
        this.dreamID = dreamID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSleep() {
        return sleep;
    }

    public void setSleep(String sleep) {
        this.sleep = sleep;
    }

    public String getEmotion() {
        return emotion;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }
}
