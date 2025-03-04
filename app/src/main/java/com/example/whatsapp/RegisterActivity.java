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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private Button createaccountbutton;
    private TextView alreadyhaveaccount;
    private EditText UserEmail, UserPassword;
    private FirebaseAuth mauth;
    private ProgressDialog loadingbar;
    private DatabaseReference dataref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mauth=FirebaseAuth.getInstance();
        dataref= FirebaseDatabase.getInstance().getReference();
        InitializeFields();
        alreadyhaveaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendusertoLoginactivity();
            }
        });
        createaccountbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreatenewAccount();
            }
        });
    }
    private void CreatenewAccount(){
            String email = UserEmail.getText().toString();
            String password= UserPassword.getText().toString();
            if(TextUtils.isEmpty(email))
            {
                Toast.makeText(RegisterActivity.this,"Please Enter email...",Toast.LENGTH_LONG).show();
            }
            else if(TextUtils.isEmpty(password))
            {
                Toast.makeText(RegisterActivity.this,"Please Enter password...",Toast.LENGTH_LONG).show();
            }
            else
            {
                loadingbar.setTitle("Creating New Account");
                loadingbar.setMessage("Please Wait");
                loadingbar.setCanceledOnTouchOutside(true);
                loadingbar.show();
                mauth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        String Currentuserid=mauth.getCurrentUser().getUid();
                        dataref.child("Users").child(Currentuserid).setValue("");
                        Sendusertomainactivity();
                        Toast.makeText(RegisterActivity.this,"Account Created Successfully",Toast.LENGTH_LONG).show();
                        loadingbar.dismiss();
                    }
                    else
                    {
                        String message = task.getException().toString();
                        Toast.makeText(RegisterActivity.this,"Error:"+message,Toast.LENGTH_SHORT).show();
                        loadingbar.dismiss();

                    }
                }
            });
        }

    }

    private void Sendusertomainactivity() {
        Intent loginintent=  new Intent(RegisterActivity.this,MainActivity.class);
        startActivity(loginintent);
    }

    private void InitializeFields() {
        createaccountbutton= findViewById(R.id.reg_login_button);
        alreadyhaveaccount= findViewById(R.id.already_have_account);
        UserEmail = findViewById(R.id.reg_email);
        UserPassword= findViewById(R.id.reg_password);
        loadingbar=new ProgressDialog(this);
    }
    private void SendusertoLoginactivity()
    {
        Intent mainintent=  new Intent(RegisterActivity.this,LoginActivity.class);
        mainintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainintent);
        finish();
    }
}
