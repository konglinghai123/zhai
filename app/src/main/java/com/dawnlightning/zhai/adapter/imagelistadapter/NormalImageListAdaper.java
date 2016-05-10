package com.dawnlightning.zhai.adapter.imagelistadapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.dawnlightning.zhai.activity.ViewImagesActivity;
import com.dawnlightning.zhai.bean.ImageListBean;

import java.util.List;

/**
 * Created by Administrator on 2016/5/9.
 */
public class NormalImageListAdaper extends BaseImageListAdapter<ImageListBean> {
    public NormalImageListAdaper(Context context){
        super(context);
    }
    @Override
    public int headinsert(List<ImageListBean> list) {
        int count=0;
        if(this.data.size()>0){
            for (ImageListBean galleryBean:list){
                for (int i=0;i<this.data.size();i++){
                    if(galleryBean.getTerm().equals(((ImageListBean)this.data.get(i)).getTerm())){
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
        return ((ImageListBean)data.get(position)).getName();
    }

    @Override
    public String setTextViewSize(int position) {
        return ((ImageListBean)data.get(position)).getName();
    }

    @Override
    public String showImage(int position) {
        return ((ImageListBean)data.get(position)).getImg();
    }

    @Override
    public void ImageOnClickListener(int position) {
        ImageListBean bean = (ImageListBean)data.get(position);
        Intent intent = new Intent();
        intent.setClass(context, ViewImagesActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Beautify", bean);
        bundle.putSerializable("type","Beautify");
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
