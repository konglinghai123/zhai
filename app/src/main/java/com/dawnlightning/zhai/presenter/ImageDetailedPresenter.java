package com.dawnlightning.zhai.presenter;

import android.content.Context;

import com.dawnlightning.zhai.bean.PicturesBean;
import com.dawnlightning.zhai.model.ImageListModel;
import com.dawnlightning.zhai.view.IViewImageDetailedView;

import java.util.List;

/**
 * Created by Administrator on 2016/5/4.
 */
public class ImageDetailedPresenter implements ImageListModel.ImageDetailedListener{
    private ImageListModel model;
    private IViewImageDetailedView view;
    private Context context;
    public ImageDetailedPresenter(IViewImageDetailedView view,Context context){
        this.view=view;
        this.context=context;
        model=new ImageListModel();
    }
    public void loadBeauify(String url){
        model.jsoupGetImageDetailed(url,this);
    }
    public void loadImageDetailed(int classifyid){
       model.viewImageDetail(classifyid,this);
    }

    @Override
    public void getSuccess(List<PicturesBean> list) {
        view.showPictures(list);
    }

    @Override
    public void getFailure(int code, String msg) {
        view.showError(code,msg);
    }
}
