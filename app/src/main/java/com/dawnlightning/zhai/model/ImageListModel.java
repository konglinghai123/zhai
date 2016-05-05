package com.dawnlightning.zhai.model;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.dawnlightning.zhai.base.Actions;
import com.dawnlightning.zhai.bean.ErrorCode;
import com.dawnlightning.zhai.bean.GalleryBean;
import com.dawnlightning.zhai.bean.PicturesBean;
import com.dawnlightning.zhai.utils.AsyncHttp;
import com.dawnlightning.zhai.utils.HttpConstants;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Administrator on 2016/5/4.
 */
public class ImageListModel {
    private final static int pagesize=10;
    public interface ImageListLisenter{
        void getSuccess(List<GalleryBean> list,Actions action,int totalpage);
        void getFailure(int code,String msg,Actions action);
    }
    public interface ImageDetailedListener{
        void getSuccess(List<PicturesBean> list);
        void getFailure(int code,String msg);
    }
    public void getImageList(int page,int classifyid,final ImageListLisenter listLisenter,final Actions action){
        RequestParams params=new RequestParams();
        params.put("page",page);
        params.put("id",classifyid);
        params.put("rows",pagesize);
        AsyncHttp.get(HttpConstants.ApiBaseUrl,params,new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    if (response.getBoolean("status")){
                        int total=Integer.parseInt(response.getString("total"));
                        int page=Integer.valueOf(total)/pagesize;
                        if (total%pagesize>0){
                            page=page+1;
                        }
                        String strlist = response.getString("tngou");
                        List<GalleryBean> imagelist = JSON.parseArray(strlist.toString(), GalleryBean.class);
                        if (imagelist.size()>0){
                            listLisenter.getSuccess(imagelist,action,page);
                        }else{
                            listLisenter.getFailure(ErrorCode.NoNextPage,"没有更多的图片",action);
                        }
                    }else{
                        listLisenter.getFailure(ErrorCode.GetImageListError,"获取图片列表失败",action);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listLisenter.getFailure(ErrorCode.GetImageListError, "获取图片列表失败",action);
                }
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                listLisenter.getFailure(ErrorCode.GetImageListError, "获取图片列表失败",action);
            }
        });

    }
    public void viewImageDetail(int classifyid,final ImageDetailedListener listener){
        RequestParams params=new RequestParams();
        params.put("id",classifyid);
        AsyncHttp.get(HttpConstants.ApiViewImageDetail, params, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.e("response",response.toString());
                try {
                    if (response.getBoolean("status")){
                        String strlist = response.getString("list");
                        List<PicturesBean> imagelist = JSON.parseArray(strlist.toString(), PicturesBean.class);
                        if (imagelist.size()>0){
                            for (PicturesBean bean:imagelist){
                                String temp=bean.getSrc();
                                bean.setSrc(HttpConstants.ImageBaseUrl+temp);
                            }
                            listener.getSuccess(imagelist);
                        }else{
                            listener.getFailure(ErrorCode.GetImageDetailError,"没有更多图片");
                        }

                    }else{
                        listener.getFailure(ErrorCode.GetImageDetailError,"获取详细失败");
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                    listener.getFailure(ErrorCode.GetImageDetailError, e.toString());
                }

            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                listener.getFailure(ErrorCode.GetImageDetailError, errorResponse.toString());
            }
        });
    }
}
