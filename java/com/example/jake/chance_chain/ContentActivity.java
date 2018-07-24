package com.example.jake.chance_chain;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
    TextView uName,uTime,nText,zhuanfaTxt,pingjiaTxt,likeTxt,zhuanNum,comNum,zhanNum;
    List<ImageView> imgList = new ArrayList<>();
    List<String> strList = new ArrayList<>();
    List<commentClass> comClass = new ArrayList<>();
    int shareNum ;
    int liuNum ;
    int likeNum ;
    AppHelper helper = new AppHelper();
    String liuText;
    Context context;
    String curUsername;
    RecyclerView mRecyclerView;
    CommentAdapter mAdapter;
    LinearLayoutManager linearLayoutManager;

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
        zhuanNum = (TextView) findViewById(R.id.zhuanfaNum);
        comNum = (TextView) findViewById(R.id.pingNum);
        zhanNum = (TextView) findViewById(R.id.zanNUm);
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
        zhuanNum.setText(String.valueOf(shareNum));
        comNum.setText(String.valueOf(liuNum));
        zhanNum.setText(String.valueOf(likeNum));


        LinearLayout fLay = (LinearLayout) findViewById(R.id.firstBar);
        LinearLayout sLay = (LinearLayout) findViewById(R.id.secondBar);
        LinearLayout tLay = (LinearLayout) findViewById(R.id.thirdBar);
        LinearLayout comLayout = (LinearLayout) findViewById(R.id.liulay);

        mRecyclerView = (RecyclerView) findViewById(R.id.cRecycle);
        linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter= new CommentAdapter(context,chanceC.commentList);
        mRecyclerView.setAdapter(mAdapter);


        fLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(shareThread).start();
                Intent intent = new Intent(ContentActivity.this,sharingActivity.class);
                startActivity(intent);
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
                    zhanNum.setText(String.valueOf(likeNum));

                }
                else{
                    likeImg.setBackgroundColor(getColor(R.color.yellow));
                    chanceC.addLiked(curUsername);
                    likeNum++;
                    likeTxt.setText(String.valueOf(likeNum));
                    zhanNum.setText(String.valueOf(likeNum));
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
            if(msg.what==1){
                zhuanfaTxt.setText(msg.obj.toString());
                zhanNum.setText(msg.obj.toString());
            }
            else if(msg.what==2){
                chanceClass cClass = (chanceClass) msg.getData().getParcelable("chance");
                mAdapter= new CommentAdapter(context,cClass.commentList);
                mRecyclerView.setAdapter(mAdapter);
                pingjiaTxt.setText(String.valueOf(cClass.commentList.size()));
                comNum.setText(String.valueOf(liuNum));

            }


        }

    };

    Runnable shareThread = new Runnable() {
        @Override
        public void run() {
            ChanceWithValueDO cc = dynamoDBMapper.load(ChanceWithValueDO.class,chanceC.cId);
            int cS;
            if(cc.getShared()!=null) {
                cS = cc.getShared().intValue()+1;
                cc.setShared((double) cS);
            }
            else {
                cS = 1;
                cc.setShared((double) cS);
            }
            Message msg = new Message();
            chanceC.shared=cS;
            msg.what=1;
            msg.obj=cS;
            handler.sendMessage(msg);

        }
    };

    Runnable liuThread = new Runnable() {
        @Override
        public void run() {
            ChanceWithValueDO cc = dynamoDBMapper.load(ChanceWithValueDO.class,chanceC.cId);
            UserPoolDO userPoolDO = dynamoDBMapper.load(UserPoolDO.class,curUsername);
            int totcc;
            if(cc.getCommentNumber()!=null) {
                totcc = cc.getCommentNumber().intValue();
                totcc+=1;
            }
            else {
                totcc=1;
            }
            cc.setCommentNumber((double) totcc);
            chanceC.setcNumber(totcc);
            Message msg = new Message();
            DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
            PaginatedList<CommentTableDO> result = dynamoDBMapper.scan(CommentTableDO.class,scanExpression);
            int comId = result.size()+1;
            final CommentTableDO commentTableDO = new CommentTableDO();
            commentTableDO.setCommentId(String.valueOf(comId));
            commentTableDO.setChanceId(chanceC.cId);
            commentTableDO.setCommentText(liuText);
            commentTableDO.setUserId(curUsername);
            Date currentTime = Calendar.getInstance().getTime();
            String dateString = DateFormat.format("yyyyMMddHHmmss", new Date(currentTime.getTime())).toString();
            commentTableDO.setUpTime(dateString);
            commentClass commentClass = new commentClass(String.valueOf(comId),dateString,chanceC.cId,liuText,curUsername);
            if(userPoolDO.getProfilePic()!=null){
                commentTableDO.setUserPic(userPoolDO.getProfilePic());
                commentClass.setUpic(userPoolDO.getProfilePic());
            }
            chanceC.addComList(commentClass);
            chanceC.addComId(String.valueOf(comId));
            if(cc.getCommentIdList()!=null) {
                cc.addCommentId(String.valueOf(comId));
            }
            else{
                List<String> commentId = new ArrayList<>();
                commentId.add(String.valueOf(comId));
                cc.setCommentIdList(commentId);
            }
            dynamoDBMapper.save(commentTableDO);
            dynamoDBMapper.save(cc);
            msg.what=2;
            Bundle bundle = new Bundle();
            bundle.putParcelable("chance",chanceC);
            msg.setData(bundle);
            handler.sendMessage(msg);

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
