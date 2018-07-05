package com.example.jake.chance_chain;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    List<String> mImage,mText,touUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);

        Log.d("frag","yoyoyoyoyooyoyo");

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        RefreshLayout refreshLayout = (RefreshLayout) view.findViewById(R.id.refreshLayout);
        refreshLayout.setRefreshHeader(new ClassicsHeader(getContext()).setSpinnerStyle(SpinnerStyle.FixedBehind));
        refreshLayout.setRefreshFooter(new ClassicsFooter(getContext()).setSpinnerStyle(SpinnerStyle.Scale));

        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.id_recyclerview_horizontal);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mImage=new ArrayList<String>(Arrays.asList(getArguments().getStringArray("image")));
        mText = new ArrayList<String>(Arrays.asList(getArguments().getStringArray("text")));
        touUri = new ArrayList<String>(Arrays.asList(getArguments().getStringArray("tou")));

        GalleryAdapter mAdapter = new GalleryAdapter(getContext(), mImage,mText,touUri);

        mRecyclerView.setAdapter(mAdapter);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000);//传入false表示刷新失败
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore(2000);
            }
        });

        return view;
    }

}
