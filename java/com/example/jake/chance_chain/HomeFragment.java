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
import java.util.List;


public class HomeFragment extends Fragment {

    List<String> mImage,mText,touUri,usId;
    DynamoDBMapper dynamoDBMapper;
    private boolean refreshFlag = false;
    private boolean loadmoreFlag = false;
    int tC,temptC;
    RecyclerView mRecyclerView;
    GalleryAdapter mAdapter;
    LinearLayoutManager linearLayoutManager;
    int uploadOffset=-1;
    int tempOffset=-1;
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
        mImage=new ArrayList<String>(Arrays.asList(getArguments().getStringArray("image")));
        mText = new ArrayList<String>(Arrays.asList(getArguments().getStringArray("text")));
        touUri = new ArrayList<String>(Arrays.asList(getArguments().getStringArray("tou")));
        usId=new ArrayList<String>(Arrays.asList(getArguments().getStringArray("uid")));
        tC = getArguments().getInt("totchance");
        temptC = tC;

        Log.d("home12", "how many wtf do i need");


        mAdapter = new GalleryAdapter(getContext(), mImage,mText,touUri,usId,uploadOffset);

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

    public void setData(List<String> txt,List<String> img, List<String> tou){
        this.mText=txt;
        this.mImage=img;
        this.touUri = tou;

    }

    Runnable pullDownRunnable = new Runnable() {
        @Override
        public void run() {



            TotalChanceDO totalChanceDO = dynamoDBMapper.load(TotalChanceDO.class,"totalID");
            Log.d("dyna12",""+totalChanceDO.getTotC());
            mImage.clear();
            mText.clear();
            touUri.clear();


            int totChance = Integer.parseInt(totalChanceDO.getTotC());
                if (totChance> 10) {

                    for (int i = totChance -9; i <=totChance; i++) {
                        ChanceWithValueDO chanceWithValueDO = dynamoDBMapper.load(ChanceWithValueDO.class, String.valueOf(i));
                        mImage.add("https://s3.amazonaws.com/chance-userfiles-mobilehub-653619147/" + String.valueOf(i) + ".png");
                        mText.add(chanceWithValueDO.getValue());
                        Log.d("letme try", "wtf " + String.valueOf(i));
                        touUri.add("https://s3.amazonaws.com/chance-userfiles-mobilehub-653619147/" + chanceWithValueDO.getUser() + ".png");
                        usId.add(chanceWithValueDO.getUser());
                    }
                } else {

                    for (int i = 1; i <= totChance; i++) {
                        ChanceWithValueDO chanceWithValueDO = dynamoDBMapper.load(ChanceWithValueDO.class, String.valueOf(i));
                        mImage.add("https://s3.amazonaws.com/chance-userfiles-mobilehub-653619147/" + String.valueOf(i) + ".png");
                        mText.add(chanceWithValueDO.getValue());
                        Log.d("letyou ty", "uid " + String.valueOf(i));
                        touUri.add("https://s3.amazonaws.com/chance-userfiles-mobilehub-653619147/" + chanceWithValueDO.getUser() + ".png");
                        usId.add(chanceWithValueDO.getUser());
                    }
                }




            refreshFlag=true;
                tC=totChance;

        }
    };

    Runnable pullUpLoad = new Runnable() {
        @Override
        public void run() {

            TotalChanceDO totalChanceDO = dynamoDBMapper.load(TotalChanceDO.class,"totalID");
            Log.d("dyna123223",""+totalChanceDO.getTotC()+" " + tC);
            int totChance = Integer.parseInt(totalChanceDO.getTotC());


            int index=0;
            if(tC>20){
                mImage.clear();
                mText.clear();
                touUri.clear();
                for(int i=tC-10;i<tC;i++){
                    ChanceWithValueDO chanceWithValueDO = dynamoDBMapper.load(ChanceWithValueDO.class, String.valueOf(i));
                    mImage.add(index,"https://s3.amazonaws.com/chance-userfiles-mobilehub-653619147/" + String.valueOf(i) + ".png");
                    mText.add(index,chanceWithValueDO.getValue());
                    Log.d("dyna123", "uid " + chanceWithValueDO.getValue());
                    touUri.add(index,"https://s3.amazonaws.com/chance-userfiles-mobilehub-653619147/" + chanceWithValueDO.getUser() + ".png");
                }
            }
            else if(tC > 10){
                mImage.clear();
                mText.clear();
                touUri.clear();
                for(int i=1;i<=tC-10;i++){
                    ChanceWithValueDO chanceWithValueDO = dynamoDBMapper.load(ChanceWithValueDO.class, String.valueOf(i));
                    mImage.add(index,"https://s3.amazonaws.com/chance-userfiles-mobilehub-653619147/" + String.valueOf(i) + ".png");
                    mText.add(index,chanceWithValueDO.getValue());
                    Log.d("dyna123123", "uid " + String.valueOf(i));
                    touUri.add(index,"https://s3.amazonaws.com/chance-userfiles-mobilehub-653619147/" + chanceWithValueDO.getUser() + ".png");
                }

            }




            uploadOffset=mImage.size()-1;


            loadmoreFlag = true;
            tC=tC-10;

        }
    };

}
