package com.dawnlightning.zhai.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dawnlightning.zhai.R;
import com.dawnlightning.zhai.activity.ViewPagerActivity;
import com.dawnlightning.zhai.bean.ImageDetailedBean;
import com.dawnlightning.zhai.utils.Options;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/4.
 */
public class PictureDetailedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private Context context;
    private List<ImageDetailedBean> data;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;
    public PictureDetailedAdapter(Context context, List<ImageDetailedBean> data) {
        this.context = context;
        this.data = data;
        options = Options.getListOptions();
    }
    public void setList( List<ImageDetailedBean> data){
        this.data=data;
    }
    public  List<ImageDetailedBean>skipdata(int postion){
        List<ImageDetailedBean> list=new ArrayList<>();
        for (int i=postion;i<postion+500;i++){
            if (i<data.size()){
                list.add(data.get(i));
            }
        }
        return list;
    }
    @Override
    public void onBindViewHolder(final  RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ItemViewHolder) {
            ((ItemViewHolder) holder).photoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent();
                    intent.setClass(context, ViewPagerActivity.class);
                    if (data.size()>1500){
                        intent.putExtra("list",(Serializable)skipdata(position));
                        intent.putExtra("position",0);
                    }else{
                        intent.putExtra("list",(Serializable)data);
                        intent.putExtra("position",position);
                    }

                    context.startActivity(intent);
                }
            });
            if ( ((ItemViewHolder) holder).photoView.getTag()!=null
                    &&((ItemViewHolder) holder).photoView.getTag().equals(data.get(position).getSrc())
                    ){
                //如何是相同URL就不重新加载，防止闪烁
                Log.e("相同","------------------------>");
            }else{
                imageLoader.displayImage(data.get(position).getSrc(), ((ItemViewHolder) holder).photoView, options, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String s, View view) {
                        ((ItemViewHolder) holder).photoView.setImageResource(R.mipmap.ic_dafult_pic);
                    }

                    @Override
                    public void onLoadingFailed(String s, View view, FailReason failReason) {

                    }

                    @Override
                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {
//                        ((ItemViewHolder) holder).photoView.setMinimumWidth(bitmap.getWidth());
//                        ((ItemViewHolder) holder).photoView.setMinimumHeight(bitmap.getHeight());
                        //       ((ItemViewHolder) holder).photoView.setImageBitmap(bitmap);
                        ((ItemViewHolder) holder).photoView.setTag(data.get(position).getSrc());
                    }

                    @Override
                    public void onLoadingCancelled(String s, View view) {

                    }
                });
            }
        }else{
            ((FootViewHolder)holder).progressBar.setVisibility(View.GONE);
            ((FootViewHolder)holder).textView.setText("---end---");
            ((FootViewHolder)holder).textView.setGravity(Gravity.CENTER);
        }
    }
    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_photoview, parent,
                    false);
            return new ItemViewHolder(view);
        } else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_foot, parent,
                    false);
            return new FootViewHolder(view);
        }
        return null;
    }
    class ItemViewHolder extends RecyclerView.ViewHolder {


        ImageView photoView;
        public ItemViewHolder(View view) {
            super(view);
           photoView=(ImageView)view.findViewById(R.id.pv_imagedetailed);

        }
    }

    class FootViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;
        TextView textView;
        public FootViewHolder(View view) {
            super(view);
            progressBar=(ProgressBar)view.findViewById(R.id.progressBar);
            textView=(TextView)view.findViewById(R.id.tv_recyclerview_foot);


        }
    }
    @Override
    public int getItemCount() {
        return data.size() == 0 ? 0 : data.size() + 1;
    }
    public void clearmemorycache(){
        imageLoader.clearMemoryCache();
    }
    public void cleardata(){
        this.data.clear();
    }
}
