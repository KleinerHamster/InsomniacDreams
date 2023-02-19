package com.example.insomniacdreams;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddDream extends AppCompatActivity{

    private DatePickerDialog datePickerDialog;
    private Button dateButton, simpleSleep, semiSleep, lucidSleep, timeNight, timeMorning, timeDaytime;
    private ImageView backToJournal,amazingView, goodView, normalView, badNormalView, badView, addNewDream;
    private TextInputEditText title_edit, description_edit;
    FirebaseFirestore dataBase;
    String emotionDream="", time="", sleep="";
    String yearDream="2022";
    String monthDream="12";
    String dayDream="16";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dream);
        sharedPreferences = getSharedPreferences("user data", Context.MODE_PRIVATE);
        //меняем цвет нижнего меню
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
            getWindow().setNavigationBarColor(getResources().getColor(R.color.background_color));

        dataBase = FirebaseFirestore.getInstance();

        title_edit = (TextInputEditText) findViewById(R.id.title_edit);
        description_edit= (TextInputEditText) findViewById(R.id.description_edit);

        //кнопка назад
        backToJournal=(ImageView) findViewById(R.id.backToJournal);
        backToJournal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainScreen.class));
            }
        });

        //эмоции
        amazingView=(ImageView) findViewById(R.id.amazing);
        goodView=(ImageView) findViewById(R.id.good);
        normalView=(ImageView) findViewById(R.id.normalImage);
        badNormalView=(ImageView) findViewById(R.id.badNormal);
        badView = (ImageView) findViewById(R.id.bad);

        //конпка радостного настроения
        amazingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amazingView.setImageResource(R.drawable.amizingenable);
                goodView.setImageResource(R.drawable.gooddisable);
                normalView.setImageResource(R.drawable.normaldisable);
                badNormalView.setImageResource(R.drawable.badnormaldisable);
                badView.setImageResource(R.drawable.badendisable);
                emotionDream="joy";
            }
        });

        //конпка хорошего настроения
        goodView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amazingView.setImageResource(R.drawable.amizingdisable);
                goodView.setImageResource(R.drawable.goodenable);
                normalView.setImageResource(R.drawable.normaldisable);
                badNormalView.setImageResource(R.drawable.badnormaldisable);
                badView.setImageResource(R.drawable.badendisable);
                emotionDream="good";
            }
        });

        //конпка среднего настроения
        normalView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amazingView.setImageResource(R.drawable.amizingdisable);
                goodView.setImageResource(R.drawable.gooddisable);
                normalView.setImageResource(R.drawable.normalenable);
                badNormalView.setImageResource(R.drawable.badnormaldisable);
                badView.setImageResource(R.drawable.badendisable);
                emotionDream="normal";
            }
        });

        //конпка плохого настроения
        badNormalView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amazingView.setImageResource(R.drawable.amizingdisable);
                goodView.setImageResource(R.drawable.gooddisable);
                normalView.setImageResource(R.drawable.normaldisable);
                badNormalView.setImageResource(R.drawable.badnormalenable);
                badView.setImageResource(R.drawable.badendisable);
                emotionDream="bad";
            }
        });

        //кнопка печали эмоции
        badView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amazingView.setImageResource(R.drawable.amizingdisable);
                goodView.setImageResource(R.drawable.gooddisable);
                normalView.setImageResource(R.drawable.normaldisable);
                badNormalView.setImageResource(R.drawable.badnormaldisable);
                badView.setImageResource(R.drawable.badenable);
                emotionDream="sadness";
            }
        });


        //выбор сна
        //простой сон
        simpleSleep = (Button) findViewById(R.id.simpleSleep);
        semiSleep = (Button) findViewById(R.id.semiSleep);
        lucidSleep = (Button) findViewById(R.id.lucidSleep);
        simpleSleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sleep="simple";
            }
        });

        //полуосозноный сон
        semiSleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sleep="semi lucid";
            }
        });

        //осозноный сон
        lucidSleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sleep="lucid";
            }
        });


        //выбор времени сна
        //ночной сон
        timeNight = (Button) findViewById(R.id.timeNight);
        timeNight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                time="night";
            }
        });

        //утренний сон
        timeMorning = (Button) findViewById(R.id.timeMorning);
        timeMorning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                time="morning";
            }
        });

        //дневной сон
        timeDaytime = (Button) findViewById(R.id.timeDaytime );
        timeDaytime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                time="day";
            }
        });


        //выбор даты
        initDatePicker();
        dateButton = findViewById(R.id.dateChoose);
        dateButton.setText(getTodayDate());
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePicker(view);
            }
        });

        String userID = sharedPreferences.getString("userId", null);
        //кнопка добавления
        addNewDream = (ImageView) findViewById(R.id.addNewDream);
        addNewDream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title =title_edit.getText().toString();
                String description = description_edit.getText().toString();
                createDream(title,description, time, sleep, emotionDream, userID);
            }
        });

    }

    //метод добавления новой записи сна
    public void createDream(String title, String description, String time, String sleep,String emotion, String userId){
        Date date = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateDelivery=yearDream+"-"+monthDream+"-"+dayDream;
        try {
            date = formatter.parse(dateDelivery);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Map<String, Object> dream = new HashMap<>();
        dream.put("Title", title);
        dream.put("Description", description);
        dream.put("Date", new Timestamp(date));
        dream.put("Time", time);
        dream.put("Sleep", sleep);
        dream.put("Emotion", emotion);
        dataBase.collection("dreamsUser").document(userId).collection("dreams")
                .add(dream).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG,"sus");
                startActivity(new Intent(getApplicationContext(), MainScreen.class));
            }
        });
    }

    //установка текущей даты
    private String getTodayDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    //иницилизация и выбор даты
    private void initDatePicker() {
        Calendar cal = Calendar.getInstance();
        int year1 = cal.get(Calendar.YEAR);
        int month1= cal.get(Calendar.MONTH);
        int day1 = cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_HOLO_DARK;

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                if(year<year1) {
                    month = month + 1;
                    String date = makeDateString(day, month, year);
                    dateButton.setText(date);
                    yearDream=String.valueOf(year);
                    monthDream=String.valueOf(month);
                    dayDream=String.valueOf(day);
                }
                else if (year==year1) {
                    if(month==month1 && day>day1){
                        Toast.makeText(getApplicationContext(), getResources().getText(R.string.error_msg_date), Toast.LENGTH_LONG).show();
                    }
                    else if (month>month1){
                        Toast.makeText(getApplicationContext(), String.valueOf(day1), Toast.LENGTH_LONG).show();
                    }
                    else{
                        month = month + 1;
                        String date = makeDateString(day, month, year);
                        dateButton.setText(date);
                        yearDream=String.valueOf(year);
                        monthDream=String.valueOf(month);
                        dayDream=String.valueOf(day);
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), getResources().getText(R.string.error_msg_date), Toast.LENGTH_LONG).show();
                }
            }
        };
        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year1, month1, day1);
    }

    //метод для вызова диалогового окна, выбора даты
    public void openDatePicker(View view)
    {
        datePickerDialog.show();
    }

    //метод для отображения
    private String makeDateString(int day, int month, int year)
    {
        dayDream=String.valueOf(day);
        yearDream=String.valueOf(year);
        monthDream=String.valueOf(month);
        return getMonthFormat(month) + " " + day + " " + year;
    }

    //метод для возврата месяца
    private String getMonthFormat(int month)
    {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";
        //по умолчанию январь
        return "JAN";
    }
}