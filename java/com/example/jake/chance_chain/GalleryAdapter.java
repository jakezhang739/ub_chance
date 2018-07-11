package com.example.jake.chance_chain;
import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
    private List<String> mDatasImage;
    private List<String> mDatasText;
    private List<String> URIlink;
    private List<String> userId;
    private int offset;

    public GalleryAdapter(Context context, List<String> datatImage,List<String> dataText, List<String> urilink,List<String> uid,int of)
    {
        this.mInflater = LayoutInflater.from(context);
        this.mDatasImage = datatImage;
        this.mDatasText = dataText;
        this.URIlink=urilink;
        this.userId=uid;
        this.offset = of;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public ViewHolder(View arg0)
        {
            super(arg0);
        }

        ImageView mImg,uImg;
        TextView mTxt,uidTxt;
    }

    @Override
    public int getItemCount()
    {
        return mDatasImage.size();
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
        return viewHolder;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i)
    {
        //viewHolder.mImg.setImageResource(mDatas.get(i));
        int c= mDatasImage.size()+offset-i;
        Log.d("gallery adapter","v "+c+" i"+i+" off "+offset);
        Log.d("trystr",""+mDatasImage.get(c));
        Picasso.get().load(mDatasImage.get(c)).into(viewHolder.mImg);
        viewHolder.mTxt.setText(mDatasText.get(c));
        Picasso.get().load(URIlink.get(c)).into(viewHolder.uImg);
        viewHolder.uidTxt.setText(userId.get(c));

    }

    public void setOffset(int of){
        this.offset=of;
    }

}
