package com.example.doo88.pocketv;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        fgadapter adapter = new fgadapter(getSupportFragmentManager());
        ViewPager viewpager =(ViewPager)findViewById(R.id.viewpager);
        viewpager.setAdapter(adapter);
        TabLayout tab = (TabLayout)findViewById(R.id.tab);
        tab.setupWithViewPager(viewpager);
    }
}
