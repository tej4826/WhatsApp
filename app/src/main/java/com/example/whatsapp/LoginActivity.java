package com.example.whatsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity
{
    private FirebaseUser currentuser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        if(currentuser!=null)
        {
            SendusertoMainactivity();
        }
    }

    private void SendusertoMainactivity()
    {
        Intent loginintent=  new Intent(LoginActivity.this,MainActivity.class);
        startActivity(loginintent);
    }
}
