package com.example.jake.chance_chain;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {

    private Button loginBtn;
    private TextView signupText,forgotText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginBtn = (Button) findViewById(R.id.buttonLogIn);
        signupText = (TextView) findViewById(R.id.textViewUserSignUp);
        forgotText = (TextView) findViewById(R.id.textViewUserForgotPassword);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,confirmUser.class);
                startActivity(intent);
            }
        });
        signupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,signUpActivity.class);
                startActivity(intent);
            }
        });
        forgotText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,forgotPassword.class);
                startActivity(intent);
            }
        });

    }
}
