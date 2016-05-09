package com.dawnlightning.zhai.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.dawnlightning.zhai.activity.ViewImagesActivity;
import com.dawnlightning.zhai.base.Classify;
import com.dawnlightning.zhai.bean.GalleryBean;
import com.dawnlightning.zhai.utils.HttpConstants;

import java.util.List;

/**
 * Created by Administrator on 2016/5/9.
 */
public class TiangouRecyclerViewAdapter extends BaseRecyclerViewAdapter<GalleryBean>{

    public TiangouRecyclerViewAdapter(Context context) {
        super(context);

    }

    @Override
    public int headinsert(List<GalleryBean> list) {
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

    @Override
    public String setTextViewTitle(int position) {
        return data.get(position).getTitle();
    }

    @Override
    public String setTextViewSize(int position) {
        return data.get(position).getSize() + "张";
    }

    @Override
    public String showImage(int position) {
        return HttpConstants.ApiImageBaseUrl + data.get(position).getImg();
    }

    @Override
    public void ImageOnClickListener(int position) {
        GalleryBean bean = data.get(position);
        Intent intent = new Intent();
        intent.setClass(context, ViewImagesActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Gallery", bean);
        bundle.putSerializable("type","ApiGrils");
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
