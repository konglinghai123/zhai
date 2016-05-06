package com.dawnlightning.zhai.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dawnlightning.zhai.R;
import com.dawnlightning.zhai.activity.ViewImagesActivity;
import com.dawnlightning.zhai.base.Classify;
import com.dawnlightning.zhai.bean.BeautyLegListBean;
import com.dawnlightning.zhai.bean.GalleryBean;
import com.dawnlightning.zhai.utils.HttpConstants;
import com.dawnlightning.zhai.utils.Options;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;
public class RecyclerViewAdapter extends Adapter<ViewHolder> {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private Context context;
    private List<GalleryBean> data;
    private List<BeautyLegListBean> beautyLegListBeanList;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;
    private FootViewHolder footViewHolder;
    private Classify classify;
    public RecyclerViewAdapter(Context context,Classify classify) {
        this.context = context;
        this.classify=classify;
        options = Options.getListOptions();
    }
    public void setData(List<GalleryBean> list){
        this.data=list;
    }
    public void setBeautyLegListBeanList(List<BeautyLegListBean> list){
        this.beautyLegListBeanList=list;
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
        if (classify.equals(Classify.ApiGrils)){
            return data.size() == 0 ? 0 : data.size() + 1;
        }else if (classify.equals(classify.BeautyLeg)){
            return beautyLegListBeanList.size() == 0 ? 0 : beautyLegListBeanList.size() + 1;
        }
        return  0;
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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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
    public void addBeautyList(List<BeautyLegListBean> list){
        this.beautyLegListBeanList.addAll(list);
    }
    public void clearBeautifyList(){
        this.beautyLegListBeanList.clear();
    }
    public int headinsertBeautify(List<BeautyLegListBean> list){
        int count=0;
        if(this.beautyLegListBeanList.size()>0){
            for (BeautyLegListBean galleryBean:list){
                for (int i=0;i<this.beautyLegListBeanList.size();i++){
                    if(galleryBean.getTerm().equals(this.beautyLegListBeanList.get(i).getTerm())){
                        break;
                    }else if(i==this.beautyLegListBeanList.size()-1){
                        this.beautyLegListBeanList.add(0, galleryBean);
                        count++;
                    }else{
                        break;
                    }
                }
            }
        }else{
            setBeautyLegListBeanList(list);
            return  list.size();
        }
        return count;
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder,final int position) {
        if (holder instanceof ItemViewHolder) {
            if (classify.equals(Classify.ApiGrils)){
            ((ItemViewHolder) holder).tv_image_title.setText(data.get(position).getTitle());
            ((ItemViewHolder) holder).tv_image_date.setText(data.get(position).getSize() + "张");
                ((ItemViewHolder) holder).iv_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                            GalleryBean bean = data.get(position);
                            Intent intent = new Intent();
                            intent.setClass(context, ViewImagesActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("Gallery", bean);
                            bundle.putSerializable("type","ApiGrils");
                            intent.putExtras(bundle);
                            context.startActivity(intent);

                    }
                });

            if (((ItemViewHolder) holder).iv_image.getTag()!=null&&((ItemViewHolder)((ItemViewHolder) holder)).iv_image.getTag().equals(HttpConstants.ApiImageBaseUrl + data.get(position).getImg())){

            }else {
                imageLoader.displayImage(HttpConstants.ApiImageBaseUrl + data.get(position).getImg(), ((ItemViewHolder) holder).iv_image, options, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String s, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String s, View view, FailReason failReason) {

                    }

                    @Override
                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {

                        ((ItemViewHolder) holder).iv_image.setTag(HttpConstants.ApiImageBaseUrl + data.get(position).getImg());

                    }

                    @Override
                    public void onLoadingCancelled(String s, View view) {

                    }
                });
            }}
            else if(classify.equals(Classify.BeautyLeg)){
                ((ItemViewHolder) holder).tv_image_title.setText(beautyLegListBeanList.get(position).getName());
                ((ItemViewHolder) holder).tv_image_date.setText(beautyLegListBeanList.get(position).getTerm() + "期");
                ((ItemViewHolder) holder).iv_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BeautyLegListBean bean = beautyLegListBeanList.get(position);
                        Intent intent = new Intent();
                        intent.setClass(context, ViewImagesActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("Beautify", bean);
                        bundle.putSerializable("type","Beautify");
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                });

                if (((ItemViewHolder) holder).iv_image.getTag()!=null
                        &&((ItemViewHolder)((ItemViewHolder) holder)).iv_image.getTag().equals(beautyLegListBeanList.get(position).getImg())){

                }else {
                    imageLoader.displayImage(beautyLegListBeanList.get(position).getImg(), ((ItemViewHolder) holder).iv_image, options, new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String s, View view) {

                        }

                        @Override
                        public void onLoadingFailed(String s, View view, FailReason failReason) {

                        }

                        @Override
                        public void onLoadingComplete(String s, View view, Bitmap bitmap) {

                            ((ItemViewHolder) holder).iv_image.setTag(beautyLegListBeanList.get(position).getImg());

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
    }}
    public void showend(){
        footViewHolder.progressBar.setVisibility(View.GONE);
        footViewHolder.textView.setText("---end---");
        footViewHolder.textView.setGravity(Gravity.CENTER);
    }
    public void setList(List<GalleryBean> list){
        this.data=list;
    }
    public void addAll(List<GalleryBean> list){
        this.data.addAll(list);
    }
    public void clearList(){
        this.data.clear();
    }
    //刷新时向最开头插入不重复数据
    //count为增加的条目数
    public int headinsert(List<GalleryBean> list){
        int count=0;
        if(this.data.size()>0){
            for (GalleryBean galleryBean:list){
                for (int i=0;i<this.data.size();i++){
                    if(galleryBean.getTime().equals(this.data.get(i).getTime())){
                        break;
                    }else if(i==this.data.size()-1){
                        this.data.add(0, galleryBean);
                        count++;
                    }else{
                        break;
                    }
                }
            }
        }else{
            setList(list);
            return  list.size();
        }
        return count;
    }
    public GalleryBean getitem(int position){
        return this.data.get(position);
    }
    class ItemViewHolder extends ViewHolder {

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

    class FootViewHolder extends ViewHolder {
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