package com.dawnlightning.zhai.fragment;

import android.os.Bundle;

import com.dawnlightning.zhai.adapter.BeautifyLegRecyclerViewAdaper;
import com.dawnlightning.zhai.base.Actions;
import com.dawnlightning.zhai.bean.BeautyLegListBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/9.
 */
public class BeautifyLegImagesFragement extends BaseFragment {
    private List<BeautyLegListBean> beautyLegListBeanList=new ArrayList<>();
    public  static BeautifyLegImagesFragement newInstance(Bundle data) {
        BeautifyLegImagesFragement beautifyLegImagesFragement =new BeautifyLegImagesFragement();
        beautifyLegImagesFragement.setArguments(data);
        return beautifyLegImagesFragement;
    }
    @Override
    public void initAdapter() {
        adapter= new BeautifyLegRecyclerViewAdaper(getActivity());
        adapter.setList(beautyLegListBeanList);
    }

    @Override
    public void Refresh(int page, Actions action) {
        imageListPresenter.loadBeatifyLegList(page,action);
    }

    @Override
    public void LoadMore(int page, Actions action) {
        imageListPresenter.loadBeatifyLegList(page,action);
    }

    @Override
    public void GoTo(int page, Actions action) {
        imageListPresenter.loadBeatifyLegList(page,action);
    }
}
