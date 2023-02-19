package com.example.insomniacdreams;

import com.google.firebase.Timestamp;

public class ChatMessage {
    private String senderId;
    private String message;
    private Timestamp date;
    private String name;
    private String surname;
    private String photo;


    //конструктор
    public ChatMessage(String senderId, String message, Timestamp date, String name, String surname, String photo) {
        this.senderId = senderId;
        this.message = message;
        this.date = date;
        this.name = name;
        this.surname = surname;
        this.photo = photo;
    }

    //геттеры и сеттеры
    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
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
