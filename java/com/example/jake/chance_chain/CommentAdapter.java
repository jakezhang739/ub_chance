package com.example.jake.chance_chain;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class CommentAdapter extends
        RecyclerView.Adapter<CommentAdapter.ViewHolder>
{

    private LayoutInflater mInflater;
    private List<chanceClass> cList = new ArrayList<chanceClass>();
    private Context mContext;



    public CommentAdapter(Context context, List<chanceClass> cc)
    {
        this.mInflater = LayoutInflater.from(context);
        this.cList = cc;
        this.mContext = context;


    }




    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public ViewHolder(View arg0)
        {
            super(arg0);
        }

        ImageView uImg,tagView,moreContent;
        TextView mTxt,uidTxt,timeTxt,dianzhan,fenxiang,pingjia;
        GridView mGridview;


    }

    @Override
    public int getItemCount()
    {
        return cList.size();
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View view = mInflater.inflate(R.layout.item,
                viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);

        viewHolder.mTxt=(TextView) view.findViewById(R.id.neirongTxt);
        viewHolder.uImg=(ImageView) view.findViewById(R.id.touxiangImg);
        viewHolder.uidTxt=(TextView) view.findViewById(R.id.userNameText);
        viewHolder.timeTxt=(TextView) view.findViewById(R.id.timeview);
        viewHolder.tagView=(ImageView) view.findViewById(R.id.tagView);
        viewHolder.mGridview = (GridView) view.findViewById(R.id.gallery);
        viewHolder.moreContent = (ImageView) view.findViewById(R.id.gengduo);
        viewHolder.pingjia = (TextView) view.findViewById(R.id.liuyan);
        viewHolder.fenxiang = (TextView) view.findViewById(R.id.fenxiang);
        viewHolder.dianzhan = (TextView) view.findViewById(R.id.dianzhan);

        Log.d("gallery adapter","v "+cList.get(0).txtNeirong);


        return viewHolder;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i)
    {

        int c = cList.size()-1-i;
        viewHolder.mTxt.setText(cList.get(c).txtTitle);

        viewHolder.uidTxt.setText(cList.get(c).userid);
        String display = displayTime(String.valueOf((long)cList.get(c).uploadTime));
        viewHolder.timeTxt.setText(display);
        switch ((int)cList.get(c).tag){
            case 1:viewHolder.tagView.setImageResource(R.drawable.huodong);break;
            case 2:viewHolder.tagView.setImageResource(R.drawable.yuema);break;
            case 3:viewHolder.tagView.setImageResource(R.drawable.remwu); break;
            case 4:viewHolder.tagView.setImageResource(R.drawable.qita); break;
        }
        viewHolder.pingjia.setText(String.valueOf(cList.get(c).cNumber));
        if(!cList.get(c).touUri.isEmpty()){
            Picasso.get().load(cList.get(c).touUri).into(viewHolder.uImg);
        }
        if(cList.get(c).imageSet.size()!=0){
            viewHolder.mGridview.setAdapter(new ImageAdapter(mContext,cList.get(c).imageSet));
        }
        if(cList.get(c).liked.size()!=0){
            viewHolder.dianzhan.setText(String.valueOf(cList.get(c).liked.size()));
        }
        if(cList.get(c).shared != 0){
            viewHolder.fenxiang.setText(String.valueOf(cList.get(c).shared));
        }

        viewHolder.moreContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),ContentActivity.class);
                intent.putExtra("cc",cList.get(c));
                v.getContext().startActivity(intent);
            }
        });





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
