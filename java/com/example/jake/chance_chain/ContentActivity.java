package com.example.jake.chance_chain;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;

public class ContentActivity extends AppCompatActivity {
    private chanceClass chanceC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.scrollRel);
        chanceC = (chanceClass) getIntent().getParcelableExtra("cc");
    }

}
