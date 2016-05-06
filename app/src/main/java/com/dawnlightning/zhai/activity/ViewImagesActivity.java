package com.dawnlightning.zhai.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dawnlightning.zhai.R;
import com.dawnlightning.zhai.adapter.PictureDetailedAdapter;
import com.dawnlightning.zhai.base.BaseActivity;
import com.dawnlightning.zhai.bean.BeautyLegListBean;
import com.dawnlightning.zhai.bean.GalleryBean;
import com.dawnlightning.zhai.bean.PicturesBean;
import com.dawnlightning.zhai.presenter.ImageDetailedPresenter;
import com.dawnlightning.zhai.view.IViewImageDetailedView;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/4.
 */
public class ViewImagesActivity extends BaseActivity implements IViewImageDetailedView {

    private ImageDetailedPresenter imageDetailedPresenter;
    private GalleryBean bean=null;
    private RecyclerView recyclerView;
    private PictureDetailedAdapter pictureDetailedAdapter;
    private List<PicturesBean > list=new ArrayList<PicturesBean>();
    private Toolbar toolbar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagedetailed);
        setNeedBackGesture(true);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        ((TextView)toolbar.findViewById(R.id.toolbar_title)).setText("图片详细");
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.ic_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                pictureDetailedAdapter.clearmemorycache();
                pictureDetailedAdapter.cleardata();
                pictureDetailedAdapter = null;
                finish();
            }
        });
        recyclerView=(RecyclerView)findViewById(R.id.rv_imagedetailed);
        pictureDetailedAdapter=new PictureDetailedAdapter(this,list);
        recyclerView.setHasFixedSize(true);

        StaggeredGridLayoutManager layoutManager=null;
        imageDetailedPresenter=new ImageDetailedPresenter(this,this);
        if (getIntent().getStringExtra("type").equals("ApiGrils")){
            layoutManager = new StaggeredGridLayoutManager(
                    2, StaggeredGridLayoutManager.VERTICAL);
            bean=(GalleryBean)getIntent().getSerializableExtra("Gallery");
            imageDetailedPresenter.loadImageDetailed(bean.getId());
        }else if(getIntent().getStringExtra("type").equals("Beautify")){
            layoutManager = new StaggeredGridLayoutManager(
                    3, StaggeredGridLayoutManager.VERTICAL);
            imageDetailedPresenter.loadBeauify(((BeautyLegListBean)getIntent().getSerializableExtra("Beautify")).getUrl());
        }
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(pictureDetailedAdapter);

    }

    @Override
    public void showPictures(List<PicturesBean> list) {

        pictureDetailedAdapter.setList(list);
        pictureDetailedAdapter.notifyDataSetChanged();

    }

    @Override
    public void showError(int code, String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }



}
