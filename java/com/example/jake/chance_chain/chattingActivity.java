package com.example.jake.chance_chain;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBQueryExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedList;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class chattingActivity extends AppCompatActivity {
    LinearLayout beijing;
    EditText getInput;
    String myUsr;
    String TextValue;
    Context context;
    AppHelper helper = new AppHelper();
    DynamoDBMapper mapper;
    String userId;
    AmazonDynamoDBClient dbClient;
    ImageView addText;
    RelativeLayout liaotiankuang;
    TextView tianconglan;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        ActionBar actionBar = getSupportActionBar();
        context = getApplicationContext().getApplicationContext();
        myUsr = helper.getCurrentUserName(context);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.chatbar);
        ImageView back = (ImageView) actionBar.getCustomView().findViewById(R.id.back);
        TextView titlteText = (TextView) actionBar.getCustomView().findViewById(R.id.title);
        userId = getIntent().getStringExtra("title").toString();
        mapper = helper.getMapper(context);
        titlteText.setText(userId);
        beijing = (LinearLayout) findViewById(R.id.liaobeijing);
        getInput = (EditText) findViewById(R.id.inputMsg);
        dbClient = helper.getClient(context);
        progressBar = (ProgressBar) findViewById(R.id.progressBarchat);
        liaotiankuang = (RelativeLayout) findViewById(R.id.liaotian);
        tianconglan = (TextView) findViewById(R.id.tianc);
        new Thread(ReceiverListener).start();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        addText = (ImageView) findViewById(R.id.fasongxiao);
        addText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextValue = getInput.getText().toString();
                addText.setVisibility(View.INVISIBLE);
                getInput.setText("");
                new Thread(sendMsg).start();
            }
        });

    }

    Handler addHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1:progressBar.setVisibility(View.INVISIBLE);liaotiankuang.setVisibility(View.VISIBLE);tianconglan.setVisibility(View.VISIBLE);ChattingTableDO chattingTableDO = (ChattingTableDO) msg.obj;
                    onAddMyField(chattingTableDO.getText());break;
                case 2:progressBar.setVisibility(View.INVISIBLE);liaotiankuang.setVisibility(View.VISIBLE);tianconglan.setVisibility(View.VISIBLE);ChattingTableDO chattingTableDO1 = (ChattingTableDO) msg.obj;
                    onAddHisField(chattingTableDO1.getText());break;
                case 3:addText.setVisibility(View.VISIBLE);progressBar.setVisibility(View.INVISIBLE);break;
                case 4:liaotiankuang.setVisibility(View.VISIBLE);tianconglan.setVisibility(View.VISIBLE);progressBar.setVisibility(View.INVISIBLE);break;
                case 5:ChattingTableDO chattingTableDO3 = (ChattingTableDO) msg.obj;onAddMyField(chattingTableDO3.getText());break;
                case 6:ChattingTableDO chattingTableDO4 = (ChattingTableDO) msg.obj;onAddHisField(chattingTableDO4.getText());break;
            }
        }
    };


    public void onAddMyField(String getMsg) {
        View layout1 = LayoutInflater.from(this).inflate(R.layout.wodeliaotian, beijing, false);
        TextView myMsg = (TextView) layout1.findViewById(R.id.woshuo);
        ImageView wotou = (ImageView) layout1.findViewById(R.id.wotou);
        Picasso.get().load("https://s3.amazonaws.com/chance-userfiles-mobilehub-653619147/" + myUsr + ".png").placeholder(R.drawable.splash).into(wotou);
        if (!getMsg.isEmpty()) {
            myMsg.setText(getMsg);
            beijing.addView(layout1);
        }

        Log.d("beijing", String.valueOf(beijing.getChildCount()) + Picasso.get().load("https://s3.amazonaws.com/chance-userfiles-mobilehub-653619147/" + "sd" + ".png"));
    }

    public void onAddHisField(String getMsg) {
        View layout1 = LayoutInflater.from(this).inflate(R.layout.tadeliaotian, beijing, false);
        TextView myMsg = (TextView) layout1.findViewById(R.id.tashuo);
        ImageView wotou = (ImageView) layout1.findViewById(R.id.tadetou);
        Picasso.get().load("https://s3.amazonaws.com/chance-userfiles-mobilehub-653619147/" + userId + ".png").placeholder(R.drawable.splash).into(wotou);
        if (!getMsg.isEmpty()) {
            myMsg.setText(getMsg);
            beijing.addView(layout1);
        }

        Log.d("beijing", String.valueOf(beijing.getChildCount()) + Picasso.get().load("https://s3.amazonaws.com/chance-userfiles-mobilehub-653619147/" + "sd" + ".png"));
    }

    Runnable sendMsg = new Runnable() {
        @Override
        public void run() {
            final ChattingTableDO chattingTableDO = new ChattingTableDO();
            int chatsize = helper.returnChatSize(mapper);
            chattingTableDO.setChatId((double)chatsize);
            chattingTableDO.setSender(myUsr);
            chattingTableDO.setReceiver(userId);
            chattingTableDO.setText(TextValue);
            Date currentTime = Calendar.getInstance().getTime();
            String dateString = DateFormat.format("yyyyMMddHHmmss", new Date(currentTime.getTime())).toString();
            chattingTableDO.setTime(dateString);
            chattingTableDO.setReadFlag("unRead");
            mapper.save(chattingTableDO);
            Message msg = new Message();
            msg.what = 3;
            addHandler.sendMessage(msg);
        }
    };

    Runnable ReceiverListener = new Runnable() {
        @Override
        public void run() {
             //Condition condition = new Condition().withAttributeValueList(new AttributeValue().withS("1"));
                    int sendSize,recSize;
                    ChattingTableDO receiver = new ChattingTableDO();
                    ChattingTableDO sender = new ChattingTableDO();
                    sender.setSender(myUsr);
                    receiver.setReceiver(myUsr);
                    DynamoDBQueryExpression sendExpression = new DynamoDBQueryExpression().withIndexName("FindSender").withConsistentRead(false).withHashKeyValues(sender);
                    DynamoDBQueryExpression recExpression = new DynamoDBQueryExpression().withIndexName("FindReceiver").withConsistentRead(false).withHashKeyValues(receiver);
                    List<ChattingTableDO> sendList = mapper.query(ChattingTableDO.class,sendExpression);
                    List<ChattingTableDO> recList = mapper.query(ChattingTableDO.class,recExpression);
                    sendSize = sendList.size();
                    recSize = recList.size();
                    List<ChattingTableDO> wholeList = new ArrayList<>();
                    wholeList.addAll(recList);
                    wholeList.addAll(sendList);
                    if(wholeList.size()!=0){
                    Collections.sort(wholeList, new Comparator<ChattingTableDO>() {
                        @Override
                        public int compare(ChattingTableDO o1, ChattingTableDO o2) {
                            return o1.getChatId() > o2.getChatId() ? 1 : o1.getChatId() == o2.getChatId() ? 0 : -1;
                        }
                    });
//                    QueryRequest queryRequest = new QueryRequest().withTableName("ChattingTable").
                    Log.d("gooooood",String.valueOf(sendList.size())+String.valueOf(recList.size()));
                    for(int i=0;i<wholeList.size();i++){
                        Log.d("sendername ",wholeList.get(i).getSender());
                        if(wholeList.get(i).getSender().equals(myUsr)){
                            Message msg = new Message();
                            msg.what=1;
                            msg.obj=wholeList.get(i);
                            addHandler.sendMessage(msg);
                        }
                        else {
                            Message msg = new Message();
                            msg.what=2;
                            msg.obj=wholeList.get(i);
                            addHandler.sendMessage(msg);
                            wholeList.get(i).setReadFlag("Read");
                            mapper.save(wholeList.get(i));
                        }

                    }
                    }

                    else{
                        Message msg = new Message();
                        msg.what=4;
                        Log.d("shiiiiiiiiiiiit",String.valueOf(sendList.size()));
                        addHandler.sendMessage(msg);
                    }

            while (true) {
//                int sendTemp,recTemp;
//                try {
//                    Thread.sleep(1000);
//                    sendExpression = new DynamoDBQueryExpression().withIndexName("FindSender").withConsistentRead(false).withHashKeyValues(sender);
//                    recExpression = new DynamoDBQueryExpression().withIndexName("FindReceiver").withConsistentRead(false).withHashKeyValues(receiver);
//                    List<ChattingTableDO> sendListTemp = mapper.query(ChattingTableDO.class,sendExpression);
//                    List<ChattingTableDO> recListTemp = mapper.query(ChattingTableDO.class,recExpression);
//                    sendTemp = sendListTemp.size();
//                    recTemp = recListTemp.size();
//                    Log.d("send,sendtemp,rec,rectemp ",String.valueOf(sendSize)+","+String.valueOf(sendTemp)+","+String.valueOf(recSize)+","+String.valueOf(recTemp));
//                    int total = sendTemp + recTemp;
//                    if(total>sendSize+recSize) {
//                        Log.d("send,sendtemp,rec,rectemp11",String.valueOf(sendSize)+","+String.valueOf(sendTemp)+","+String.valueOf(recSize)+","+String.valueOf(recTemp));
//                        List<ChattingTableDO> wholeListTemp = new ArrayList<>();
//                        wholeListTemp.addAll(sendListTemp);
//                        wholeListTemp.addAll(recListTemp);
//                        Collections.sort(wholeListTemp, new Comparator<ChattingTableDO>() {
//                            @Override
//                            public int compare(ChattingTableDO o1, ChattingTableDO o2) {
//                                return o1.getChatId() > o2.getChatId() ? 1 : o1.getChatId() == o2.getChatId() ? 0 : -1;
//                            }
//                        });
//                        for(int i=sendSize+recSize;i<wholeListTemp.size();i++){
//                          //  Log.d("sendername ",wholeList.get(i).getSender());
//                            if(wholeListTemp.get(i).getSender().equals(myUsr)){
//                                Message msgTemp = new Message();
//                                msgTemp.what=5;
//                                msgTemp.obj=wholeListTemp.get(i);
//                                addHandler.sendMessage(msgTemp);
//                            }
//                            else {
//                                Message msgTemp = new Message();
//                                msgTemp.what=6;
//                                msgTemp.obj=wholeListTemp.get(i);
//                                addHandler.sendMessage(msgTemp);
//                            }
//
//                        }
//                        sendSize=sendTemp;
//                        recSize = recTemp;
//
//
//                    }
//                    else{
//
//                    }
//                } catch (Exception e) {
//                    Log.d("error ", e.toString());
//
//                }
            }

        }
    };


}
