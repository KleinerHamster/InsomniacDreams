package com.example.insomniacdreams;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TableFragment extends Fragment {
    private DreamModel model;
    TextView simpleTable, lucidTable, semiTable;
    TextView amazingTable, goodTable, normalTable, badNormalTable, badTable, nightTable, morningTable, daytimeTable;

    //конструктор
    public TableFragment() {
    }

    public static TableFragment newInstance() {
        return new TableFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_table, container, false);

        semiTable=(TextView) rootView.findViewById(R.id.semiTable);
        lucidTable=(TextView) rootView.findViewById(R.id.lucidTable);
        simpleTable=(TextView) rootView.findViewById(R.id.simpleTable);

        amazingTable=(TextView) rootView.findViewById(R.id.amazingTable);
        goodTable=(TextView) rootView.findViewById(R.id.goodTable);
        normalTable=(TextView) rootView.findViewById(R.id.normalTable);
        badNormalTable=(TextView) rootView.findViewById(R.id.badNormalTable);
        badTable=(TextView) rootView.findViewById(R.id.badTable);

        nightTable=(TextView) rootView.findViewById(R.id.nightTable);
        morningTable=(TextView) rootView.findViewById(R.id.morningTable);
        daytimeTable=(TextView) rootView.findViewById(R.id.daytimeTable);

        model = new ViewModelProvider(requireActivity()).get(DreamModel.class);
        ArrayList<Dream> allDreams = (ArrayList<Dream>) model.get().getValue();

        List<Dream> dreams = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "yyyy");

        for(int i=0; i<allDreams.size();i++){
            Date date= allDreams.get(i).getDate().toDate();
            if(simpleDateFormat.format(date).equals("2022")){
                dreams.add(allDreams.get(i));
            }
        }
        setTableDream(dreams);
        setTableMood(dreams);
        setTableTime(dreams);
        return rootView;
    }

    //метод для рассчета процентов типа сна
    public void setTableDream(List<Dream> dreams){
        int simple=0, lucid=0, semi=0;

        for(int i =0 ; i < dreams.size(); i++){
            if(dreams.get(i).getSleep().equals("simple")){
                simple++;
            }
            else if(dreams.get(i).getSleep().equals("lucid")){
                lucid++;
            }
            else if(dreams.get(i).getSleep().equals("semi lucid")){
                semi++;
            }
        }
        int simplePercent=(simple*100)/dreams.size();
        int lucidPercent=(lucid*100)/dreams.size();
        int semiPercent=(semi*100)/dreams.size();
        semiTable.setText(String.valueOf(semiPercent)+"%");
        lucidTable.setText(String.valueOf(lucidPercent)+"%");
        simpleTable.setText(String.valueOf(simplePercent)+"%");
    }

    //метод для рассчета процентов эмоций
    public void setTableMood(List<Dream> dreams){
        int amazing=0, good=0, normal=0, bad=0, sad=0;
        for(int i =0 ; i < dreams.size(); i++){
            if(dreams.get(i).getEmotion().equals("joy")){
                amazing++;
            }
            else if(dreams.get(i).getEmotion().equals("good")){
                good++;
            }
            else if(dreams.get(i).getEmotion().equals("normal")){
                normal++;
            }
            else if(dreams.get(i).getEmotion().equals("bad")){
                bad++;
            }
            else if(dreams.get(i).getEmotion().equals("sadness")){
                sad++;
            }
        }
        int amazingPercent=(amazing*100)/dreams.size();
        int goodPercent=(good*100)/dreams.size();
        int normalPercent=(normal*100)/dreams.size();
        int badPercent=(bad*100)/dreams.size();
        int sadPercent=(sad*100)/dreams.size();
        amazingTable.setText(String.valueOf(amazingPercent)+"%");
        goodTable.setText(String.valueOf(goodPercent)+"%");
        normalTable.setText(String.valueOf(normalPercent)+"%");
        badNormalTable.setText(String.valueOf(badPercent)+"%");
        badTable.setText(String.valueOf(sadPercent)+"%");
    }

    //метод для рассчета процентов времени сна
    public void setTableTime(List<Dream> dreams){
        int night=0, morning=0, day=0;

        for(int i =0 ; i < dreams.size(); i++){
            if(dreams.get(i).getTime().equals("night")){
                night++;
            }
            else if(dreams.get(i).getTime().equals("morning")){
                morning++;
            }
            else if(dreams.get(i).getTime().equals("day")){
                day++;
            }
        }
        int nightPercent=(night*100)/dreams.size();
        int morningPercent=(morning*100)/dreams.size();
        int dayPercent=(day*100)/dreams.size();
        daytimeTable.setText(String.valueOf(dayPercent)+"%");
        morningTable.setText(String.valueOf(morningPercent)+"%");
        nightTable.setText(String.valueOf(nightPercent)+"%");
    }
}
