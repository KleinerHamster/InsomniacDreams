package com.example.insomniacdreams;


import org.junit.Test;

import static org.junit.Assert.*;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ExampleUnitTest {

    @Test
    public void typeDream_isCorrect() {
        //рассчитываем процент
        int simple=0, lucid=0, semi=0;
        String[] dreamsType={"simple","simple", "simple", "lucid", "lucid"};
        for(int i =0 ; i < dreamsType.length; i++){
            if(dreamsType[i].equals("simple")){
                simple++;
            }
            else if(dreamsType[i].equals("lucid")){
                lucid++;
            }
            else if(dreamsType[i].equals("semi lucid")){
                semi++;
            }
        }
        int simplePercent=(simple*100)/dreamsType.length;
        int lucidPercent=(lucid*100)/dreamsType.length;
        int semiPercent=(semi*100)/dreamsType.length;
        int[] expected={60,40,0};
        int[] actual={simplePercent,lucidPercent,semiPercent};
        assertArrayEquals("Type of dream", expected, actual);
    }

    @Test
    public void mood_isCorrect() {
        //рассчитываем процент
        int amazing = 0, good = 0, normal = 0, bad = 0, sad = 0;
        String[] mood={"sadness","sadness", "sadness", "sadness",
                "normal","good", "good", "joy", "bad", "joy", "joy", "bad", "normal", "normal"};

        for (int i = 0; i < mood.length; i++) {
            if (mood[i].equals("joy")) {
                amazing++;
            } else if (mood[i].equals("good")) {
                good++;
            } else if (mood[i].equals("normal")) {
                normal++;
            } else if (mood[i].equals("bad")) {
                bad++;
            } else if (mood[i].equals("sadness")) {
                sad++;
            }
        }
        int amazingPercent = (amazing * 100) / mood.length;
        int goodPercent = (good * 100) / mood.length;
        int normalPercent = (normal * 100) / mood.length;
        int badPercent = (bad * 100) / mood.length;
        int sadPercent = (sad * 100) / mood.length;

        int[] expected={21, 14, 21, 14, 28};
        int[] actual={amazingPercent,goodPercent,normalPercent,badPercent, sadPercent};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void timeDream_isCorrect() {
        //рассчитываем процент
        int night=0, morning=0, day=0;
        String[] dreamsTime={"night","day", "day", "morning", "morning"};

        for(int i =0 ; i < dreamsTime.length; i++){
            if(dreamsTime[i].equals("night")){
                night++;
            }
            else if(dreamsTime[i].equals("morning")){
                morning++;
            }
            else if(dreamsTime[i].equals("day")){
                day++;
            }
        }

        int nightPercent=(night*100)/dreamsTime.length;
        int morningPercent=(morning*100)/dreamsTime.length;
        int dayPercent=(day*100)/dreamsTime.length;
        int[] expected={20,40,40};
        int[] actual={nightPercent,morningPercent,dayPercent};
        assertArrayEquals("Time", expected, actual);
    }


}