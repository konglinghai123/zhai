package com.dawnlightning.zhai.view;

import com.dawnlightning.zhai.base.Actions;
import com.dawnlightning.zhai.bean.GalleryBean;

import java.util.List;

/**
 * Created by Administrator on 2016/5/4.
 */
public interface IBaseFragmentView{
    public void showImageList(List<GalleryBean> list,Actions action,int totalpage);
    public void showError(int code,String msg,Actions action);
}
