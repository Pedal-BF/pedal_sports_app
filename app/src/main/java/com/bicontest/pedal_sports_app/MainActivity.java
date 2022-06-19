package com.bicontest.pedal_sports_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    MainFragment mainFragment;
    CommunityFragment communityFragment;
    BookmarksFragment bookmarksFragment;
    MypageFragment mypageFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainFragment = new MainFragment();
        communityFragment = new CommunityFragment();
        bookmarksFragment = new BookmarksFragment();
        mypageFragment = new MypageFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, mainFragment).commit();
        BottomNavigationView bottom_menu = findViewById(R.id.bottom_menu);
        bottom_menu.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.main_tab:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, mainFragment).commit();
                        return true;
                    case R.id.community_tab:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, communityFragment).commit();
                        return true;
                    case R.id.bookmarks_tab:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, bookmarksFragment).commit();
                        return true;
                    case R.id.mypage_tab:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, mypageFragment).commit();
                        return true;
                }
                return false;
            }
        });
    }
}