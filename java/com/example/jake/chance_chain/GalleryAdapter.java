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

public class GalleryAdapter extends
        RecyclerView.Adapter<GalleryAdapter.ViewHolder>
{

    private LayoutInflater mInflater;
    private List<String> mDatasImage;
    private List<String> mDatasText;
    private List<String> URIlink;

    public GalleryAdapter(Context context, List<String> datatImage,List<String> dataText, List<String> urilink)
    {
        this.mInflater = LayoutInflater.from(context);
        this.mDatasImage = datatImage;
        this.mDatasText = dataText;
        this.URIlink=urilink;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public ViewHolder(View arg0)
        {
            super(arg0);
        }

        ImageView mImg,uImg;
        TextView mTxt;
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
        return viewHolder;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i)
    {
        //viewHolder.mImg.setImageResource(mDatas.get(i));
        Log.d("trystr",""+mDatasImage.get(i));
        Picasso.get().load(mDatasImage.get(i)).into(viewHolder.mImg);
        viewHolder.mTxt.setText(mDatasText.get(i));
        Picasso.get().load(URIlink.get(i)).into(viewHolder.uImg);

    }

}
