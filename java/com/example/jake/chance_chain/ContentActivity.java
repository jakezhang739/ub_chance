package com.example.jake.chance_chain;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ContentActivity extends AppCompatActivity {
    private chanceClass chanceC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        //chanceC = new chanceClass();
        chanceC = (chanceClass) getIntent().getParcelableExtra("cc");
    }

}
