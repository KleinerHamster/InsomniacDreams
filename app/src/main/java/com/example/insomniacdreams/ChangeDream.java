package com.example.insomniacdreams;

import static android.content.ContentValues.TAG;

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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ChangeDream extends AppCompatActivity {

    private DatePickerDialog datePickerDialog;
    private Button dateButton, simpleSleep, semiSleep, lucidSleep, timeNight, timeMorning, timeDaytime;
    private ImageView backToJournal,amazingView, goodView, normalView, badNormalView, badView, addNewDream;
    private TextInputEditText title_edit, description_edit;
    FirebaseFirestore dataBase;
    String emotionDream, time, sleep, dreamID;
    String yearDream="2022";
    String monthDream="12";
    String dayDream="8";
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_dream);
        sharedPreferences = getSharedPreferences("user data", Context.MODE_PRIVATE);
        dataBase = FirebaseFirestore.getInstance();
        //меняем цвет нижнего меню
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
            getWindow().setNavigationBarColor(getResources().getColor(R.color.background_color));

        //эмоции
        amazingView=(ImageView) findViewById(R.id.amazing);
        goodView=(ImageView) findViewById(R.id.good);
        normalView=(ImageView) findViewById(R.id.normalImage);
        badNormalView=(ImageView) findViewById(R.id.badNormal);
        badView = (ImageView) findViewById(R.id.bad);

        simpleSleep = (Button) findViewById(R.id.simpleSleep);
        semiSleep = (Button) findViewById(R.id.semiSleep);
        lucidSleep = (Button) findViewById(R.id.lucidSleep);

        timeNight = (Button) findViewById(R.id.timeNight);
        timeMorning = (Button) findViewById(R.id.timeMorning);
        timeDaytime = (Button) findViewById(R.id.timeDaytime );

        //получаем данные для изменения
        dreamID=getIntent().getStringExtra("id");
        title_edit = (TextInputEditText) findViewById(R.id.title_edit);
        title_edit.setText(getIntent().getStringExtra("title"));
        description_edit= (TextInputEditText) findViewById(R.id.description_edit);
        description_edit.setText(getIntent().getStringExtra("description"));
        emotionDream=getIntent().getStringExtra("emotion");
        moodSet(emotionDream);
        time=getIntent().getStringExtra("time");
        timeSet(time);
        sleep=getIntent().getStringExtra("sleep");
        sleepSet(sleep);
        dayDream=getIntent().getStringExtra("day");
        monthDream=getIntent().getStringExtra("month");
        yearDream=getIntent().getStringExtra("year");

        //выбор времени сна
        //ночной сон
        timeNight.setOnClickListener(view -> {
            timeNight.setSelected(true);
            timeMorning.setSelected(false);
            timeDaytime.setSelected(false);
            time="night";
        });

        //утренний сон
        timeMorning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeMorning.setSelected(true);
                timeNight.setSelected(false);
                timeDaytime.setSelected(false);
                time="morning";
            }
        });

        //дневной сон
        timeDaytime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeDaytime.setSelected(true);
                timeNight.setSelected(false);
                timeMorning.setSelected(false);
                time="day";
            }
        });
        //выбор сна
        //простой сон
        simpleSleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                simpleSleep.setSelected(true);
                semiSleep.setSelected(false);
                lucidSleep.setSelected(false);
                sleep="simple";
            }
        });

        //полуосозноный сон
        semiSleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                simpleSleep.setSelected(false);
                semiSleep.setSelected(true);
                lucidSleep.setSelected(false);
                sleep="semi lucid";
            }
        });

        //осозноный сон
        lucidSleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                simpleSleep.setSelected(false);
                semiSleep.setSelected(false);
                lucidSleep.setSelected(true);
                sleep="lucid";
            }
        });

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
        goodView.setOnClickListener(view -> {
            amazingView.setImageResource(R.drawable.amizingdisable);
            goodView.setImageResource(R.drawable.goodenable);
            normalView.setImageResource(R.drawable.normaldisable);
            badNormalView.setImageResource(R.drawable.badnormaldisable);
            badView.setImageResource(R.drawable.badendisable);
            emotionDream="good";
        });

        //конпка среднего настроения
        normalView.setOnClickListener(view -> {
            amazingView.setImageResource(R.drawable.amizingdisable);
            goodView.setImageResource(R.drawable.gooddisable);
            normalView.setImageResource(R.drawable.normalenable);
            badNormalView.setImageResource(R.drawable.badnormaldisable);
            badView.setImageResource(R.drawable.badendisable);
            emotionDream="normal";
        });

        //конпка плохого настроения
        badNormalView.setOnClickListener(view -> {
            amazingView.setImageResource(R.drawable.amizingdisable);
            goodView.setImageResource(R.drawable.gooddisable);
            normalView.setImageResource(R.drawable.normaldisable);
            badNormalView.setImageResource(R.drawable.badnormalenable);
            badView.setImageResource(R.drawable.badendisable);
            emotionDream="bad";
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

        //кнопка назад
        backToJournal=(ImageView) findViewById(R.id.backToJournal);
        backToJournal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainScreen.class));
            }
        });

        //выбор даты
        initDatePicker();
        dateButton = findViewById(R.id.dateChoose);
        dateButton.setText(getDateDream());
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePicker(view);
            }
        });

        String userID = sharedPreferences.getString("userId", null);
        //кнопка изменения
        addNewDream = (ImageView) findViewById(R.id.addNewDream);
        addNewDream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title =title_edit.getText().toString();
                String description = description_edit.getText().toString();
                updateDream(title,description, time, sleep, emotionDream, userID, dreamID);
            }
        });
    }

    //метод добавления новой записи сна
    public void updateDream(String title, String description, String time, String sleep,String emotion, String userId, String dreamID){
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
                .document(dreamID).set(dream).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG,"sus");
                startActivity(new Intent(getApplicationContext(), MainScreen.class));
            }
        });
    }


    //метод для выброного настроения
    public void moodSet(String mood){
        if(mood.equals("joy"))
            amazingView.setImageResource(R.drawable.amizingenable);
        else if(mood.equals("good"))
            goodView.setImageResource(R.drawable.goodenable);
        else if(mood.equals("normal"))
            normalView.setImageResource(R.drawable.normalenable);
        else if(mood.equals("bad"))
            badNormalView.setImageResource(R.drawable.badnormalenable);
        else if(mood.equals("sadness"))
            badView.setImageResource(R.drawable.badenable);
    }

    //метод для выбранного типа сна
    public void sleepSet(String type){
        if(type.equals("simple"))
            simpleSleep.setSelected(true);
        else if(type.equals("semi lucid"))
            semiSleep.setSelected(true);
        else if(type.equals("lucid"))
            lucidSleep.setSelected(true);
    }

    //метод для выбранного времени сна
    public void timeSet(String type){
        if(type.equals("night"))
            timeNight.setSelected(true);
        else if(type.equals("morning"))
            timeMorning.setSelected(true);
        else if(type.equals("day"))
            timeDaytime.setSelected(true);
    }

    //установка текущей даты
    private String getDateDream() {
        return makeDateString(Integer.parseInt(dayDream), Integer.parseInt(monthDream),
                Integer.parseInt(yearDream));
    }

    //иницилизация и выбор даты
    private void initDatePicker() {
        Calendar cal = Calendar.getInstance();
        int year1 = cal.get(Calendar.YEAR);
        int month1= cal.get(Calendar.MONTH);
        int day1 = cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_HOLO_DARK;

        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, month, day) -> {
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