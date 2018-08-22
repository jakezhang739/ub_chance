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
import android.text.Layout;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.http.HttpMethodName;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBQueryExpression;
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
    TextView uName,uTime,nText,zhuanNum,comNum,zhanNum,shouTxt,fuTxt,renshu;
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
    String shoufei,fufei,stype,ftype;
    RecyclerView mRecyclerView;
    CommentAdapter mAdapter;
    LinearLayoutManager linearLayoutManager;
    Button getButton;
    PopupWindow popupWindow;
    View rootview;
    TextView showText;

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
        shoufei = String.valueOf(chanceC.shoufei);
        fufei = String.valueOf(chanceC.fufei);
        stype = chanceC.sType;
        ftype = chanceC.fType;
        shouTxt = (TextView) findViewById(R.id.shoufei);
        fuTxt = (TextView) findViewById(R.id.fufeiTxt);
        renshu = (TextView) findViewById(R.id.renshu);
        if(!shoufei.equals("0")) {
            shouTxt.setText("收费金额： " + shoufei + stype);
        }
        if(!fufei.equals("0")) {
            fuTxt.setText("付费金额： " + fufei + ftype);
        }
        int rNum = (int) chanceC.renshu;
        renshu.setText("还剩"+String.valueOf(rNum)+"人能获得该机会");
        touImg = (ImageView) findViewById(R.id.contentTou);
        uName = (TextView) findViewById(R.id.contentUid);
        uTime = (TextView) findViewById(R.id.contentTime);
        nText = (TextView) findViewById(R.id.contentNei);
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
        Log.d("uptime",String.valueOf(chanceC.uploadTime));

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
                intent.putExtra("link",chanceC);
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
                    zhanNum.setText(String.valueOf(likeNum));

                }
                else{
                    likeImg.setBackgroundColor(getColor(R.color.yellow));
                    chanceC.addLiked(curUsername);
                    likeNum++;
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
                chanceC.cNumber++;
                comNum.setText(String.valueOf(chanceC.cNumber));
                new Thread(liuThread).start();
                comLayout.setVisibility(View.INVISIBLE);
            }
        });

        getButton = (Button) findViewById(R.id.getBtn);
        popupWindow = new PopupWindow(this);
        popupWindow.setWidth(RelativeLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(LayoutInflater.from(this).inflate(R.layout.popupwindow,null));
        rootview = LayoutInflater.from(ContentActivity.this).inflate(R.layout.activity_content, null);
        showText = (TextView) findViewById(R.id.huodeJihui);

        if(chanceC.gottenId.contains(curUsername)){
            getButton.setVisibility(View.INVISIBLE);
            showText.setVisibility(View.VISIBLE);
        }
        else if(chanceC.renshu<1){
            getButton.setVisibility(View.INVISIBLE);
            showText.setText("该机会已达到获得人数上限");
            showText.setVisibility(View.VISIBLE);
        }
        else {
            getButton.setVisibility(View.VISIBLE);
            showText.setVisibility(View.INVISIBLE);
        }

        if(chanceC.userid.equals(curUsername)){
            getButton.setVisibility(View.INVISIBLE);
        }


        getButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(getRunnable).start();

            }
        });
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);







    }

    public void popupdismiss(View v){
        popupWindow.dismiss();
    }

    Runnable getRunnable = new Runnable() {
        @Override
        public void run() {
            ChanceWithValueDO chanceWithValueDO = dynamoDBMapper.load(ChanceWithValueDO.class, chanceC.cId);
            UserPoolDO userPoolDO = dynamoDBMapper.load(UserPoolDO.class, curUsername);
            Log.d("getrun",shoufei);
            if (userPoolDO.getAvailableWallet() >= Double.parseDouble(shoufei)) {
                if(chanceWithValueDO.getRenShu()<1){
                    Message msg = new Message();
                    msg.what=4;
                    handler.sendMessage(msg);
                }
                else {
                    userPoolDO.setFrozenwallet(userPoolDO.getFrozenwallet() + Double.parseDouble(shoufei));
                    userPoolDO.setAvailableWallet(userPoolDO.getAvailableWallet() - Double.parseDouble(shoufei));
                    List<String> cGetList, uGetList;
                    if (chanceWithValueDO.getGetList() != null) {
                        cGetList = chanceWithValueDO.getGetList();
                    } else {
                        cGetList = new ArrayList<>();
                    }
                    if (userPoolDO.getGottenList() != null) {
                        uGetList = userPoolDO.getGottenList();
                    } else {
                        uGetList = new ArrayList<>();
                    }
                    cGetList.add(curUsername);
                    uGetList.add(chanceC.cId);
                    chanceWithValueDO.setGetList(cGetList);
                    chanceWithValueDO.setRenShu(chanceWithValueDO.getRenShu()-1);

                    userPoolDO.setGottenList(uGetList);
                    //userPoolDO.addGotten(chanceC.cId);
                    dynamoDBMapper.save(userPoolDO);
                    dynamoDBMapper.save(chanceWithValueDO);
                    Message msg = new Message();
                    msg.what = 3;
                    handler.sendMessage(msg);
                }
            }
            else {
                Log.d("getrun11",shoufei);
                Message msg1 = new Message();
                msg1.what=5;
                handler.sendMessage(msg1);

            }
        }
    };



    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Log.d("handler",String.valueOf(msg.what));

            if(msg.what==1){
                zhanNum.setText(msg.obj.toString());
            }
            else if(msg.what==2){
                chanceClass cClass = (chanceClass) msg.getData().getParcelable("chance");
                mAdapter= new CommentAdapter(context,cClass.commentList);
                mRecyclerView.setAdapter(mAdapter);
                comNum.setText(String.valueOf(liuNum));

            }
            else if(msg.what==3){
                popupWindow.showAtLocation(rootview, Gravity.CENTER_VERTICAL,0,0);
                getButton.setVisibility(View.INVISIBLE);
                showText.setVisibility(View.VISIBLE);
            }
            else if(msg.what==4){
                Toast.makeText(getApplicationContext().getApplicationContext(),"该机会已被抢光了！！！",Toast.LENGTH_LONG).show();

            }
            else if(msg.what==5){
                Log.d("handler1",String.valueOf(msg.what));
                Toast.makeText(getApplicationContext().getApplicationContext(),"可用资产不足获得该机会",Toast.LENGTH_LONG).show();

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
            dynamoDBMapper.save(cc);

        }
    };

    Runnable liuThread = new Runnable() {
        @Override
        public void run() {
            ChanceWithValueDO cc = dynamoDBMapper.load(ChanceWithValueDO.class,chanceC.cId);
            UserPoolDO userPoolDO = dynamoDBMapper.load(UserPoolDO.class,curUsername);
            int totcc;
            if(cc.getCommentIdList()!=null) {
                totcc = cc.getCommentIdList().size();
                totcc+=1;
            }
            else {
                totcc=1;
            }
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
                List<String> tempList = cc.getCommentIdList();
                tempList.add(String.valueOf(comId));
                cc.setCommentIdList(tempList);
            }
            else{
                List<String> commentId = new ArrayList<>();
                commentId.add(String.valueOf(comId));
                cc.setCommentIdList(commentId);
            }
            Log.d("show comid ",String.valueOf(commentTableDO.getCommentId())+" "+String.valueOf(cc.getCommentIdList().size()));
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
                    List<String> like = cc.getLiked();
                    like.remove(curUsername);
                }
                else{
                    List<String> like = cc.getLiked();
                    like.add(curUsername);
                    cc.setLiked(like);
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

    public void gotomyChance(View v){
        Intent intent = new Intent(v.getContext(),wodejihui.class);
        startActivity(intent);
    }

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
