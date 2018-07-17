package com.example.jake.chance_chain;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class GalleryAdapter extends
        RecyclerView.Adapter<GalleryAdapter.ViewHolder>
{

    private LayoutInflater mInflater;
    private List<chanceClass> cList = new ArrayList<chanceClass>();

    public GalleryAdapter(Context context, List<chanceClass> cc)
    {
        this.mInflater = LayoutInflater.from(context);
        this.cList = cc;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public ViewHolder(View arg0)
        {
            super(arg0);
        }

        ImageView mImg,uImg,tagView;
        TextView mTxt,uidTxt,timeTxt;
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

        viewHolder.mImg = (ImageView) view
                .findViewById(R.id.neirongImg);
        viewHolder.mTxt=(TextView) view.findViewById(R.id.neirongTxt);
        viewHolder.uImg=(ImageView) view.findViewById(R.id.touxiangImg);
        viewHolder.uidTxt=(TextView) view.findViewById(R.id.userNameText);
        viewHolder.timeTxt=(TextView) view.findViewById(R.id.timeview);
        viewHolder.tagView=(ImageView) view.findViewById(R.id.tagView);
        return viewHolder;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i)
    {
        //viewHolder.mImg.setImageResource(mDatas.get(i));
        Log.d("gallery adapter","v ");
        //Picasso.get().load(cList.get(i)).into(viewHolder.mImg);
        viewHolder.mTxt.setText(cList.get(i).txtNeirong);

        viewHolder.uidTxt.setText(cList.get(i).userid);
        String display = displayTime(String.valueOf((long)cList.get(i).uploadTime));
        viewHolder.timeTxt.setText(display);
        switch ((int)cList.get(i).tag){
            case 1:viewHolder.tagView.setImageResource(R.drawable.yuema);break;
            case 2:viewHolder.tagView.setImageResource(R.drawable.huodong);break;
            case 3:viewHolder.tagView.setImageResource(R.drawable.remwu); break;
            case 4:viewHolder.tagView.setImageResource(R.drawable.qita); break;
        }
        if(!cList.get(i).touUri.equals("")){
            Picasso.get().load(cList.get(i).touUri).into(viewHolder.uImg);
        }

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
