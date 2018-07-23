package com.example.jake.chance_chain;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class HomeFragment extends Fragment {

    List<String> mImage,mText,touUri,usId;
    DynamoDBMapper dynamoDBMapper;
    private boolean refreshFlag = false;
    private boolean loadmoreFlag = false;
    private List<chanceClass> cList = new ArrayList<chanceClass>();
    private List<commentClass> comList = new ArrayList<>();
    int tC,temptC;
    RecyclerView mRecyclerView;
    GalleryAdapter mAdapter;
    LinearLayoutManager linearLayoutManager;
    int uploadOffset=-1;
    int tempOffset=-1;
    AppHelper helper = new AppHelper();
    static {
        ClassicsHeader.REFRESH_HEADER_PULLING = "下拉可以刷新";
        ClassicsHeader.REFRESH_HEADER_REFRESHING = "正在刷新...";
        ClassicsHeader.REFRESH_HEADER_LOADING = "正在加载...";
        ClassicsHeader.REFRESH_HEADER_RELEASE = "释放立即刷新";
        ClassicsHeader.REFRESH_HEADER_FINISH = "刷新完成";
        ClassicsHeader.REFRESH_HEADER_FAILED = "刷新失败";
        ClassicsHeader.REFRESH_HEADER_SECONDARY = "释放进入二楼";
        ClassicsFooter.REFRESH_FOOTER_PULLING = "上拉加载更多";
        ClassicsFooter.REFRESH_FOOTER_RELEASE = "释放立即加载";
        ClassicsFooter.REFRESH_FOOTER_REFRESHING = "正在刷新...";
        ClassicsFooter.REFRESH_FOOTER_LOADING = "正在加载...";
        ClassicsFooter.REFRESH_FOOTER_FINISH = "加载完成";
        ClassicsFooter.REFRESH_FOOTER_FAILED = "加载失败";
        ClassicsFooter.REFRESH_FOOTER_NOTHING = "没有更多数据了";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);

        Log.d("frag","yoyoyoyoyooyoyo");
        dynamoDBMapper = AppHelper.getMapper(getContext());



        View view = inflater.inflate(R.layout.fragment_home, container, false);
        RefreshLayout refreshLayout = (RefreshLayout) view.findViewById(R.id.refreshLayout);
        refreshLayout.setRefreshHeader(new ClassicsHeader(getContext()).setSpinnerStyle(SpinnerStyle.FixedBehind));
        refreshLayout.setRefreshFooter(new ClassicsFooter(getContext()).setSpinnerStyle(SpinnerStyle.Scale));




        mRecyclerView = (RecyclerView) view.findViewById(R.id.id_recyclerview_horizontal);
        //设置布局管理器
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);


//
//        Log.d("home12", "how many wtf do i need");
//
//
        mAdapter = new GalleryAdapter(getContext(), cList);

        mRecyclerView.setAdapter(mAdapter);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                Log.d("uplll "," "+uploadOffset);


                new Thread(pullDownRunnable).start();

                while(!refreshFlag){

                }
                refreshlayout.finishRefresh(refreshFlag);//传入false表示刷新失败
                refreshLayout.setNoMoreData(false);
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                new Thread(pullUpLoad).start();
                while(!loadmoreFlag){

                }
                if(tC<=10){

                    refreshLayout.finishLoadMore(500,loadmoreFlag,true);
                }

                refreshLayout.finishLoadMore(500,loadmoreFlag,false);
            }
        });

        return view;
    }



    public void setClass(List<chanceClass> cc, List<commentClass> coList){
        this.cList = cc;
        this.comList = coList;
    }

    Runnable pullDownRunnable = new Runnable() {
        @Override
        public void run() {


            int totChance = helper.returnChanceeSize(dynamoDBMapper);


            int curChance = Integer.parseInt(cList.get(cList.size()-1).cId)+1;

            Log.d("sss11t try222", "come on "+curChance);

            Collections.reverse(cList);
            if(totChance-curChance>=9){
                for(int i = curChance;i<=curChance+9;i++){
                    ChanceWithValueDO chanceWithValueDO = dynamoDBMapper.load(ChanceWithValueDO.class, String.valueOf(i));
                    chanceClass cc = new chanceClass(chanceWithValueDO.getRewardType(), chanceWithValueDO.getUsername(), chanceWithValueDO.getTitle(), chanceWithValueDO.getText(), chanceWithValueDO.getId(), chanceWithValueDO.getBonus(), chanceWithValueDO.getReward(), chanceWithValueDO.getTag(), chanceWithValueDO.getTime());
                    UserPoolDO userPoolDO = dynamoDBMapper.load(UserPoolDO.class, cc.userid);
                    if (userPoolDO.getProfilePic() != null) {
                        cc.settImg(userPoolDO.getProfilePic());
                    }
                    cList.add(cc);
                }

            }

            else {
                for (int i = curChance; i <= totChance; i++) {
                    ChanceWithValueDO chanceWithValueDO = dynamoDBMapper.load(ChanceWithValueDO.class, String.valueOf(i));
                    chanceClass cc = new chanceClass(chanceWithValueDO.getRewardType(), chanceWithValueDO.getUsername(), chanceWithValueDO.getTitle(), chanceWithValueDO.getText(), chanceWithValueDO.getId(), chanceWithValueDO.getBonus(), chanceWithValueDO.getReward(), chanceWithValueDO.getTag(), chanceWithValueDO.getTime());
                    UserPoolDO userPoolDO = dynamoDBMapper.load(UserPoolDO.class, cc.userid);
                    if (userPoolDO.getProfilePic() != null) {
                        cc.settImg(userPoolDO.getProfilePic());
                    }
                    cList.add(cc);
                }
            }
            Collections.reverse(cList);
            refreshFlag=true;



        }
    };

    Runnable pullUpLoad = new Runnable() {
        @Override
        public void run() {

            Log.d("just try222", "come on "+helper.returnChanceeSize(dynamoDBMapper));

            int curChance = Integer.parseInt(cList.get(0).cId)-1;

            Log.d("jus11t try222", "come on "+curChance);

            if(curChance>=9){
                for(int i=curChance;i>=curChance-9;i--){
                    ChanceWithValueDO chanceWithValueDO = dynamoDBMapper.load(ChanceWithValueDO.class,String.valueOf(i));
                    chanceClass cc = new chanceClass(chanceWithValueDO.getRewardType(),chanceWithValueDO.getUsername(),chanceWithValueDO.getTitle(),chanceWithValueDO.getText(),chanceWithValueDO.getId(),chanceWithValueDO.getBonus(),chanceWithValueDO.getReward(),chanceWithValueDO.getTag(),chanceWithValueDO.getTime());
                    UserPoolDO userPoolDO = dynamoDBMapper.load(UserPoolDO.class,cc.userid);
                    if(userPoolDO.getProfilePic()!=null){
                        cc.settImg(userPoolDO.getProfilePic());
                    }
                    cList.add(cc);
                }
            }

            for(int i = curChance;i>=1;i--){
                ChanceWithValueDO chanceWithValueDO = dynamoDBMapper.load(ChanceWithValueDO.class,String.valueOf(i));
                chanceClass cc = new chanceClass(chanceWithValueDO.getRewardType(),chanceWithValueDO.getUsername(),chanceWithValueDO.getTitle(),chanceWithValueDO.getText(),chanceWithValueDO.getId(),chanceWithValueDO.getBonus(),chanceWithValueDO.getReward(),chanceWithValueDO.getTag(),chanceWithValueDO.getTime());
                UserPoolDO userPoolDO = dynamoDBMapper.load(UserPoolDO.class,cc.userid);
                if(userPoolDO.getProfilePic()!=null){
                    cc.settImg(userPoolDO.getProfilePic());
                }
                cList.add(cc);
            }
            loadmoreFlag=true;


        }
    };

}
