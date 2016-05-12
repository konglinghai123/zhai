package com.dawnlightning.zhai.model;

import android.util.Log;

import com.dawnlightning.zhai.api.ImageApiManager;
import com.dawnlightning.zhai.base.ErrorCode;
import com.dawnlightning.zhai.bean.BeiLaQiDetailedBean;
import com.dawnlightning.zhai.bean.ImageDetailedBean;
import com.dawnlightning.zhai.bean.TianGouImagesBean;
import com.dawnlightning.zhai.utils.HttpConstants;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

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
        new ImageApiManager(HttpConstants.umenibaseurl).getUmeiImagesDetailed(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ResponseBody>() {
                    @Override
                    public void call(ResponseBody responseBody) {

                        try {
                            String strresponse = new String(responseBody.bytes(), "gb2312");
                            Document doc = Jsoup.parse(strresponse);
                            String baseurl = HttpConstants.umenibaseurl+url.substring(0, url.lastIndexOf("/") + 1);
                            List<ImageDetailedBean> list = new ArrayList<ImageDetailedBean>();
                            Element pages_content = doc.getElementById("pagination");
                            Elements elements = pages_content.select("a[href]"); // 具有 href 属性的链接
                            Queue<String> listurl = new LinkedList<String>();
                            listurl.add(url.substring(url.lastIndexOf("/") + 1));
                            for (int i = 1; i < elements.size() - 2; i++) {
                                listurl.add(elements.get(i).attr("href"));
                                Log.e("href",elements.get(i).attr("href"));
                            }
                            deepGetImage(listurl, list,baseurl, listener);
                        } catch (IOException e) {
                            e.printStackTrace();
                            listener.getFailure(ErrorCode.GetImageDetailError, "获取专辑详细失败");
                        }

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        listener.getFailure(ErrorCode.GetImageDetailError, "获取专辑详细失败");
                    }
                });

    }
    private void deepGetImage(final  Queue<String> urllist,final  List<ImageDetailedBean> list,final String baseurl ,final  ImageDetailedListener listener){
        if (CurrentThreadCount<MaxThreadCount) {
            final String url = urllist.poll();
            Log.e("url",baseurl+url.toString());
            CurrentThreadCount++;
            new ImageApiManager(baseurl).getUmeiImagesDetailed(url)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<ResponseBody>() {
                        @Override
                        public void call(ResponseBody responseBody) {
                            try {
                                String strresponse = new String(responseBody.bytes(), "gb2312");
                                Document doc = Jsoup.parse(strresponse);
                                Elements imgs = doc.getElementsByClass("img_box");
                                for (Element img : imgs) {
                                    ImageDetailedBean bean = new ImageDetailedBean();
                                    String url = img.getElementsByClass("IMG_show").first().attr("src").toString();
                                    if (url.lastIndexOf("-") != -1) {
                                        url.substring(0, url.lastIndexOf("-"));
                                    }
                                    bean.setSrc(url);
                                    bean.setGallery("1");
                                    bean.setId("1");
                                    list.add(bean);
                                }
                                if (urllist.size() != 0) {
                                    CurrentThreadCount--;
                                    deepGetImage(urllist, list,baseurl, listener);
                                } else {
                                    listener.getSuccess(list);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                                CurrentThreadCount--;
                                deepGetImage(urllist, list,baseurl, listener);
                            }

                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            CurrentThreadCount--;
                            deepGetImage(urllist, list,baseurl, listener);
                        }
                    });
       }

    }

//
//    public void jsoupGetImageDetailed(String url,final ImageDetailedListener listener){
//        AsyncHttp.jsoupget(url, null, new TextHttpResponseHandler() {
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                listener.getFailure(ErrorCode.GetImageDetailError, "获取专辑详细失败");
//            }
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, String responseString) {
//                if (statusCode == 200) {
//                    List<ImageDetailedBean> list = new ArrayList<ImageDetailedBean>();
//                    Document doc = Jsoup.parse(responseString);
//                    Element Table_all = doc.getElementsByClass("table_all").first();
//                    Elements tds = Table_all.getElementsByClass("table3");
//                    for (int i = 0; i < tds.size(); i++) {
//                        ImageDetailedBean bean = new ImageDetailedBean();
//                        bean.setSrc(HttpConstants.BeautyLegHost + tds.get(i).attr("src"));
//                        bean.setGallery("1");
//                        bean.setId("1");
//                        list.add(bean);
//                    }
//                    listener.getSuccess(list);
//                } else {
//                    listener.getFailure(ErrorCode.GetImageDetailError, "获取专辑详细失败");
//                }
//            }
//        });
//    }

    public void getBeilaQiImageDetailed(String url,final ImageDetailedListener listener){
        int id=Integer.parseInt(url.replace("http://appd.beilaqi.com:832/action/iso_action.php?action=article&id=", ""));
        new ImageApiManager(HttpConstants.BeiLaQiBaseUrl).
                getBeiLaQiImageDetailed(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<BeiLaQiDetailedBean>() {
                    @Override
                    public void call(BeiLaQiDetailedBean beiLaQiDetailedBean) {
                        if (beiLaQiDetailedBean.getStatus().contains("1")) {
                            List<ImageDetailedBean> imageDetailedBeans = new ArrayList<ImageDetailedBean>();
                            for (BeiLaQiDetailedBean.DataEntity data : beiLaQiDetailedBean.getData()) {
                                ImageDetailedBean bean = new ImageDetailedBean();
                                bean.setSrc(data.getUrl());
                                bean.setId("1");
                                bean.setGallery("1");
                                imageDetailedBeans.add(bean);
                            }
                            listener.getSuccess(imageDetailedBeans);
                        } else {
                            Log.e("response", beiLaQiDetailedBean.getStatus());
                            listener.getFailure(ErrorCode.GetImageDetailError, "获取专辑详细失败");
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e("error", throwable.toString());
                        listener.getFailure(ErrorCode.GetImageDetailError, "获取专辑详细失败");
                    }
                });


    }

    public void TianGouImageDetail(String url, final ImageDetailedListener listener){
        int id=Integer.parseInt(url.replace("http://www.tngou.net/tnfs/api/show?id=", ""));
        new ImageApiManager(HttpConstants.TianGouBaseUrl).getTianGouImageDetailed(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<TianGouImagesBean>() {
                    @Override
                    public void call(TianGouImagesBean tianGouImagesBean) {
                        Log.e("url",tianGouImagesBean.getUrl());
                        if (tianGouImagesBean.getStatus()) {
                            List<ImageDetailedBean> imagelist = new ArrayList<ImageDetailedBean>();
                            for (TianGouImagesBean.ListEntity listEntity : tianGouImagesBean.getList()) {
                                ImageDetailedBean bean = new ImageDetailedBean();
                                bean.setSrc(HttpConstants.ApiImageBaseUrl + listEntity.getSrc());
                                bean.setGallery(String.valueOf(listEntity.getGallery()));
                                bean.setId(String.valueOf(listEntity.getId()));
                                imagelist.add(bean);
                            }
                            if (imagelist.size() > 0) {
                                listener.getSuccess(imagelist);
                            } else {
                                listener.getFailure(ErrorCode.GetImageDetailError, "没有更多图片");
                            }

                        } else {
                            listener.getFailure(ErrorCode.GetImageDetailError, "获取详细失败");
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                        listener.getFailure(ErrorCode.GetImageDetailError, "获取详细失败");
                    }
                });
    }
}
