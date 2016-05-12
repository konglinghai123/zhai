package com.dawnlightning.zhai.model;

import com.dawnlightning.zhai.api.ImageApiManager;
import com.dawnlightning.zhai.base.Actions;
import com.dawnlightning.zhai.bean.BeiLaQiListBean;
import com.dawnlightning.zhai.bean.ImageListBean;
import com.dawnlightning.zhai.base.ErrorCode;
import com.dawnlightning.zhai.bean.TianGouListBean;
import com.dawnlightning.zhai.utils.HttpConstants;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

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
        new ImageApiManager(HttpConstants.umenibaseurl).getUmeiImageList(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ResponseBody>() {
                    @Override
                    public void call(ResponseBody response) {
                        try {
                            String strresponse = new String(response.bytes(), "gb2312");
                            Document doc = Jsoup.parse(strresponse);
                            List<ImageListBean> list = new ArrayList<ImageListBean>();
                            Elements divs = doc.getElementsByClass("t");
                            Element pages = doc.getElementsByClass("pages").first();
                            int page = Integer.parseInt(pages.getElementsByTag("a").last().text().toString());
                            int index = 0;
                            for (Element element : divs) {
                                if (index > 0 && index < divs.size() - 1) {
                                    ImageListBean imageListBean = new ImageListBean();
                                    Element element1 = element.getElementsByClass("title").first().getElementsByTag("a").first();
                                    if (element1 != null) {
                                        imageListBean.setUrl(element1.attr("href").toString());
                                        String name = element1.attr("title").toString();
                                        imageListBean.setName(name.substring(name.indexOf("[") + 1, name.indexOf("]")));
                                        imageListBean.setTerm(name.substring(name.lastIndexOf("[") + 1, name.lastIndexOf("]")));
                                        imageListBean.setImg(element.getElementsByClass("img").last().getElementsByTag("img").first().attr("src").toString());
                                        list.add(imageListBean);
                                    }

                                }
                                index++;
                            }
                            if (list.size() > 0) {
                                listLisenter.getSuccess(list, action, page);
                            } else {
                                listLisenter.getFailure(ErrorCode.NoNextPage, "没有更多的图片", action);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            listLisenter.getFailure(ErrorCode.GetImageListError, e.toString(), action);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        listLisenter.getFailure(ErrorCode.GetImageListError, throwable.toString(), action);
                    }
                });
    }
    public void getBeilaQiImageList(int page,int classifyid,final ImageListLisenter listLisenter,final Actions action){
        new ImageApiManager(HttpConstants.BeiLaQiBaseUrl).getBeiLaQiImageList(page,classifyid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BeiLaQiListBean>() {
                    @Override
                    public void call(BeiLaQiListBean beiLaQiListBean) {
                        if (beiLaQiListBean.getStatus().contains("1")) {
                            int page =Integer.parseInt(beiLaQiListBean.getTotalPage());
                            List<ImageListBean> imageListBeans = new ArrayList<ImageListBean>();
                            for (BeiLaQiListBean.DataEntity bean :beiLaQiListBean.getData()) {
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
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                        listLisenter.getFailure(ErrorCode.GetImageListError, "获取图片列表失败", action);
                    }
                });

    }
    public void getTianGouImageList(int page, int classifyid, final ImageListLisenter listLisenter, final Actions action){
        new ImageApiManager(HttpConstants.TianGouBaseUrl).getTianGouImageList(page, classifyid,pagesize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<TianGouListBean>() {
                    @Override
                    public void call(TianGouListBean tianGouListBean) {
                        if (tianGouListBean.getStatus()){
                            int total =tianGouListBean.getTotal();
                            int page = Integer.valueOf(total) / pagesize;
                            if (total % pagesize > 0) {
                                page = page + 1;
                            }
                            List<ImageListBean> picturesBeans=new ArrayList<ImageListBean>();
                            for (TianGouListBean.TngouEntity bean:tianGouListBean.getTngou()){
                                ImageListBean imageListBean =new ImageListBean();
                                imageListBean.setImg(HttpConstants.ApiImageBaseUrl+bean.getImg());
                                imageListBean.setTerm(String.valueOf(bean.getCount()));
                                imageListBean.setUrl(String.format(HttpConstants.ApiViewImageDetail, bean.getId()));
                                imageListBean.setName(bean.getTitle());
                                picturesBeans.add(imageListBean);
                            }
                            if (picturesBeans.size() > 0) {
                                listLisenter.getSuccess(picturesBeans, action,tianGouListBean.getTotal());
                            } else {
                                listLisenter.getFailure(ErrorCode.NoNextPage, "没有更多的图片", action);
                            }
                        }else{
                            listLisenter.getFailure(ErrorCode.GetImageListError, "获取图片列表失败", action);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        listLisenter.getFailure(ErrorCode.GetImageListError, "获取图片列表失败", action);
                    }
                });

    }

}
