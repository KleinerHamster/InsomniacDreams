package com.example.insomniacdreams;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;

public class DreamBookFragment extends Fragment{

    ArrayList<ArrayWords> arrayWords= new ArrayList<>();
    ArrayWordsAdapter adapter;

    public DreamBookFragment() {
    }

    public static DreamBookFragment newInstance() {
        return new DreamBookFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dream_book, container, false);

        //Загрузка в RecyclerView из JSON файла
        //находим recyclerview в активности
        RecyclerView rvAllCategory = rootView.findViewById(R.id.main_recyclerView_12);
        rvAllCategory.setHasFixedSize(true);
        //устанавливаем  layout manager для размещения элементов
        rvAllCategory.setLayoutManager(new LinearLayoutManager(getActivity()));
        try {
            JSONObject jsonObject = new JSONObject(loadJSONFromAsset());
            char c;
            for(c = 'A'; c <= 'Z'; ++c){
                JSONArray jsonArray=jsonObject.getJSONArray(String.valueOf(c));
                ArrayList<WordDream> wordDreamArrayList=new ArrayList<>();
                for(int i=0; i<jsonArray.length();i++){
                    JSONObject dreamData=jsonArray.getJSONObject(i);
                    wordDreamArrayList.add(new WordDream(dreamData.getString("dreamName"), dreamData.getString("dreamText")));
                }
                arrayWords.add(new ArrayWords(String.valueOf(c), wordDreamArrayList));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //создаем адаптер, передает массив данных
        adapter = new ArrayWordsAdapter(arrayWords);
        //устанавливаем адаптер к recyclerview для заполнения эл-тов
        rvAllCategory.setAdapter(adapter);

        SearchView searchView= (SearchView) rootView.findViewById(R.id.searchView);
        //действие при поиске
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            //метод при изменение ввода
            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });




        return rootView;
    }


    //метод для загрузки данных из файла json
    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open("dreams.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}