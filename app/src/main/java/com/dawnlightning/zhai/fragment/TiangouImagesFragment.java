package com.dawnlightning.zhai.fragment;

import android.os.Bundle;

import com.dawnlightning.zhai.adapter.TiangouRecyclerViewAdapter;
import com.dawnlightning.zhai.base.Actions;
import com.dawnlightning.zhai.bean.GalleryBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/9.
 */
public class TiangouImagesFragment extends BaseFragment {
    private List<GalleryBean> data = new ArrayList<>();
    public  static TiangouImagesFragment newInstance(Bundle data) {
        TiangouImagesFragment tiangouImagesFragment =new TiangouImagesFragment();
        tiangouImagesFragment.setArguments(data);
        return tiangouImagesFragment;
    }



    @Override
    public void initAdapter() {
        adapter= new TiangouRecyclerViewAdapter(getActivity());
        adapter.setList(data);
    }

    @Override
    public void Refresh(int page, Actions action) {
        imageListPresenter.loadImageList(page, channel_id, action);
    }

    @Override
    public void LoadMore(int page, Actions action) {
        imageListPresenter.loadImageList(page, channel_id, action);
    }

    @Override
    public void GoTo(int page, Actions action) {
        imageListPresenter.loadImageList(page, channel_id, action);
    }
}
