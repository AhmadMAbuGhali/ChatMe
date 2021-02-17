package com.power.chatme;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {
        private Toolbar mToolbar;
        private ViewPager myViewPager;
        private TabLayout myTabLayout;
        private  TabsAccessorAdapter myTabsAccessorAdapter;
        private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Chat Me");


        myViewPager = findViewById(R.id.main_tabs_Pager);
        myTabsAccessorAdapter = new TabsAccessorAdapter(getSupportFragmentManager());
        myViewPager.setAdapter(myTabsAccessorAdapter);

        myTabLayout = findViewById(R.id.main_tabs);
        myTabLayout.setupWithViewPager(myViewPager);


    }

    @Override
    protected void onStart() {
        super.onStart();
        if(currentUser==null){
            SendUserToRegActivity();

        }
    }

    private void SendUserToRegActivity() {

        Intent regIntent = new Intent(MainActivity.this,RegisterActivity.class);
        startActivity(regIntent);
    }
}