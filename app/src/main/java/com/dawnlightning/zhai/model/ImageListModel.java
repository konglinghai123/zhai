package com.dawnlightning.zhai.model;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.dawnlightning.zhai.base.Actions;
import com.dawnlightning.zhai.bean.BeautyLegListBean;
import com.dawnlightning.zhai.bean.ErrorCode;
import com.dawnlightning.zhai.bean.GalleryBean;
import com.dawnlightning.zhai.bean.PicturesBean;
import com.dawnlightning.zhai.utils.AsyncHttp;
import com.dawnlightning.zhai.utils.HttpConstants;
import com.loopj.android.http.AsyncHttpClient;
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
    private final static int MaxThreadCount=15;
    private  static int CurrentThreadCount=0;
    public interface ImageListLisenter{
        void getSuccess(List<GalleryBean> list,Actions action,int totalpage);
        void getBeautifyLegSuccess(List<BeautyLegListBean> list,Actions action,int totalpage);
        void getFailure(int code,String msg,Actions action);

    }
    public interface ImageDetailedListener{
        void getSuccess(List<PicturesBean> list);
        void getFailure(int code,String msg);
    }
    public void umeiGetImageDetailed(final String url,final  ImageDetailedListener listener){

        AsyncHttp.jsoupget(url, null, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                listener.getFailure(ErrorCode.GetImageDetailError,"获取专辑详细失败");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if (statusCode==200){
                    List<PicturesBean> list=new ArrayList<PicturesBean>();
                    String baseurl=url.substring(0,url.lastIndexOf("/")+1);
                    Document doc = Jsoup.parse(responseString);
                    Element pages_content=doc.getElementById("pagination");
                    Elements elements = pages_content.select("a[href]"); // 具有 href 属性的链接
                    Queue<String> listurl=new LinkedList<String>();
                    listurl.add(url);
                    for (int i=1;i<elements.size()-2;i++){
                        listurl.add(baseurl+elements.get(i).attr("href"));
                    }
                    deepGetImage(listurl,list,listener);
                }else{
                    listener.getFailure(ErrorCode.GetImageDetailError,"获取专辑详细失败");
                }
            }
        });
    }
    private void deepGetImage(final  Queue<String> urllist,final  List<PicturesBean> list ,final  ImageDetailedListener listener){
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
                            PicturesBean bean=new PicturesBean();
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
                    List<BeautyLegListBean> list=new ArrayList<BeautyLegListBean>();
                    Document doc = Jsoup.parse(responseString);
                    Elements divs=doc.getElementsByClass("t");
                    Element pages=doc.getElementsByClass("pages").first();
                    int page=Integer.parseInt(pages.getElementsByTag("a").last().text().toString());
                    int index=0;
                    for (Element element:divs){
                        if (index>0&&index<divs.size()-1) {
                            BeautyLegListBean beautyLegListBean=new BeautyLegListBean();
                            Element element1=element.getElementsByClass("title").first().getElementsByTag("a").first();
                            if (element1!=null){
                                beautyLegListBean.setUrl(HttpConstants.umenibaseurl+element1.attr("href").toString());
                                String name=element1.attr("title").toString();
                                beautyLegListBean.setName(name.substring(name.indexOf("[")+1,name.indexOf("]")));
                                beautyLegListBean.setTerm(name.substring(name.lastIndexOf("[") + 1, name.lastIndexOf("]")));
                                beautyLegListBean.setImg(element.getElementsByClass("img").last().getElementsByTag("img").first().attr("src").toString());
                                list.add(beautyLegListBean);
                            }

                        }
                        index++;
                    }

                    listLisenter.getBeautifyLegSuccess(list,action,page);
                }else{
                    listLisenter.getFailure(ErrorCode.GetImageListError,"获取图片列表失败",action);
                }
            }
        });
    }
    public void jsoupGetImageList(int page,final ImageListLisenter listLisenter,final Actions action){
        RequestParams params=new RequestParams();
        params.put("p",page-1);
        AsyncHttp.jsoupget(HttpConstants.BeautyLegPortrait,params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                listLisenter.getFailure(ErrorCode.GetImageListError,"获取图片列表失败",action);

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if (statusCode==200){
                    List<BeautyLegListBean> list=new ArrayList<BeautyLegListBean>();
                    Document doc = Jsoup.parse(responseString);
                    Element Table_all = doc.getElementsByClass("table_all").first();
                    Element element=doc.getElementsByTag("table").last();
                    Elements trs = element.getElementsByTag("tr");
                    for(int i = 0;i<trs.size();i++){
                        Elements tds = trs.get(i).getElementsByTag("td");
                        for(int j = 0;j<tds.size();j++){
                            String url=tds.get(j).getElementsByTag("a").attr("href").toString();
                            String name=tds.get(j).getElementsByTag("img").attr("alt").toString();
                            name=name.substring(name.lastIndexOf(" ")+1);
                            String src=HttpConstants.BeautyLegHost+tds.get(j).getElementsByTag("img").attr("src").toString();
                            String term=url.substring(url.lastIndexOf("no=")+3);
                            BeautyLegListBean beautyLegListBean=new BeautyLegListBean(url,src,term,name);
                            list.add(beautyLegListBean);
                        }
                    }
                    int totalterm=Integer.parseInt(list.get(0).getTerm());
                    int page=totalterm/50;
                    if (totalterm%50>0){
                        page=page+1;
                    }
                    listLisenter.getBeautifyLegSuccess(list,action,page);
                }else{
                    listLisenter.getFailure(ErrorCode.GetImageListError,"获取图片列表失败",action);
                }

            }
        });

    }
    public void jsoupGetImageDetailed(String url,final ImageDetailedListener listener){
        AsyncHttp.jsoupget(url, null, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                listener.getFailure(ErrorCode.GetImageDetailError,"获取专辑详细失败");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if (statusCode==200){
                    List<PicturesBean> list=new ArrayList<PicturesBean>();
                    Document doc = Jsoup.parse(responseString);
                    Element Table_all = doc.getElementsByClass("table_all").first();
                    Elements tds=Table_all.getElementsByClass("table3");
                    for (int i=0;i<tds.size();i++){
                        PicturesBean bean=new PicturesBean();
                        bean.setSrc(HttpConstants.BeautyLegHost+tds.get(i).attr("src"));
                        bean.setGallery("1");
                        bean.setId("1");
                        list.add(bean);
                    }
                    listener.getSuccess(list);
                }else{
                    listener.getFailure(ErrorCode.GetImageDetailError,"获取专辑详细失败");
                }
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
