package com.example.insomniacdreams;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Date;

//сортировка
public class MyComparator implements Comparator<Dream> {

    //метод для сортировки по убыванию
    public int compare(Dream s1, Dream s2)
    {
        Date date1 = s1.getDate().toDate();
        Date date2 = s2.getDate().toDate();
        if (date1.compareTo(date2)==0)
            return 0;
        else if (date1.compareTo(date2)<0)
            return 1;
        else
            return -1;
    }
}
