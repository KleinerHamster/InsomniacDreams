package com.example.insomniacdreams;

import java.util.ArrayList;

public class ArrayWords {
    private String letter;//буква
    private ArrayList<WordDream> wordDreams;

    //конструктор
    public ArrayWords(String letter, ArrayList<WordDream> wordDreams) {
        this.letter = letter;
        this.wordDreams = wordDreams;
    }

    //геттеры и сеттеры
    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public ArrayList<WordDream> getWordDreams() {
        return wordDreams;
    }

    public void setWordDreams(ArrayList<WordDream> wordDreams) {
        this.wordDreams = wordDreams;
    }
}
