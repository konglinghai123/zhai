package com.dawnlightning.zhai.model;

import com.alibaba.fastjson.JSON;
import com.dawnlightning.zhai.base.ErrorCode;
import com.dawnlightning.zhai.bean.BeiLaQiApiDetailedBean;
import com.dawnlightning.zhai.bean.ImageDetailedBean;
import com.dawnlightning.zhai.utils.AsyncHttp;
import com.dawnlightning.zhai.utils.HttpConstants;
import com.loopj.android.http.JsonHttpResponseHandler;
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
 * Created by Administrator on 2016/5/10.
 */
public class ImageDetailedModel {
    private final static int MaxThreadCount=15;
    private  static int CurrentThreadCount=0;
    public interface ImageDetailedListener{
        void getSuccess(List<ImageDetailedBean> list);
        void getFailure(int code,String msg);
    }

    public void umeiGetImageDetailed(final String url,final  ImageDetailedListener listener){

        AsyncHttp.jsoupget(url, null, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                listener.getFailure(ErrorCode.GetImageDetailError, "获取专辑详细失败");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if (statusCode == 200) {
                    List<ImageDetailedBean> list = new ArrayList<ImageDetailedBean>();
                    String baseurl = url.substring(0, url.lastIndexOf("/") + 1);
                    Document doc = Jsoup.parse(responseString);
                    Element pages_content = doc.getElementById("pagination");
                    Elements elements = pages_content.select("a[href]"); // 具有 href 属性的链接
                    Queue<String> listurl = new LinkedList<String>();
                    listurl.add(url);
                    for (int i = 1; i < elements.size() - 2; i++) {
                        listurl.add(baseurl + elements.get(i).attr("href"));
                    }
                    deepGetImage(listurl, list, listener);
                } else {
                    listener.getFailure(ErrorCode.GetImageDetailError, "获取专辑详细失败");
                }
            }
        });
    }
    private void deepGetImage(final  Queue<String> urllist,final  List<ImageDetailedBean> list ,final  ImageDetailedListener listener){
        if (CurrentThreadCount<MaxThreadCount){
            final  String url=urllist.poll();
            CurrentThreadCount++;
            AsyncHttp.jsoupget(url,null,new TextHttpResponseHandler(){

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    CurrentThreadCount--;
                    deepGetImage(urllist,list,listener);
                }
                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    if (statusCode==200){
                        Document doc = Jsoup.parse(responseString);
                        Elements imgs=doc.getElementsByClass("img_box");
                        for (Element img:imgs){
                            ImageDetailedBean bean=new ImageDetailedBean();
                            String url= img.getElementsByClass("IMG_show").first().attr("src").toString();
                            if (url.lastIndexOf("-")!=-1){
                                url.substring(0,url.lastIndexOf("-"));
                            }
                            bean.setSrc(url);
                            bean.setGallery("1");
                            bean.setId("1");
                            list.add(bean);
                        }
                        if (urllist.size()!=0){
                            CurrentThreadCount--;
                            deepGetImage(urllist,list,listener);
                        }else{
                            listener.getSuccess(list);
                        }

                    }else{
                        CurrentThreadCount--;
                        deepGetImage(urllist,list,listener);
                    }
                }
            });
        }

    }

    public void jsoupGetImageDetailed(String url,final ImageDetailedListener listener){
        AsyncHttp.jsoupget(url, null, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                listener.getFailure(ErrorCode.GetImageDetailError, "获取专辑详细失败");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if (statusCode == 200) {
                    List<ImageDetailedBean> list = new ArrayList<ImageDetailedBean>();
                    Document doc = Jsoup.parse(responseString);
                    Element Table_all = doc.getElementsByClass("table_all").first();
                    Elements tds = Table_all.getElementsByClass("table3");
                    for (int i = 0; i < tds.size(); i++) {
                        ImageDetailedBean bean = new ImageDetailedBean();
                        bean.setSrc(HttpConstants.BeautyLegHost + tds.get(i).attr("src"));
                        bean.setGallery("1");
                        bean.setId("1");
                        list.add(bean);
                    }
                    listener.getSuccess(list);
                } else {
                    listener.getFailure(ErrorCode.GetImageDetailError, "获取专辑详细失败");
                }
            }
        });
    }

    public void getBeilaQiImageDetailed(String url,final ImageDetailedListener listener){
        AsyncHttp.get(url, null, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if (response.getInt("status") == 1) {
                        String strlist = response.getString("data");
                        List<BeiLaQiApiDetailedBean> imagelist = JSON.parseArray(strlist.toString(), BeiLaQiApiDetailedBean.class);
                        List<ImageDetailedBean> imageDetailedBeans = new ArrayList<ImageDetailedBean>();
                        for (BeiLaQiApiDetailedBean beiLaQiApiDetailedBean : imagelist) {
                            ImageDetailedBean bean = new ImageDetailedBean();
                            bean.setSrc(beiLaQiApiDetailedBean.getUrl());
                            bean.setId("1");
                            bean.setGallery("1");
                            imageDetailedBeans.add(bean);
                        }
                        listener.getSuccess(imageDetailedBeans);
                    } else {
                        listener.getFailure(ErrorCode.GetImageDetailError, "获取专辑详细失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.getFailure(ErrorCode.GetImageDetailError, "获取专辑详细失败");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                listener.getFailure(ErrorCode.GetImageDetailError, "获取专辑详细失败");
            }
        });

    }

    public void viewImageDetail(String url,final ImageDetailedListener listener){

        AsyncHttp.get(url,null, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if (response.getBoolean("status")){
                        String strlist = response.getString("list");
                        List<ImageDetailedBean> imagelist = JSON.parseArray(strlist.toString(), ImageDetailedBean.class);
                        if (imagelist.size()>0){
                            for (ImageDetailedBean bean:imagelist){
                                String temp=bean.getSrc();
                                bean.setSrc(HttpConstants.ApiImageBaseUrl +temp);
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
