package com.example.jake.chance_chain;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class HisActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_his);
        String userName = getIntent().getStringExtra("userName");
    }
}
