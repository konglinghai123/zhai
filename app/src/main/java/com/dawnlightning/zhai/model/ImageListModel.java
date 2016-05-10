package com.dawnlightning.zhai.model;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.dawnlightning.zhai.base.Actions;
import com.dawnlightning.zhai.bean.ImageDetailedBean;
import com.dawnlightning.zhai.bean.ImageListBean;
import com.dawnlightning.zhai.bean.BeiLaQiApiListBean;
import com.dawnlightning.zhai.bean.BeiLaQiApiDetailedBean;
import com.dawnlightning.zhai.base.ErrorCode;
import com.dawnlightning.zhai.bean.TianGouApiListBean;
import com.dawnlightning.zhai.utils.AsyncHttp;
import com.dawnlightning.zhai.utils.HttpConstants;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Administrator on 2016/5/4.
 */
public class ImageListModel {
    private final static int pagesize=10;
    public interface ImageListLisenter{
        void getSuccess(List<ImageListBean> list, Actions action, int totalpage);
        void getFailure(int code,String msg,Actions action);

    }
    public void umeiGetImageList(int page,final ImageListLisenter listLisenter,final Actions action){
        String url=String.format(HttpConstants.umei,page);
        AsyncHttp.jsoupget(url, null, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                listLisenter.getFailure(ErrorCode.GetImageListError,"获取图片列表失败",action);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if (statusCode==200){
                    List<ImageListBean> list=new ArrayList<ImageListBean>();
                    Document doc = Jsoup.parse(responseString);
                    Elements divs=doc.getElementsByClass("t");
                    Element pages=doc.getElementsByClass("pages").first();
                    int page=Integer.parseInt(pages.getElementsByTag("a").last().text().toString());
                    int index=0;
                    for (Element element:divs){
                        if (index>0&&index<divs.size()-1) {
                            ImageListBean imageListBean =new ImageListBean();
                            Element element1=element.getElementsByClass("title").first().getElementsByTag("a").first();
                            if (element1!=null){
                                imageListBean.setUrl(HttpConstants.umenibaseurl+element1.attr("href").toString());
                                String name=element1.attr("title").toString();
                                imageListBean.setName(name.substring(name.indexOf("[")+1,name.indexOf("]")));
                                imageListBean.setTerm(name.substring(name.lastIndexOf("[") + 1, name.lastIndexOf("]")));
                                imageListBean.setImg(element.getElementsByClass("img").last().getElementsByTag("img").first().attr("src").toString());
                                list.add(imageListBean);
                            }

                        }
                        index++;
                    }

                    listLisenter.getSuccess(list, action, page);
                }else{
                    listLisenter.getFailure(ErrorCode.GetImageListError,"获取图片列表失败",action);
                }
            }
        });
    }
    public void jsoupGetImageList(int page,final ImageListLisenter listLisenter,final Actions action){
        RequestParams params=new RequestParams();
        params.put("p",page-1);
        AsyncHttp.jsoupget(HttpConstants.BeautyLegPortrait, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                listLisenter.getFailure(ErrorCode.GetImageListError, "获取图片列表失败", action);

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if (statusCode == 200) {
                    List<ImageListBean> list = new ArrayList<ImageListBean>();
                    Document doc = Jsoup.parse(responseString);
                    Element Table_all = doc.getElementsByClass("table_all").first();
                    Element element = doc.getElementsByTag("table").last();
                    Elements trs = element.getElementsByTag("tr");
                    for (int i = 0; i < trs.size(); i++) {
                        Elements tds = trs.get(i).getElementsByTag("td");
                        for (int j = 0; j < tds.size(); j++) {
                            String url = tds.get(j).getElementsByTag("a").attr("href").toString();
                            String name = tds.get(j).getElementsByTag("img").attr("alt").toString();
                            name = name.substring(name.lastIndexOf(" ") + 1);
                            String src = HttpConstants.BeautyLegHost + tds.get(j).getElementsByTag("img").attr("src").toString();
                            String term = url.substring(url.lastIndexOf("no=") + 3);
                            ImageListBean imageListBean = new ImageListBean(url, src, term, name);
                            list.add(imageListBean);
                        }
                    }
                    int totalterm = Integer.parseInt(list.get(0).getTerm());
                    int page = totalterm / 50;
                    if (totalterm % 50 > 0) {
                        page = page + 1;
                    }
                    listLisenter.getSuccess(list, action, page);
                } else {
                    listLisenter.getFailure(ErrorCode.GetImageListError, "获取图片列表失败", action);
                }

            }
        });

    }

    public void getBeilaQiImageList(int page,int classifyid,final ImageListLisenter listLisenter,final Actions action){
        String url=String.format(HttpConstants.BeiLaQiImageList,classifyid,page);
        AsyncHttp.get(url, null, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.e("response", response.toString());
                try {
                    if (response.getInt("status") == 1) {
                        int page = Integer.parseInt(response.getString("totalPage"));
                        String strlist = response.getString("data");
                        List<BeiLaQiApiListBean> imagelist = JSON.parseArray(strlist.toString(), BeiLaQiApiListBean.class);
                        List<ImageListBean> imageListBeans = new ArrayList<ImageListBean>();
                        for (BeiLaQiApiListBean bean : imagelist) {
                            ImageListBean imageListBean = new ImageListBean();
                            imageListBean.setImg(bean.getUrl());
                            imageListBean.setTerm(bean.getCount());
                            imageListBean.setName(bean.getTitle());
                            imageListBean.setUrl(String.format(HttpConstants.BeiLaQiImageDetailed, bean.getId()));
                            imageListBeans.add(imageListBean);
                        }
                        if (imageListBeans.size() > 0) {
                            listLisenter.getSuccess(imageListBeans, action, page);
                        } else {
                            listLisenter.getFailure(ErrorCode.NoNextPage, "没有更多的图片", action);
                        }
                    } else {
                        listLisenter.getFailure(ErrorCode.GetImageListError, "获取图片列表失败", action);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listLisenter.getFailure(ErrorCode.GetImageListError, "获取图片列表失败", action);
                }
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                listLisenter.getFailure(ErrorCode.GetImageListError, "获取图片列表失败", action);
            }
        });
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
                        List<TianGouApiListBean> imagelist = JSON.parseArray(strlist.toString(), TianGouApiListBean.class);
                        List<ImageListBean> picturesBeans=new ArrayList<ImageListBean>();
                        for (TianGouApiListBean bean:imagelist){
                          ImageListBean imageListBean =new ImageListBean();
                            imageListBean.setImg(HttpConstants.ApiImageBaseUrl+bean.getImg());
                            imageListBean.setTerm(String.valueOf(bean.getCount()));
                            imageListBean.setUrl(String.format(HttpConstants.ApiViewImageDetail, bean.getId()));
                            imageListBean.setName(bean.getTitle());
                            picturesBeans.add(imageListBean);
                        }
                        if (picturesBeans.size()>0){
                            listLisenter.getSuccess(picturesBeans, action, page);
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

}
