package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private TabLayout myTabLayout;
    private ViewPager myViewPager;
    private TabsAccessesorAdapter mytabsAccessesorAdapter;
    private FirebaseUser currentuser;
    private FirebaseAuth mauth;
    private DatabaseReference rootref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mauth=FirebaseAuth.getInstance();
        rootref= FirebaseDatabase.getInstance().getReference();
        currentuser=mauth.getCurrentUser();
        mToolbar= (Toolbar) findViewById(R.id.main_page_toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("WhatsApp");

        myViewPager= findViewById(R.id.main_tabs_pager);
        mytabsAccessesorAdapter=new TabsAccessesorAdapter(getSupportFragmentManager(),3);
        myViewPager.setAdapter(mytabsAccessesorAdapter);

        myTabLayout= findViewById(R.id.main_tabs);
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
        else{
            VerifyUserExistence();
        }
    }

    private void VerifyUserExistence() {
        String currentUserID = mauth.getCurrentUser().getUid();
        rootref.child("Users").child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("name").exists())
                {
                    Toast.makeText(MainActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    SendusertoSettingsActivity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void Sendusertologinactivity()
    {
        Intent loginintent=  new Intent(MainActivity.this,LoginActivity.class);
        loginintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginintent);
        finish();
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.options_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId()==R.id.main_findfriends)
        {

        }
        if(item.getItemId()==R.id.main_create_group)
        {
            RequestNewGroup();
        }
        if(item.getItemId()==R.id.main_settings)
        {
            SendusertoSettingsActivity();
        }
        if(item.getItemId()==R.id.main_logout)
        {
            mauth.signOut();
            Sendusertologinactivity();
        }
        return true;
    }

    private void RequestNewGroup() {
        AlertDialog.Builder builder= new AlertDialog.Builder(MainActivity.this,R.style.AlertDialog);
        builder.setTitle("Enter Group Name");
        final EditText groupnamefield= new EditText(MainActivity.this);
        groupnamefield.setHint("ex: Y19 Rockers");
        builder.setView(groupnamefield);
        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String Groupname= groupnamefield.getText().toString();
                if(Groupname.isEmpty())
                {
                    Toast.makeText(MainActivity.this, "Please Enter Group Name", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    CreateNewGroup(Groupname);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               dialog.cancel();
            }
        });
        builder.show();
    }

    private void CreateNewGroup(final String groupname) {
        rootref.child("Groups").child(groupname).setValue("").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(MainActivity.this, groupname+" Group Created Successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void SendusertoSettingsActivity()
    {
        Intent settingsintent=  new Intent(MainActivity.this,SettingsActivity.class);
        settingsintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(settingsintent);
        finish();
    }

}
