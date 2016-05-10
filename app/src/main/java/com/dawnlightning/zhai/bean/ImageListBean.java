package com.dawnlightning.zhai.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/5/5.
 */
public class ImageListBean implements Serializable {
    private String url;
    private String img;
    private String term;
    private String name;
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ImageListBean(String url, String img, String term, String name) {
        this.url = url;
        this.img = img;
        this.term = term;
        this.name = name;
    }

    public ImageListBean(){

    }
}
