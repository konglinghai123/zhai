package com.dawnlightning.zhai.view;

import com.dawnlightning.zhai.bean.PicturesBean;

import java.util.List;

/**
 * Created by Administrator on 2016/5/4.
 */
public interface IViewImageDetailedView {
    void showPictures(List<PicturesBean> list);
    void showError(int code,String msg);
}
