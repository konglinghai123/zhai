package com.dawnlightning.zhai.base;

/**
 * Created by Administrator on 2016/5/4.
 */
public interface IRefreshAndLoadmore {
    void Refresh(int page,Actions action);
    void LoadMore(int page,Actions action);
    void GoTo(int page,Actions actions);
}

