package com.example.jake.chance_chain;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class fabuActivity extends AppCompatActivity {
    Context context;
    String myUsr;
    AppHelper helper = new AppHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fabu);
        ActionBar actionBar = getSupportActionBar();
        context = getApplicationContext().getApplicationContext();
        myUsr = helper.getCurrentUserName(context);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.chatbar);
        ImageView back = (ImageView) actionBar.getCustomView().findViewById(R.id.back);
        TextView titlteText = (TextView) actionBar.getCustomView().findViewById(R.id.title);
        titlteText.setText("我的发布");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView weiJinxin = (TextView) findViewById(R.id.wjx);
        TextView jinXinZhong = (TextView) findViewById(R.id.jxz);
        TextView yiWanCheng = (TextView) findViewById(R.id.ywc);
        TextView tag1 = (TextView) findViewById(R.id.tag1);
        TextView tag2 = (TextView) findViewById(R.id.tag2);
        TextView tag3 = (TextView) findViewById(R.id.tag3);
        weiJinxin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tag1.setVisibility(View.VISIBLE);
                tag2.setVisibility(View.INVISIBLE);
                tag3.setVisibility(View.INVISIBLE);

            }
        });

        jinXinZhong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tag1.setVisibility(View.INVISIBLE);
                tag2.setVisibility(View.VISIBLE);
                tag3.setVisibility(View.INVISIBLE);
            }
        });

        yiWanCheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tag1.setVisibility(View.INVISIBLE);
                tag2.setVisibility(View.INVISIBLE);
                tag3.setVisibility(View.VISIBLE);
            }
        });

    }

    Runnable myFabu = new Runnable() {
        @Override
        public void run() {

        }
    };
}
