package com.example.jake.chance_chain;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;

import java.util.ArrayList;
import java.util.List;

public class myThread extends Thread {
    BaseActivity baseActivity;
    DynamoDBMapper dynamoDBMapper;
    private List<String> mDatasText;
    private List<String> mDatasImage;
    private List<String> dTouImage;
    private List<String> uid;
    private volatile boolean running=true;
    private HomeFragment fg;
    private FragmentTransaction ft;
    AppHelper helper = new AppHelper();
    public myThread(BaseActivity baseActivity, DynamoDBMapper dynamoDBMapper, FragmentTransaction fragt, HomeFragment fragment) {
        this.baseActivity = baseActivity;
        this.dynamoDBMapper=dynamoDBMapper;
        this.ft = fragt;
        this.fg=fragment;
    }

    public void run() {

        Log.d("just try222", "come on "+helper.returnChanceeSize(dynamoDBMapper));
//        TotalChanceDO totalChanceDO = dynamoDBMapper.load(TotalChanceDO.class,"totalID");
//        Log.d("dyna",""+totalChanceDO.getTotC());
//        mDatasImage = new ArrayList<String>();
//        mDatasText = new ArrayList<String>();
//        dTouImage = new ArrayList<String>();
//        uid = new ArrayList<String>();
//
//        int totChance = Integer.parseInt(totalChanceDO.getTotC());
//        if(totChance > 10){
//            for(int i = totChance-9;i<=totChance;i++){
//                ChanceWithValueDO chanceWithValueDO = dynamoDBMapper.load(ChanceWithValueDO.class,String.valueOf(i));
//                mDatasImage.add("https://s3.amazonaws.com/chance-userfiles-mobilehub-653619147/"+String.valueOf(i)+".png");
//                mDatasText.add(chanceWithValueDO.getValue());
//                Log.d("dyna222","uid "+String.valueOf(i));
//                dTouImage.add("https://s3.amazonaws.com/chance-userfiles-mobilehub-653619147/"+chanceWithValueDO.getUser()+".png");
//                uid.add(chanceWithValueDO.getUser());
//                Log.d("uid121","sd "+uid.get(0));
//
//            }
//        }
//        else{
//            for(int i = 1;i<=totChance;i++){
//                ChanceWithValueDO chanceWithValueDO = dynamoDBMapper.load(ChanceWithValueDO.class,String.valueOf(i));
//                mDatasImage.add("https://s3.amazonaws.com/chance-userfiles-mobilehub-653619147/"+String.valueOf(i)+".png");
//                mDatasText.add(chanceWithValueDO.getValue());
//                Log.d("dyna","uid "+chanceWithValueDO.getUser());
//                dTouImage.add("https://s3.amazonaws.com/chance-userfiles-mobilehub-653619147/"+chanceWithValueDO.getUser()+".png");
//                uid.add(chanceWithValueDO.getUser());
//
//            }
//        }
//        Log.d("thread", "fg"+"sdf11");
//        Bundle bundle = new Bundle();
//        bundle.putStringArray("image",mDatasImage.toArray(new String[mDatasImage.size()]));
//        bundle.putStringArray("text",mDatasText.toArray(new String[mDatasText.size()]));
//        bundle.putStringArray("tou",dTouImage.toArray(new String[dTouImage.size()]));
//        bundle.putStringArray("uid",uid.toArray(new String[uid.size()]));
//        bundle.putInt("totchance",totChance);
//
//        Log.d("thread", "fg"+"sdf");
//
//
//        baseActivity.setTry(mDatasImage,mDatasText,dTouImage,totalChanceDO.getTotC());
//
//        baseActivity.setFragment(bundle,ft);
//        Log.d("thread", "fg"+"sd123f");
        //设置布局管理器

        //设置适配器


    }

}
