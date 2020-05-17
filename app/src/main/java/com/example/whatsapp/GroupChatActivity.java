package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;


public class GroupChatActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private ImageButton Sendmessagebutton;
    private EditText usermessageinput;
    private ScrollView mScrollview;
    private TextView DisplayTextmessages;
    private String currentGroupname,currentuserid,currentusername,currentDate,currentTime;
    private FirebaseAuth mAuth;
    private DatabaseReference usersreference,Groupnameref,GroupMessgrefkey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        currentGroupname=getIntent().getExtras().get("Groupname").toString();
        Toast.makeText(GroupChatActivity.this, currentGroupname, Toast.LENGTH_SHORT).show();
        mAuth=FirebaseAuth.getInstance();
        currentuserid=mAuth.getCurrentUser().getUid();
        usersreference= FirebaseDatabase.getInstance().getReference().child("Users");
        Groupnameref=FirebaseDatabase.getInstance().getReference().child("Groups").child(currentGroupname);

        Initializemethod();
        GetuserInfo();
        Sendmessagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Savemessageinfotodatabase();
                usermessageinput.setText("");
                mScrollview.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Groupnameref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists())
                {
                    Displaymessages(dataSnapshot);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists())
                {
                    Displaymessages(dataSnapshot);
                }

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void Displaymessages(DataSnapshot dataSnapshot) {
        Iterator iterator= dataSnapshot.getChildren().iterator();
        while (iterator.hasNext())
        {
            String chatdate=(String)((DataSnapshot)iterator.next()).getValue();
            String chatmessg=(String)((DataSnapshot)iterator.next()).getValue();
            String chatusername=(String)((DataSnapshot)iterator.next()).getValue();
            String chattime=(String)((DataSnapshot)iterator.next()).getValue();

            DisplayTextmessages.append(chatusername+" :\n" + chatmessg+"\n"+chatdate+"   "+chattime+"\n\n\n");
            mScrollview.fullScroll(ScrollView.FOCUS_DOWN);
        }
    }

    private void Savemessageinfotodatabase() {
        String usermessage=usermessageinput.getText().toString();
        String messagekey=Groupnameref.push().getKey();
        if(usermessage.isEmpty())
        {
            Toast.makeText(this, "Please Enter text first", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Calendar calfordate=Calendar.getInstance();
            SimpleDateFormat currentdateformat=new SimpleDateFormat("dd MMM,yyyy");
            currentDate=currentdateformat.format(calfordate.getTime());
            Calendar calfortime=Calendar.getInstance();
            SimpleDateFormat currenttimeformat=new SimpleDateFormat("hh:mm a");
            currentTime=currenttimeformat.format(calfortime.getTime());
            HashMap<String,Object> groupmessagekey= new HashMap<>();
            Groupnameref.updateChildren(groupmessagekey);
            GroupMessgrefkey=Groupnameref.child(messagekey);
            HashMap<String,Object> messageinfomap= new HashMap<>();
            messageinfomap.put("name",currentusername);
            messageinfomap.put("message",usermessage);
            messageinfomap.put("date",currentDate);
            messageinfomap.put("time",currentTime);
            GroupMessgrefkey.updateChildren(messageinfomap);

        }
    }

    private void GetuserInfo() {
        usersreference.child(currentuserid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    currentusername=dataSnapshot.child("name").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void Initializemethod() {
        mToolbar= findViewById(R.id.group_chat_appbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(currentGroupname);
        Sendmessagebutton= findViewById(R.id.send_message_image);
        usermessageinput= findViewById(R.id.input_group_message);
        mScrollview= findViewById(R.id.my_scroll_view);
        DisplayTextmessages= findViewById(R.id.group_chat_text_display);
    }
}
