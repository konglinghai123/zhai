package com.dawnlightning.zhai.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dawnlightning.zhai.R;
import com.dawnlightning.zhai.adapter.PictureDetailedAdapter;
import com.dawnlightning.zhai.base.Actions;
import com.dawnlightning.zhai.base.AppApplication;
import com.dawnlightning.zhai.bean.ImageDetailedBean;
import com.dawnlightning.zhai.bean.ImageListBean;
import com.dawnlightning.zhai.presenter.ImageDetailedPresenter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;

/**
 * Created by Administrator on 2016/5/12.
 */
public class MainFragment extends Fragment {
    private RecyclerView recyclerView;
    private PictureDetailedAdapter pictureDetailedAdapter;
    private List<ImageDetailedBean> list=new ArrayList<ImageDetailedBean>();
    private CircularProgressBar circularProgressBar;
    private Toolbar toolbar;
    private final static int FILEPATH=1;
    private final static int FINISH=2;
    public  static MainFragment newInstance() {
        MainFragment mainFragment =new MainFragment();
        return mainFragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view =inflater.inflate(R.layout.activity_imagedetailed, container,false);
        toolbar=(Toolbar)view.findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);
        recyclerView=(RecyclerView)view.findViewById(R.id.rv_imagedetailed);
        recyclerView.setHasFixedSize(true);
        circularProgressBar=(CircularProgressBar)view.findViewById(R.id.pb_imagedetail_loading);
        circularProgressBar.setVisibility(View.VISIBLE);
        StaggeredGridLayoutManager  layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        pictureDetailedAdapter=new PictureDetailedAdapter(getActivity(),list);
        refresh();
        return view;
    }






    public void refresh(){
        list.clear();
        pictureDetailedAdapter.notifyDataSetChanged();
        circularProgressBar.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                getAllFiles(AppApplication.cache("zhai/Cache"));
            }
        }).start();
    }
    public void getAllFiles(File root){
        File files[] = root.listFiles();
        if(files != null){
            for (File f : files){
                if(f.isDirectory()){
                    getAllFiles(f);
                }else{

                    ImageDetailedBean bean=new ImageDetailedBean();
                    bean.setSrc("file://"+f.getPath());
                    bean.setId("1");
                    bean.setGallery("1");
                    Message message = mHandle.obtainMessage();
                    message.what = FILEPATH;
                    message.obj = bean;
                    message.sendToTarget();
                }
            }
            Message message = mHandle.obtainMessage();
            message.what = FINISH;
            message.sendToTarget();
        }
    }
    public Handler mHandle=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case FILEPATH:
                    ImageDetailedBean bean=(ImageDetailedBean)msg.obj;
                    list.add(bean);
                    break;
                case FINISH:

                    recyclerView.setAdapter(pictureDetailedAdapter);
                    circularProgressBar.setVisibility(View.GONE);
                    break;

            }
            super.handleMessage(msg);
        }
    };
}
