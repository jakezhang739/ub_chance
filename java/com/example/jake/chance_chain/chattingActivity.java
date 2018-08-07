package com.example.jake.chance_chain;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class chattingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.chatbar);
        ImageView back = (ImageView) actionBar.getCustomView().findViewById(R.id.back);
        TextView titlteText = (TextView) actionBar.getCustomView().findViewById(R.id.titletext);
        String userId = getIntent().getStringExtra("title").toString();
        titlteText.setText(userId);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(chattingActivity.this,HisActivity.class);
                startActivity(intent);
            }
        });
    }
}
