package com.example.jake.chance_chain;

import android.content.Context;
import android.media.Image;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amazonaws.http.HttpMethodName;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedList;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ContentActivity extends AppCompatActivity {
    private chanceClass chanceC;
    DynamoDBMapper dynamoDBMapper;
    ImageView touImg,likeImg;
    TextView uName,uTime,nText,zhuanfaTxt,pingjiaTxt,likeTxt;
    List<ImageView> imgList = new ArrayList<>();
    List<String> strList = new ArrayList<>();
    int shareNum ;
    int liuNum ;
    int likeNum ;
    AppHelper helper = new AppHelper();
    String liuText;
    Context context;
    String curUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        context = getApplication().getApplicationContext();
        curUsername = helper.getCurrentUserName(context);
        dynamoDBMapper=AppHelper.getMapper(context);
        LinearLayout imgLay = (LinearLayout) findViewById(R.id.imgLayout);
        chanceC = (chanceClass) getIntent().getParcelableExtra("cc");
        shareNum = chanceC.shared;
        liuNum = chanceC.cNumber;
        likeNum = chanceC.liked.size();
        touImg = (ImageView) findViewById(R.id.contentTou);
        uName = (TextView) findViewById(R.id.contentUid);
        uTime = (TextView) findViewById(R.id.contentTime);
        nText = (TextView) findViewById(R.id.contentNei);
        zhuanfaTxt = (TextView) findViewById(R.id.zhuanBar);
        pingjiaTxt = (TextView) findViewById(R.id.pinBar);
        likeTxt = (TextView) findViewById(R.id.zanBar);
        likeImg = (ImageView) findViewById(R.id.zanSpic);
        if(chanceC.liked.contains(curUsername)) {
            likeImg.setBackgroundColor(getColor(R.color.yellow));

        }
        else{
            likeImg.setBackgroundColor(getColor(R.color.colorPrimaryDark));
        }

        if(!chanceC.touUri.isEmpty()){
            Picasso.get().load(chanceC.touUri).resize(60,60).into(touImg);
        }
        uName.setText(chanceC.userid);

        uTime.setText(displayTime(String.valueOf((long) chanceC.uploadTime)));
        nText.setText(chanceC.txtNeirong);
        if(chanceC.imageSet.size()!=0){
            strList = chanceC.imageSet;
            for(int i = 0; i<strList.size();i++){
                ImageView neiImg =new ImageView(this);
                Picasso.get().load(strList.get(i)).into(neiImg);
                imgList.add(neiImg);
            }
        }
        for(int i =0;i<imgList.size();i++){
            imgLay.addView(imgList.get(i));
            TextView spaceImg = new TextView(this);
            spaceImg.setHeight(20);
            imgLay.addView(spaceImg);
        }

        ImageView dianPic = (ImageView) findViewById(R.id.zanSpic);

        zhuanfaTxt.setText(String.valueOf(shareNum));
        pingjiaTxt.setText(String.valueOf(liuNum));
        likeTxt.setText(String.valueOf(likeNum));


        LinearLayout fLay = (LinearLayout) findViewById(R.id.firstBar);
        LinearLayout sLay = (LinearLayout) findViewById(R.id.secondBar);
        LinearLayout tLay = (LinearLayout) findViewById(R.id.thirdBar);
        LinearLayout comLayout = (LinearLayout) findViewById(R.id.liulay);

        fLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        sLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (comLayout.getVisibility() == View.VISIBLE) {
                    comLayout.setVisibility(View.INVISIBLE);
                } else {
                    comLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        tLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chanceC.liked.contains(curUsername)) {
                    likeImg.setBackgroundColor(getColor(R.color.colorPrimaryDark));
                    chanceC.deleteLike(curUsername);
                    likeNum--;
                    likeTxt.setText(String.valueOf(likeNum));

                }
                else{
                    likeImg.setBackgroundColor(getColor(R.color.yellow));
                    chanceC.addLiked(curUsername);
                    likeNum++;
                    likeTxt.setText(String.valueOf(likeNum));
                }
                new Thread(likeThread).start();

            }
        });

        ImageView faLiu = (ImageView) findViewById(R.id.faliuyan);
        EditText shuruFa = (EditText) findViewById(R.id.pingText);

        faLiu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                liuText = shuruFa.getText().toString();
                new Thread(liuThread).start();
            }
        });

    }


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:zhuanfaTxt.setText(msg.obj.toString()); break;
                case 2:pingjiaTxt.setText(msg.obj.toString());break;
            }

        }

    };

    Runnable shareThread = new Runnable() {
        @Override
        public void run() {
            chanceClass cc = dynamoDBMapper.load(chanceClass.class,chanceC.cId);
            chanceC.shared ++;
            cc.setShare(chanceC.shared);
            Message msg = new Message();
            msg.what=1;
            msg.obj=chanceC.shared;
            handler.sendMessage(msg);

        }
    };

    Runnable liuThread = new Runnable() {
        @Override
        public void run() {
            chanceClass cc = dynamoDBMapper.load(chanceClass.class,chanceC.cId);
            chanceC.cNumber ++;
            cc.setcNumber(chanceC.cNumber);
            Message msg = new Message();
            msg.what=2;
            msg.obj=chanceC.cNumber;
            handler.sendMessage(msg);
            DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
            PaginatedList<CommentTableDO> result = dynamoDBMapper.scan(CommentTableDO.class,scanExpression);
            int comId = result.size()+1;
            final CommentTableDO commentTableDO = new CommentTableDO();
            commentTableDO.setCommentId(String.valueOf(comId));
            commentTableDO.setChanceId(chanceC.cId);
            commentTableDO.setCommentText(liuText);
            commentTableDO.setUserId(chanceC.userid);
            commentTableDO.setUpTime(String.valueOf(chanceC.uploadTime));
            if(!chanceC.touUri.isEmpty()){
                commentTableDO.setUserPic(chanceC.touUri);
            }

        }
    };

    Runnable likeThread = new Runnable() {
        @Override
        public void run() {
            ChanceWithValueDO cc = dynamoDBMapper.load(ChanceWithValueDO.class,chanceC.cId);
            if(cc.getLiked()!=null) {
                if (cc.getLiked().contains(curUsername)) {
                    cc.deleteLike(curUsername);
                }
                else{
                    cc.addLike(curUsername);
                }
            }
            else {
                 List<String> Liked = new ArrayList<>();
                 Liked.add(curUsername);
                 cc.setLiked(Liked);
            }

            dynamoDBMapper.save(cc);

        }
    };

    public String displayTime(String thatTime){
        Date currentTime = Calendar.getInstance().getTime();
        String dateString = DateFormat.format("yyyyMMddHHmmss", new Date(currentTime.getTime())).toString();
        int hr1,hr2,min1,min2;
        String sameday1,sameday2;
        sameday1=thatTime.substring(0,8);
        sameday2=dateString.substring(0,8);
        hr1=Integer.parseInt(thatTime.substring(8,10));
        hr2=Integer.parseInt(dateString.substring(8,10));
        min1=Integer.parseInt(thatTime.substring(10,12));
        min2=Integer.parseInt(dateString.substring(10,12));
        if(!sameday1.equals(sameday2)){
            Log.d("same ",sameday1 + " " + sameday2);
            return sameday1.substring(0,4)+"年"+sameday1.substring(4,6)+'月'+sameday1.substring(6,8)+"号";
        }
        else if(hr1!=hr2){
            Log.d("hr ",hr1+" "+hr2);
            return String.valueOf(hr2-hr1)+"小时前";
        }
        else if(min1!=min2){
            Log.d("min ", min1+" "+min2);
            return String.valueOf(min2-min1)+"分钟前";
        }
        else{
            return "刚刚";
        }



    }

}
