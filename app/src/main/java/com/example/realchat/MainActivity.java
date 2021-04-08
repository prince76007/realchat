package com.example.realchat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.util.ArrayList;

import Model.Message;
import adapter.MessageAdapter;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference dbMessages,dbUser;

    RecyclerView recyclerView ;
    MessageAdapter messageAdapter;
    ArrayList<String> textMessage;
    ArrayList<Boolean> byMeOrNot;

    EditText sendingMessage;
    String userName="",uId;
    //LocalDateTime time;
    int i=1,j=1;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        dbMessages = database.getReference("message");
        dbUser=database.getReference("user");
        auth= FirebaseAuth.getInstance();

        textMessage=new ArrayList<String>();
        byMeOrNot=new ArrayList<Boolean>();
        setContentView(R.layout.activity_main);
        recyclerView=findViewById(R.id.chatRecyView);
        messageAdapter = new MessageAdapter(textMessage,byMeOrNot, this);

        recyclerView.setAdapter(messageAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        sendingMessage=findViewById(R.id.sendingMessage);
        FirebaseUser currentUser =  auth.getCurrentUser();
        if(currentUser==null){
            Intent intent= new Intent(this,SignUpActivity.class);
            startActivity(intent);
            finish();
        }else{
            uId=auth.getCurrentUser().getUid();
            dbUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    userName=snapshot.child(uId).getValue().toString();
                    Toast.makeText(MainActivity.this,"Welcome "+userName,Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


        dbMessages.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                textMessage.clear();
                byMeOrNot.clear();
                i=1;
                for(DataSnapshot message:snapshot.getChildren()){
                    textMessage.add(message.child("text").getValue().toString());
                    byMeOrNot.add(message.child("from").getValue().toString().equals(uId));
                    i++;
                }
messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        messageAdapter.notifyDataSetChanged();

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void sendMessage(View view){
        if(sendingMessage.getText().toString().trim().length()>0){
            Message message=new Message(uId,sendingMessage.getText().toString().trim(),LocalDateTime.now().toString());
            dbMessages.child("message"+i).setValue(message);
            sendingMessage.setText("");
            messageAdapter.notifyDataSetChanged();
        }
    }
}