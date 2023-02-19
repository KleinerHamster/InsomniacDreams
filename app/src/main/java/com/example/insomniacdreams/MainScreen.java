package com.example.insomniacdreams;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        //меняем цвет нижнего меню
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
            getWindow().setNavigationBarColor(getResources().getColor(R.color.background_color));

        BottomNavigationView navigationView = (BottomNavigationView) findViewById(R.id.navigation);
        navigationView.setOnItemSelectedListener(mOnItemSelectedListener);
        loadFragment(JournalFragment.newInstance());
    }

    //нажатие на выбранный эл-т меню
    private NavigationBarView.OnItemSelectedListener mOnItemSelectedListener =
            new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.navigation_home:
                            loadFragment(JournalFragment.newInstance());
                            return true;
                        case R.id.navigation_dream_book:
                            loadFragment(DreamBookFragment.newInstance());
                            return true;
                        case R.id.navigation_statistics:
                            loadFragment(StatisticFragment.newInstance());
                            return true;
                    }
                    return false;
                }
            };

    private void loadFragment(Fragment fragment){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_content, fragment);
        ft.commit();
    }
}