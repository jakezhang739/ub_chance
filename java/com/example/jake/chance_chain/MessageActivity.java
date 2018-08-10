package com.example.jake.chance_chain;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;

public class MessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        int unread =getIntent().getIntExtra("unread",0);
        RelativeLayout sixin = (RelativeLayout) findViewById(R.id.merel3);
        TextView unreadTag = (TextView) findViewById(R.id.sixinRead);
        if(unread>0){
            unreadTag.setVisibility(View.VISIBLE);
            unreadTag.setText(String.valueOf(unread));
        }
        sixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MessageActivity.this,liuyan.class);
                startActivity(intent);
            }
        });
    }
}
