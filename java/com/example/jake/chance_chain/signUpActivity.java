package com.example.jake.chance_chain;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class signUpActivity extends AppCompatActivity {
    private Button finishBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        finishBtn = (Button) findViewById(R.id.signUp);
        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signUpActivity.this,confirmUser.class);
                startActivity(intent);
            }
        });
    }
}
