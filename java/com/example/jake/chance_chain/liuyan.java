package com.example.jake.chance_chain;

import android.content.Context;
import android.media.Image;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBQueryExpression;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class liuyan extends AppCompatActivity {

    LinearLayout stuff;
    String myUsr;
    String TextValue;
    Context context;
    AppHelper helper = new AppHelper();
    DynamoDBMapper mapper;
    HashMap<String,ChattingTableDO> mapping = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liuyan);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.chatbar);
        stuff = (LinearLayout) findViewById(R.id.stuff);
        context = getApplicationContext().getApplicationContext();
        myUsr = helper.getCurrentUserName(context);
        mapper = helper.getMapper(context);
        TextView titlebar = (TextView) actionBar.getCustomView().findViewById(R.id.title);
        titlebar.setText("消息");
        ImageView backImg = (ImageView) actionBar.getCustomView().findViewById(R.id.back);
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void onAddField(String uri, String username, String time, String txt, String num) {
        View layout1 = LayoutInflater.from(this).inflate(R.layout.liuitem, stuff, false);
        TextView myMsg = (TextView) layout1.findViewById(R.id.woshuo);
        ImageView wotou = (ImageView) layout1.findViewById(R.id.wotou);
        Picasso.get().load("https://s3.amazonaws.com/chance-userfiles-mobilehub-653619147/" + myUsr + ".png").placeholder(R.drawable.splash).into(wotou);
        if (!getMsg.isEmpty()) {
            myMsg.setText(getMsg);
            beijing.addView(layout1);
        }

        Log.d("beijing", String.valueOf(beijing.getChildCount()) + Picasso.get().load("https://s3.amazonaws.com/chance-userfiles-mobilehub-653619147/" + "sd" + ".png"));
    }

    Runnable setup = new Runnable() {
        @Override
        public void run() {
            ChattingTableDO receiver = new ChattingTableDO();
            ChattingTableDO sender = new ChattingTableDO();
            sender.setSender(myUsr);
            receiver.setReceiver(myUsr);
            DynamoDBQueryExpression sendExpression = new DynamoDBQueryExpression().withIndexName("FindSender").withConsistentRead(false).withHashKeyValues(sender);
            DynamoDBQueryExpression recExpression = new DynamoDBQueryExpression().withIndexName("FindReceiver").withConsistentRead(false).withHashKeyValues(receiver);
            List<ChattingTableDO> sendList = mapper.query(ChattingTableDO.class, sendExpression);
            List<ChattingTableDO> recList = mapper.query(ChattingTableDO.class, recExpression);
            List<ChattingTableDO> wholeList = new ArrayList<>();
            wholeList.addAll(recList);
            wholeList.addAll(sendList);
            if (wholeList.size() != 0) {
                Collections.sort(wholeList, new Comparator<ChattingTableDO>() {
                    @Override
                    public int compare(ChattingTableDO o1, ChattingTableDO o2) {
                        return o1.getChatId() > o2.getChatId() ? -1 : o1.getChatId() == o2.getChatId() ? 0 : 1;
                    }
                });
            }


        }

    };
}