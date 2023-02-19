package com.example.insomniacdreams;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiagrammsFragment extends Fragment {
    private DreamModel model;
    PieChart chartOfDream, chartOfTime;
    BarChart mChart;

    //конструктор
    public DiagrammsFragment() {
    }

    public static DiagrammsFragment newInstance() {
        return new DiagrammsFragment();
    }

    @Nullable
    @Override//circularGauge()
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_diagrams, container, false);

        chartOfDream=(PieChart) rootView.findViewById(R.id.chart_type_of_dream);
        mChart=(BarChart) rootView.findViewById(R.id.chart_mood) ;
        chartOfTime=(PieChart) rootView.findViewById(R.id.chart_time_sleep);
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
        setupPieChartOfDream(dreams);
        setupChartOfMood(dreams);
        setupPieChartOfTime(dreams);
        return rootView;
    }

    //круговая диаграмма видов снов
    public void setupPieChartOfDream(List<Dream> dreams){
        //рассчитываем процент
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

        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        String label = " ";

        //иницилизация данных
        Map<String, Integer> typeAmountMap = new HashMap<>();
        typeAmountMap.put("Simple",simplePercent);
        typeAmountMap.put("Lucid",lucidPercent);
        typeAmountMap.put("Semi lucid",semiPercent);

        //иницилизация цветов для графика
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#9EBDD9"));
        colors.add(Color.parseColor("#eb9b9a"));
        colors.add(Color.parseColor("#e2c2bd"));


        //input data and fit data into pie chart entry
        for(String type: typeAmountMap.keySet()){
            pieEntries.add(new PieEntry(typeAmountMap.get(type).intValue(), type));
        }

        PieDataSet pieDataSet = new PieDataSet(pieEntries,label);
        pieDataSet.setValueTextSize(14f);
        //указываем цвет диаграммы
        pieDataSet.setColors(colors);
        PieData pieData = new PieData(pieDataSet);
        pieData.setDrawValues(true);
        pieData.setValueTextColor(Color.parseColor("#FF000000"));
        pieData.setValueFormatter(new PercentFormatter());
        chartOfDream.setData(pieData);
        chartOfDream.invalidate();
        chartOfDream.setEntryLabelColor(Color.parseColor("#FF000000"));
        chartOfDream.getDescription().setEnabled(false);
        chartOfDream.setHoleRadius(0f);
        chartOfDream.setTransparentCircleRadius(0f);
        chartOfDream.getLegend().setTextColor(Color.WHITE);
        chartOfDream.getLegend().setTextSize(16f);
        chartOfDream.getLegend().setForm(Legend.LegendForm.CIRCLE);
    }

    //диаграмма настроений
    public void setupChartOfMood(List<Dream> dreams) {
        int amazing = 0, good = 0, normal = 0, bad = 0, sad = 0;
        for (int i = 0; i < dreams.size(); i++) {
            if (dreams.get(i).getEmotion().equals("joy")) {
                amazing++;
            } else if (dreams.get(i).getEmotion().equals("good")) {
                good++;
            } else if (dreams.get(i).getEmotion().equals("normal")) {
                normal++;
            } else if (dreams.get(i).getEmotion().equals("bad")) {
                bad++;
            } else if (dreams.get(i).getEmotion().equals("sadness")) {
                sad++;
            }
        }
        int amazingPercent = (amazing * 100) / dreams.size();
        int goodPercent = (good * 100) / dreams.size();
        int normalPercent = (normal * 100) / dreams.size();
        int badPercent = (bad * 100) / dreams.size();
        int sadPercent = (sad * 100) / dreams.size();

        ArrayList<Bitmap> imageList = new ArrayList<>();
        imageList.add(BitmapFactory.decodeResource(getResources(), R.drawable.amizingenable));
        imageList.add(BitmapFactory.decodeResource(getResources(), R.drawable.goodenable));
        imageList.add(BitmapFactory.decodeResource(getResources(), R.drawable.normalenable));
        imageList.add(BitmapFactory.decodeResource(getResources(), R.drawable.badnormalenable));
        imageList.add(BitmapFactory.decodeResource(getResources(), R.drawable.badenable));

        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);
        mChart.getDescription().setEnabled(false);
        mChart.setPinchZoom(false);
        mChart.setDrawGridBackground(false);


        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(7);
        xAxis.setDrawLabels(false);


        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setAxisLineColor(Color.WHITE);
        leftAxis.setDrawGridLines(false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);
        Legend l = mChart.getLegend();
        l.setEnabled(false);
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        yVals1.add(new BarEntry(4, amazingPercent));
        yVals1.add(new BarEntry(3, goodPercent));
        yVals1.add(new BarEntry(2, normalPercent));
        yVals1.add(new BarEntry(1, badPercent));
        yVals1.add(new BarEntry(0, sadPercent));


        BarDataSet set1 = new BarDataSet(yVals1, "");
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#eb6053"));
        colors.add(Color.parseColor("#eb9b9a"));
        colors.add(Color.parseColor("#e2c2bd"));
        colors.add(Color.parseColor("#9EBDD9"));
        colors.add(Color.parseColor("#6ca0c5"));
        set1.setColors(colors);

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);
        BarData data = new BarData(dataSets);
        data.setDrawValues(false);
        mChart.setData(data);
        mChart.getData().setBarWidth(0.1f);
        mChart.setScaleEnabled(false);
        mChart.setRenderer(new BarChartCustomRenderer(mChart, mChart.getAnimator(), mChart.getViewPortHandler(), imageList, getContext()));
        mChart.setExtraOffsets(0, 0, 0, 25);
    }

    //круговая диаграмма времени
    public void setupPieChartOfTime(List<Dream> dreams){
        //рассчитываем процент
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

        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        String label = " ";

        //иницилизация данных
        Map<String, Integer> typeAmountMap = new HashMap<>();
        typeAmountMap.put("Night",nightPercent);
        typeAmountMap.put("Morning",morningPercent);
        typeAmountMap.put("Day",dayPercent);

        //иницилизация цветов для графика
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#7096b6"));
        colors.add(Color.parseColor("#bbbfc2"));
        colors.add(Color.parseColor("#707173"));


        //input data and fit data into pie chart entry
        for(String type: typeAmountMap.keySet()){
            pieEntries.add(new PieEntry(typeAmountMap.get(type).intValue(), type));
        }

        PieDataSet pieDataSet = new PieDataSet(pieEntries,label);
        pieDataSet.setValueTextSize(14f);
        //указываем цвет диаграммы
        pieDataSet.setColors(colors);
        PieData pieData = new PieData(pieDataSet);
        pieData.setDrawValues(true);
        pieData.setValueTextColor(Color.parseColor("#FF000000"));
        pieData.setValueFormatter(new PercentFormatter());
        chartOfTime.setData(pieData);
        chartOfTime.invalidate();
        chartOfTime.setEntryLabelColor(Color.parseColor("#FF000000"));
        chartOfTime.getDescription().setEnabled(false);
        chartOfTime.setHoleRadius(0f);
        chartOfTime.setTransparentCircleRadius(0f);
        chartOfTime.getLegend().setTextColor(Color.WHITE);
        chartOfTime.getLegend().setTextSize(16f);
        chartOfTime.getLegend().setForm(Legend.LegendForm.CIRCLE);
    }

}
