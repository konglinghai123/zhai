package com.dawnlightning.zhai.view;

import com.dawnlightning.zhai.bean.ImageDetailedBean;

import java.util.List;

/**
 * Created by Administrator on 2016/5/4.
 */
public interface IViewImageDetailedView {
    void showPictures(List<ImageDetailedBean> list);
    void showError(int code,String msg);
}
