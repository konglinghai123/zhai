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
import com.dawnlightning.zhai.bean.ImageDetailedBean;
import com.dawnlightning.zhai.bean.ImageListBean;
import com.dawnlightning.zhai.presenter.ImageDetailedPresenter;
import com.dawnlightning.zhai.view.IViewImageDetailedView;


import java.util.ArrayList;
import java.util.List;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;

/**
 * Created by Administrator on 2016/5/4.
 */
public class ViewImagesActivity extends BaseActivity implements IViewImageDetailedView {
    private ImageDetailedPresenter imageDetailedPresenter;
    private ImageListBean bean=null;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private PictureDetailedAdapter pictureDetailedAdapter;
    private List<ImageDetailedBean> list=new ArrayList<ImageDetailedBean>();
    private CircularProgressBar circularProgressBar;
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
                finish();
            }
        });
        recyclerView=(RecyclerView)findViewById(R.id.rv_imagedetailed);
        pictureDetailedAdapter=new PictureDetailedAdapter(this,list);
        recyclerView.setHasFixedSize(true);
        circularProgressBar=(CircularProgressBar)findViewById(R.id.pb_imagedetail_loading);
        circularProgressBar.setVisibility(View.VISIBLE);
        StaggeredGridLayoutManager layoutManager=null;
        imageDetailedPresenter=new ImageDetailedPresenter(this,this);
        if (getIntent().getStringExtra("type").equals("ApiGrils")){
            layoutManager = new StaggeredGridLayoutManager(
                    2, StaggeredGridLayoutManager.VERTICAL);

            imageDetailedPresenter.loadImageDetailed(((ImageListBean)getIntent().getSerializableExtra("Gallery")).getUrl());
        }else if(getIntent().getStringExtra("type").equals("Beautify")){
            layoutManager = new StaggeredGridLayoutManager(
                    3, StaggeredGridLayoutManager.VERTICAL);
            imageDetailedPresenter.loadmeitu(((ImageListBean)getIntent().getSerializableExtra("Beautify")).getUrl());
        }else if(getIntent().getStringExtra("type").equals("NewApiGrils")){
            layoutManager = new StaggeredGridLayoutManager(
                    3, StaggeredGridLayoutManager.VERTICAL);
            imageDetailedPresenter.loadbeilaqi(((ImageListBean) getIntent().getSerializableExtra("Beautify")).getUrl());
        }
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(pictureDetailedAdapter);

    }

    @Override
    public void showPictures(List<ImageDetailedBean> list) {

        circularProgressBar.setVisibility(View.GONE);
        pictureDetailedAdapter.setList(list);
        pictureDetailedAdapter.notifyDataSetChanged();

    }

    @Override
    public void showError(int code, String msg) {
        circularProgressBar.setVisibility(View.GONE);
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }



}
