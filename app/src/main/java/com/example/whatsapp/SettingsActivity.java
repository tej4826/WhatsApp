package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {
    private Button UpdateAccountSettings;
    private EditText username, status;
    private CircleImageView userprofileimage;
    private String CurrentUserId;
    private FirebaseAuth mauth;
    private DatabaseReference dataref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mauth=FirebaseAuth.getInstance();
        CurrentUserId=mauth.getCurrentUser().getUid();
        dataref= FirebaseDatabase.getInstance().getReference();
        InitializeFields();

        UpdateAccountSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateSettings();
            }
        });
        RetrieveUserInfo();
    }

    private void RetrieveUserInfo() {
        dataref.child("Users").child(CurrentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if((dataSnapshot.exists()) && dataSnapshot.hasChild("name") && dataSnapshot.hasChild("image"))
                {
                    String retrieveusername=dataSnapshot.child("name").getValue().toString();
                    String retrievestatus=dataSnapshot.child("status").getValue().toString();
                    String retrieveprofileimage=dataSnapshot.child("image").getValue().toString();
                    username.setText(retrieveusername);
                    status.setText(retrievestatus);
                }
                else if((dataSnapshot.exists()) && dataSnapshot.hasChild("name"))
                {
                    String retrieveusername=dataSnapshot.child("name").getValue().toString();
                    String retrievestatus=dataSnapshot.child("status").getValue().toString();
                    username.setText(retrieveusername);
                    status.setText(retrievestatus);
                }
                else
                {
                    username.setVisibility(View.VISIBLE);
                    Toast.makeText(SettingsActivity.this, "Please Update your Credentials", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void UpdateSettings() {
        String setUsername= username.getText().toString();
        String setUserstatus=status.getText().toString();
        if(TextUtils.isEmpty(setUserstatus))
        {
            setUserstatus="Can't talk WhatsApp Only";
        }
        if(TextUtils.isEmpty(setUsername))
        {
            Toast.makeText(this, "Please enter Username", Toast.LENGTH_SHORT).show();
        }
        else
        {
            HashMap <String, String> profileMap=new HashMap<>();
                profileMap.put("uid",CurrentUserId);
                profileMap.put("name",setUsername);
                profileMap.put("status",setUserstatus);
            dataref.child("Users").child(CurrentUserId).setValue(profileMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        SendusertoMainactivity();
                        Toast.makeText(SettingsActivity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        String error=task.getException().toString();
                        Toast.makeText(SettingsActivity.this, "Error"+error, Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

    private void InitializeFields() {
        UpdateAccountSettings=(Button) findViewById(R.id.update_settings_button);
        username=(EditText) findViewById(R.id.set_username);
        status=(EditText) findViewById(R.id.set_status);
        userprofileimage=(CircleImageView) findViewById(R.id.set_profile_image);

    }
    private void SendusertoMainactivity()
    {
        Intent settingintent=  new Intent(SettingsActivity.this,MainActivity.class);
        settingintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(settingintent);
        finish();
    }
}
