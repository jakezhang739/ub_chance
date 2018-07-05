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

import java.util.List;

public class myThread extends Thread {
    BaseActivity baseActivity;
    HomeFragment homeFragment;
    Context context;
    DynamoDBMapper dynamoDBMapper;
    private RecyclerView mRecyclerView;
    private GalleryAdapter mAdapter;
    private List<String> mDatasText;
    private List<String> mDatasImage;
    private List<String> dTouImage;
    private volatile boolean running=true;
    private Fragment fg;
    private FragmentTransaction ft;
    public myThread(BaseActivity baseActivity, Context context, DynamoDBMapper dynamoDBMapper, RecyclerView mRecyclerView, GalleryAdapter mAdapter, List<String> mDatasText, List<String> mDatasImage, List<String> tImage, FragmentTransaction fragt, Fragment fragment) {
        this.baseActivity = baseActivity;
        this.context = context;
        this.dynamoDBMapper=dynamoDBMapper;
        this.mRecyclerView=mRecyclerView;
        this.mAdapter=mAdapter;
        this.mDatasImage=mDatasImage;
        this.mDatasText=mDatasText;
        this.dTouImage = tImage;
        this.ft = fragt;
        this.fg=fragment;
        //this.uId=uId;
    }

    public void run() {
        TotalChanceDO totalChanceDO = dynamoDBMapper.load(TotalChanceDO.class,"totalID");
        Log.d("dyna",""+totalChanceDO.getTotC());

        for(int i = 1;i<=Integer.parseInt(totalChanceDO.getTotC());i++){
            ChanceWithValueDO chanceWithValueDO = dynamoDBMapper.load(ChanceWithValueDO.class,String.valueOf(i));
            mDatasImage.add("https://s3.amazonaws.com/chance-userfiles-mobilehub-653619147/"+String.valueOf(i)+".png");
            mDatasText.add(chanceWithValueDO.getValue());
            Log.d("dyna","uid "+chanceWithValueDO.getUser());
            dTouImage.add("https://s3.amazonaws.com/chance-userfiles-mobilehub-653619147/"+chanceWithValueDO.getUser()+".png");

        }
        Log.d("thread", "fg"+"sdf11");
        Bundle bundle = new Bundle();
        bundle.putStringArray("image",mDatasImage.toArray(new String[mDatasImage.size()]));
        bundle.putStringArray("text",mDatasText.toArray(new String[mDatasText.size()]));
        bundle.putStringArray("tou",dTouImage.toArray(new String[dTouImage.size()]));

        Log.d("thread", "fg"+"sdf");


        baseActivity.setTry(mDatasImage,mDatasText,dTouImage,totalChanceDO.getTotC());

        baseActivity.setFragment(bundle,ft);
        Log.d("thread", "fg"+"sd123f");
        //设置布局管理器

        //设置适配器


    }

}
