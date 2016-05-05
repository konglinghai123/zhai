package com.dawnlightning.zhai.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dawnlightning.zhai.R;
import com.dawnlightning.zhai.bean.PicturesBean;
import com.dawnlightning.zhai.utils.Options;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by Administrator on 2016/5/4.
 */
public class PictureDetailedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private Context context;
    private List<PicturesBean> data;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;
    public PictureDetailedAdapter(Context context, List<PicturesBean> data) {
        this.context = context;
        this.data = data;
        options = Options.getListOptions();
    }
    public void setList( List<PicturesBean> data){
        this.data=data;
    }

    @Override
    public void onBindViewHolder(final  RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ItemViewHolder) {
            if ( ((ItemViewHolder) holder).photoView.getTag()!=null
                    &&((ItemViewHolder) holder).photoView.getTag().equals(data.get(position).getSrc())
                    ){
                //如何是相同URL就不重新加载，防止闪烁
                Log.e("相同","------------------------>");
            }else{
                imageLoader.loadImage(data.get(position).getSrc(), options, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String s, View view) {
                        ((ItemViewHolder) holder).photoView.setImageResource(R.mipmap.ic_dafult_pic);
                    }

                    @Override
                    public void onLoadingFailed(String s, View view, FailReason failReason) {

                    }

                    @Override
                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                        ((ItemViewHolder) holder).photoView.setMinimumWidth(bitmap.getWidth());
                        ((ItemViewHolder) holder).photoView.setMinimumHeight(bitmap.getHeight());
                        ((ItemViewHolder) holder).photoView.setImageBitmap(bitmap);
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


        PhotoView photoView;
        public ItemViewHolder(View view) {
            super(view);
           photoView=(PhotoView)view.findViewById(R.id.pv_imagedetailed);

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
