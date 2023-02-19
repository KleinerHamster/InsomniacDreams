package com.example.insomniacdreams;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class DreamModel extends ViewModel {

    private MutableLiveData<List<Dream>> data = new MutableLiveData<>();;

    public MutableLiveData<List<Dream>> get(){
        return data;
    }

    //метод для переустановки даных
    public void setData(List<Dream> dreams) {
        data.setValue(dreams);
    }
}
