package com.example.jake.chance_chain;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;

public class myWallet extends AppCompatActivity {

    Context context;
    TextView currency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wallet);
        context = getApplicationContext().getApplicationContext();
        new Thread(setup).start();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.chatbar);
        ImageView back = (ImageView) actionBar.getCustomView().findViewById(R.id.back);
        TextView title = (TextView) actionBar.getCustomView().findViewById(R.id.title);
        title.setText("我的钱包");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(myWallet.this,MyActivity.class);
                startActivity(intent);
            }
        });
        TextView guanli = (TextView) findViewById(R.id.textView11);
        currency = (TextView) findViewById(R.id.textView12);
        guanli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    Handler setupHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
        currency.setText("CC: "+msg.obj.toString());
        }
    };

    Runnable setup = new Runnable() {
        @Override
        public void run() {
            AppHelper helper = new AppHelper();
            DynamoDBMapper mapper = helper.getMapper(context);
            String user = helper.getCurrentUserName(context);
            UserPoolDO userPoolDO = mapper.load(UserPoolDO.class,user);
            Message msg = new Message();
            if(userPoolDO.getCandyCurrency()!=null){
                msg.obj=userPoolDO.getCandyCurrency().intValue();
            }
            else {
                msg.obj=0;
            }
            setupHandler.sendMessage(msg);


        }
    };

}
