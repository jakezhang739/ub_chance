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
import android.util.ArraySet;
import android.util.Log;
import android.view.View;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class myThread extends Thread {
    BaseActivity baseActivity;
    DynamoDBMapper dynamoDBMapper;
    private List<chanceClass> cList = new ArrayList<chanceClass>();
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

        int totChance = helper.returnChanceeSize(dynamoDBMapper);

        Log.d("just try222", "come on "+helper.returnChanceeSize(dynamoDBMapper));

        if(totChance > 10){
            for(int i = totChance;i>=totChance-9;i--){
                ChanceWithValueDO chanceWithValueDO = dynamoDBMapper.load(ChanceWithValueDO.class,String.valueOf(i));
                Log.d("uid121","sd "+totChance);
                chanceClass cc = new chanceClass(chanceWithValueDO.getBonusType(),chanceWithValueDO.getRewardType(),chanceWithValueDO.getUsername(),chanceWithValueDO.getTitle(),chanceWithValueDO.getText(),chanceWithValueDO.getId(),chanceWithValueDO.getBonus(),chanceWithValueDO.getReward(),chanceWithValueDO.getTag(),chanceWithValueDO.getTime());
                UserPoolDO userPoolDO = dynamoDBMapper.load(UserPoolDO.class,cc.userid);
                if(userPoolDO.getProfilePic()!=null){
                    cc.settImg(userPoolDO.getProfilePic());
                }
                cList.add(cc);

            }
        }
        else{
            for(int i = totChance;i>=1;i--){
                ChanceWithValueDO chanceWithValueDO = dynamoDBMapper.load(ChanceWithValueDO.class,String.valueOf(i));
                chanceClass cc = new chanceClass(chanceWithValueDO.getBonusType(),chanceWithValueDO.getRewardType(),chanceWithValueDO.getUsername(),chanceWithValueDO.getTitle(),chanceWithValueDO.getText(),chanceWithValueDO.getId(),chanceWithValueDO.getBonus(),chanceWithValueDO.getReward(),chanceWithValueDO.getTag(),chanceWithValueDO.getTime());
                UserPoolDO userPoolDO = dynamoDBMapper.load(UserPoolDO.class,cc.userid);
                if(userPoolDO.getProfilePic()!=null){
                    cc.settImg(userPoolDO.getProfilePic());
                }
                cList.add(cc);
                Log.d("uid121","sd "+totChance);

            }
        }
        Log.d("thread", "fg"+"sdf11");
        ;


        Log.d("thread", "fg"+"sdf");



        baseActivity.setFragment(cList,ft);
        Log.d("thread", "fg"+"sd123f");
        //设置布局管理器

        //设置适配器


    }

}
