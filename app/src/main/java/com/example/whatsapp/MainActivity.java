package com.example.whatsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TableLayout;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private TabLayout myTabLayout;
    private ViewPager myViewPager;
    private TabsAccessesorAdapter mytabsAccessesorAdapter;
    private FirebaseUser currentuser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar= (Toolbar) findViewById(R.id.main_page_toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("WhatsApp");

        myViewPager=(ViewPager)findViewById(R.id.main_tabs_pager);
        mytabsAccessesorAdapter=new TabsAccessesorAdapter(getSupportFragmentManager(),3);
        myViewPager.setAdapter(mytabsAccessesorAdapter);

        myTabLayout=(TabLayout)findViewById(R.id.main_tabs);
        myTabLayout.setupWithViewPager(myViewPager);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        if(currentuser==null)
        {
            Sendusertologinactivity();
        }
    }
    private void Sendusertologinactivity()
    {
        Intent loginintent=  new Intent(MainActivity.this,LoginActivity.class);
        startActivity(loginintent);
    }
}
