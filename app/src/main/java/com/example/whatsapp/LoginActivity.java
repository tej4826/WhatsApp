package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity
{

    private Button LoginButton, PhoneLoginButton;
    private EditText UserEmail, UserPassword;
    private TextView Neednewaccount, forgetpassword;
    private FirebaseAuth mauth;
    private ProgressDialog loadingbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mauth=FirebaseAuth.getInstance();

        Initializefields();
        Neednewaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    SendusertoRegisteractivity();
            }
        });
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allowusertologin();
            }
        });
    }

    private void allowusertologin() {
        String email = UserEmail.getText().toString();
        String password= UserPassword.getText().toString();
        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(LoginActivity.this,"Please Enter email...",Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(LoginActivity.this,"Please Enter password...",Toast.LENGTH_LONG).show();
        }
        else
        {
            loadingbar.setTitle("Signing in to Account");
            loadingbar.setMessage("Please Wait");
            loadingbar.setCanceledOnTouchOutside(true);
            loadingbar.show();
            mauth.signInWithEmailAndPassword(email,password).addOnCompleteListener(
                    new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                SendusertoMainactivity();
                                Toast.makeText(LoginActivity.this,"Logged in Successfully",Toast.LENGTH_LONG).show();
                                loadingbar.dismiss();
                            }
                            else
                            {
                                String message = task.getException().toString();
                                Toast.makeText(LoginActivity.this,"Error:"+message,Toast.LENGTH_SHORT).show();
                                loadingbar.dismiss();
                            }
                        }
                    }
            );
        }

    }

    private void Initializefields() {
        LoginButton = (Button) findViewById(R.id.login_button);
        PhoneLoginButton=(Button)findViewById(R.id.phone_login_new_button);
        UserEmail = (EditText) findViewById(R.id.email);
        UserPassword=(EditText)findViewById(R.id.password);
        Neednewaccount=(TextView)findViewById(R.id.need_new_account);
        forgetpassword=(TextView)findViewById(R.id.forget_password);
        loadingbar=new ProgressDialog(this);


    }



    private void SendusertoMainactivity()
    {
        Intent loginintent=  new Intent(LoginActivity.this,MainActivity.class);
        loginintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginintent);
        finish();
    }
    private void SendusertoRegisteractivity()
    {
        Intent registerintent=  new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(registerintent);
    }
}
