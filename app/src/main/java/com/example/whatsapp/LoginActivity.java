package com.example.whatsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity
{
    private FirebaseUser currentuser;
    private Button LoginButton, PhoneLoginButton;
    private EditText UserEmail, UserPassword;
    private TextView Neednewaccount, forgetpassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Initializefields();
        Neednewaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    SendusertoRegisteractivity();
            }
        });
    }

    private void Initializefields() {
        LoginButton = (Button) findViewById(R.id.login_button);
        PhoneLoginButton=(Button)findViewById(R.id.phone_login_new_button);
        UserEmail = (EditText) findViewById(R.id.email);
        UserPassword=(EditText)findViewById(R.id.password);
        Neednewaccount=(TextView)findViewById(R.id.need_new_account);
        forgetpassword=(TextView)findViewById(R.id.forget_password);

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
    private void SendusertoRegisteractivity()
    {
        Intent registerintent=  new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(registerintent);
    }
}
