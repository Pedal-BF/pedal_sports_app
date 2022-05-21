package com.example.pedal_sports_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    MainHome mainHome;
    Community community;
    BookMarks bookMarks;
    MyPage myPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, LoadingActivity.class);
        startActivity(intent);

        mainHome = new MainHome();
        community = new Community();
        bookMarks = new BookMarks();
        myPage = new MyPage();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, mainHome).commit();
        BottomNavigationView bottom_menu_bar = findViewById(R.id.bottom_menu_bar);
        bottom_menu_bar.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.first_tab:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, mainHome).commit();
                        return true;
                    case R.id.second_tab:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, community).commit();
                        return true;
                    case R.id.third_tab:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, bookMarks).commit();
                        return true;
                    case R.id.fourth_tab:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, myPage).commit();
                        return true;
                }
                return false;
            }
        });
    }
}