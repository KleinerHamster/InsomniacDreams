package com.example.insomniacdreams;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        //меняем цвет нижнего меню
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
            getWindow().setNavigationBarColor(getResources().getColor(R.color.background_color));

        SharedPreferences sharedPreferences = getSharedPreferences("user data", Context.MODE_PRIVATE);
        String login = sharedPreferences.getString("LOGIN", null);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(login==null) {
                    startActivity(new Intent(SplashScreen.this, SingIn.class));
                }
                else{
                    startActivity(new Intent(SplashScreen.this, MainScreen.class));
                }
                finish();
            }
        },5000);
    }
}