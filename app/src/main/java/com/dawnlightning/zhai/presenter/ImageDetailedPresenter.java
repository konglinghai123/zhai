package com.dawnlightning.zhai.presenter;

import android.content.Context;

import com.dawnlightning.zhai.bean.ImageDetailedBean;
import com.dawnlightning.zhai.model.ImageDetailedModel;
import com.dawnlightning.zhai.model.ImageListModel;
import com.dawnlightning.zhai.view.IViewImageDetailedView;

import java.util.List;

/**
 * Created by Administrator on 2016/5/4.
 */
public class ImageDetailedPresenter implements ImageDetailedModel.ImageDetailedListener{
    private ImageDetailedModel model;
    private IViewImageDetailedView view;
    private Context context;
    public ImageDetailedPresenter(IViewImageDetailedView view,Context context){
        this.view=view;
        this.context=context;
        model=new ImageDetailedModel();
    }
    public void loadBeauify(String url){

        model.jsoupGetImageDetailed(url,this);//官网
    }
    public void loadmeitu(String url){

        model.umeiGetImageDetailed(url, this);
    }
    public void loadbeilaqi(String url){
        model.getBeilaQiImageDetailed(url,this);
    }
    public void loadImageDetailed(String url){
       model.viewImageDetail(url,this);
    }

    @Override
    public void getSuccess(List<ImageDetailedBean> list) {
        view.showPictures(list);
    }

    @Override
    public void getFailure(int code, String msg) {
        view.showError(code,msg);
    }
}
