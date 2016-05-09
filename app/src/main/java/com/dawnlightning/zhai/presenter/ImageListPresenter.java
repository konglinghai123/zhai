package com.dawnlightning.zhai.presenter;

import android.content.Context;

import com.dawnlightning.zhai.base.Actions;
import com.dawnlightning.zhai.bean.BeautyLegListBean;
import com.dawnlightning.zhai.bean.GalleryBean;
import com.dawnlightning.zhai.model.ImageListModel;
import com.dawnlightning.zhai.view.IBaseFragmentView;

import java.util.List;

/**
 * Created by Administrator on 2016/5/4.
 */
public class ImageListPresenter implements ImageListModel.ImageListLisenter {
    private ImageListModel model;
    private IBaseFragmentView view;
    private Context context;
    public ImageListPresenter(IBaseFragmentView view,Context context){
        this.view=view;
        this.context=context;
        model=new ImageListModel();
    }
    public void loadImageList(int page,int classify,Actions action){
        model.getImageList(page,classify,this,action);
    }
    public void loadBeatifyLegList(int page,Actions action){
        //model.jsoupGetImageList(page,this,action);//官网
        model.umeiGetImageList(page,this,action);
    }
    @Override
    public void getSuccess(List<GalleryBean> list,Actions action,int totalpage) {
        view.showImageList(list,action,totalpage);
    }

    @Override
    public void getBeautifyLegSuccess(List<BeautyLegListBean> list, Actions action, int totalpage) {
        view.showBeautifyImageList(list,action,totalpage);
    }

    @Override
    public void getFailure(int code, String msg,Actions action) {
        view.showError(code,msg,action);
    }

}
