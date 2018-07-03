package com.example.jake.chance_chain;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class signUpActivity extends AppCompatActivity implements AWSLoginHandler {
    private Button finishBtn;
    AWSLoginModel awsLoginModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        awsLoginModel = new AWSLoginModel(this, this);
        finishBtn = (Button) findViewById(R.id.signUp);
        EditText username = (EditText) findViewById(R.id.editTextRegUserId);
        EditText pass = (EditText) findViewById(R.id.editTextRegUserPassword);
        EditText rePass = (EditText) findViewById(R.id.editTextRegGivenName);
        EditText phone = (EditText) findViewById(R.id.editTextRegPhone);
        EditText Email = (EditText) findViewById(R.id.editTextRegEmail);
        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("wtf","1 "+pass.getText().toString()+" 2 "+rePass.getText().toString());
                if(username.length() == 0 || pass.length()==0 || rePass.length() == 0 || phone.length()==0 || Email.length() == 0){
                    Toast.makeText(signUpActivity.this,"请完成所有信息",Toast.LENGTH_LONG).show();
                }

               else if(!pass.getText().toString().equals(rePass.getText().toString())){
                    Toast.makeText(signUpActivity.this,"两次密码不一样请重新输入",Toast.LENGTH_LONG).show();
                }
                else {
                    registerAction();
                }
            }
        });
    }

    @Override
    public void onRegisterSuccess(boolean mustConfirmToComplete) {

        Intent intent = new Intent(signUpActivity.this, confirmUser.class);
        startActivity(intent);

    }

    @Override
    public void onRegisterConfirmed() {

    }

    @Override
    public void onSignInSuccess() {

    }

    @Override
    public void onFailure(int process, Exception exception) {

        Toast.makeText(signUpActivity.this, "注册信息错误，请重新填写", Toast.LENGTH_LONG).show();

    }
    private void registerAction() {
        EditText username = (EditText) findViewById(R.id.editTextRegUserId);
        EditText pass = (EditText) findViewById(R.id.editTextRegUserPassword);
        EditText phone = (EditText) findViewById(R.id.editTextRegPhone);
        EditText Email = (EditText) findViewById(R.id.editTextRegEmail);

        // do sign in and handles on interface
        awsLoginModel.registerUser(username.getText().toString(), Email.getText().toString(), pass.getText().toString());
    }
}
