package com.dawnlightning.zhai.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.dawnlightning.zhai.R;
import com.dawnlightning.zhai.base.Classify;
import com.dawnlightning.zhai.utils.Options;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

/**
 * Created by Administrator on 2016/5/9.
 */
public abstract  class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    public Context context;
    public List<T> data;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;
    private FootViewHolder footViewHolder;
    public abstract  int headinsert(List<T> list);
    public abstract  String setTextViewTitle(int position);
    public abstract  String setTextViewSize(int position);
    public abstract  String showImage(int position);
    public abstract void ImageOnClickListener(int position);
    public BaseRecyclerViewAdapter(Context context) {
        this.context = context;

        options = Options.getListOptions();
    }
    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {

            return data.size() == 0 ? 0 : data.size() + 1;
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
            View view = LayoutInflater.from(context).inflate(R.layout.item_base, parent,
                    false);
            return new ItemViewHolder(view);
        } else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_foot, parent,
                    false);
            footViewHolder=new FootViewHolder(view);
            return footViewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder,final int position) {
        if (holder instanceof ItemViewHolder) {

                ((ItemViewHolder) holder).tv_image_title.setText(setTextViewTitle(position));
                ((ItemViewHolder) holder).tv_image_date.setText(setTextViewSize(position));
                ((ItemViewHolder) holder).iv_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ImageOnClickListener(position);
                    }
                });

                if (((ItemViewHolder) holder).iv_image.getTag()!=null&&((ItemViewHolder)((ItemViewHolder) holder)).iv_image.getTag().equals(showImage(position))){
                }else {
                    imageLoader.displayImage(showImage(position), ((ItemViewHolder) holder).iv_image, options, new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String s, View view) {

                        }

                        @Override
                        public void onLoadingFailed(String s, View view, FailReason failReason) {

                        }

                        @Override
                        public void onLoadingComplete(String s, View view, Bitmap bitmap) {

                            ((ItemViewHolder) holder).iv_image.setTag(showImage(position));

                        }

                        @Override
                        public void onLoadingCancelled(String s, View view) {

                        }
                    });
                }

                if (onItemClickListener != null) {

                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            int position = holder.getLayoutPosition();
                            onItemClickListener.onItemClick(holder.itemView, position);
                        }
                    });

                    holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            int position = holder.getLayoutPosition();
                            onItemClickListener.onItemLongClick(holder.itemView, position);
                            return false;
                        }
                    });

                }
            }else{

            }
        }
    public void showend(){
        footViewHolder.progressBar.setVisibility(View.GONE);
        footViewHolder.textView.setText("---end---");
        footViewHolder.textView.setGravity(Gravity.CENTER);
    }
    public void setList(List<T> list){
        this.data=list;
    }
    public void addAll(List<T> list){
        this.data.addAll(list);
    }
    public void clearList(){
        this.data.clear();
    }


    public T getitem(int position){
        return this.data.get(position);
    }
   static class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView tv_image_date;
        TextView tv_image_title;
        ImageView iv_image;
        public ItemViewHolder(View view) {
            super(view);
            tv_image_date = (TextView) view.findViewById(R.id.tv_image_size);
            tv_image_title = (TextView) view.findViewById(R.id.tv_image_title);
            iv_image=(ImageView)view.findViewById(R.id.iv_image);

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
    public void clearmomreycache(){
        imageLoader.clearMemoryCache();
    }
}
