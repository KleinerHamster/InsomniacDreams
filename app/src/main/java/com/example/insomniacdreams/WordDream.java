package com.example.insomniacdreams;

public class WordDream {

    private String dreamName;//слово
    private String dreamText;//значение

    //конструктор
    public WordDream(String dreamName, String dreamText) {
        this.dreamName = dreamName;
        this.dreamText = dreamText;
    }

    //геттеры и сеттеры
    public String getDreamName() {
        return dreamName;
    }

    public void setDreamName(String dreamName) {
        this.dreamName = dreamName;
    }

    public String getDreamText() {
        return dreamText;
    }

    public void setDreamText(String dreamText) {
        this.dreamText = dreamText;
    }
}
