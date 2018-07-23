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
    private List<commentClass> comList = new ArrayList<>();
    private List<String> imgList = new ArrayList<>();
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
            for(int i = totChance-9;i<=totChance-9;i++){
                ChanceWithValueDO chanceWithValueDO = dynamoDBMapper.load(ChanceWithValueDO.class,String.valueOf(i));
                chanceClass cc = new chanceClass(chanceWithValueDO.getRewardType(),chanceWithValueDO.getUsername(),chanceWithValueDO.getTitle(),chanceWithValueDO.getText(),chanceWithValueDO.getId(),chanceWithValueDO.getBonus(),chanceWithValueDO.getReward(),chanceWithValueDO.getTag(),chanceWithValueDO.getTime());
                UserPoolDO userPoolDO = dynamoDBMapper.load(UserPoolDO.class,cc.userid);
                if(userPoolDO.getProfilePic()!=null){
                    cc.settImg(userPoolDO.getProfilePic());
                }
                if(chanceWithValueDO.getPictures()!=null){
                    cc.setPicture(chanceWithValueDO.getPictures());
                }
                if(chanceWithValueDO.getLiked()!=null){
                    cc.setLiked(chanceWithValueDO.getLiked());
                }
                if(chanceWithValueDO.getCommentNumber()!=null){
                    cc.setcNumber(chanceWithValueDO.getCommentNumber());
                }
                if(chanceWithValueDO.getCommentIdList()!=null){
                    cc.setCid(chanceWithValueDO.getCommentIdList());
                }
                cList.add(cc);
                Log.d("uid121","sd "+totChance);

            }
        }
        else{
            for(int i = 1;i<=totChance;i++){
                ChanceWithValueDO chanceWithValueDO = dynamoDBMapper.load(ChanceWithValueDO.class,String.valueOf(i));
                chanceClass cc = new chanceClass(chanceWithValueDO.getRewardType(),chanceWithValueDO.getUsername(),chanceWithValueDO.getTitle(),chanceWithValueDO.getText(),chanceWithValueDO.getId(),chanceWithValueDO.getBonus(),chanceWithValueDO.getReward(),chanceWithValueDO.getTag(),chanceWithValueDO.getTime());
                UserPoolDO userPoolDO = dynamoDBMapper.load(UserPoolDO.class,cc.userid);
                if(userPoolDO.getProfilePic()!=null){
                    cc.settImg(userPoolDO.getProfilePic());
                }
                if(chanceWithValueDO.getPictures()!=null){
                    cc.setPicture(chanceWithValueDO.getPictures());
                }
                if(chanceWithValueDO.getLiked()!=null){
                    cc.setLiked(chanceWithValueDO.getLiked());
                }
                if(chanceWithValueDO.getCommentNumber()!=null){
                    int cTotal = chanceWithValueDO.getCommentNumber().intValue();
                    cc.setcNumber(chanceWithValueDO.getCommentNumber());
                    for(int j =0;j<cTotal;j++){
                        CommentTableDO commentTableDO = dynamoDBMapper.load(CommentTableDO.class,chanceWithValueDO.getCommentIdList().get(j));
                        commentClass comC = new commentClass(commentTableDO.getCommentId(),commentTableDO.getUpTime(),commentTableDO.getChanceId(),commentTableDO.getCommentText(),commentTableDO.getUserId());
                        if(commentTableDO.getUserPic()!=null){
                            comC.setUpic(commentTableDO.getUserPic());
                        }
                        comList.add(comC);
                    }
                }
                if(chanceWithValueDO.getCommentIdList()!=null){
                    cc.setCid(chanceWithValueDO.getCommentIdList());
                }
                cList.add(cc);
                Log.d("uid121","sd "+totChance);

            }
        }
        Log.d("thread", "fg"+"sdf11");
        ;


        Log.d("thread", "fg"+"sdf");



        baseActivity.setFragment(cList,comList,ft);
        Log.d("thread", "fg"+"sd123f");
        //设置布局管理器

        //设置适配器


    }

}
