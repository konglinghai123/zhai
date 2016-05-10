package com.dawnlightning.zhai.adapter.imagelistadapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.dawnlightning.zhai.activity.ViewImagesActivity;
import com.dawnlightning.zhai.bean.ImageListBean;

/**
 * Created by Administrator on 2016/5/9.
 */
public class TiangouImageListAdapter extends NormalImageListAdaper {

    public TiangouImageListAdapter(Context context) {
        super(context);

    }
    @Override
    public void ImageOnClickListener(int position) {
        ImageListBean bean = (ImageListBean)data.get(position);
        Intent intent = new Intent();
        intent.setClass(context, ViewImagesActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Gallery", bean);
        bundle.putSerializable("type","ApiGrils");
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
