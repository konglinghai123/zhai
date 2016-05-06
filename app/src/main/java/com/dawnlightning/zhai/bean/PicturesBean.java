package com.dawnlightning.zhai.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/5/4.
 */
public class PicturesBean implements Serializable{
    private String gallery;

    private String id;
    private String src;

    public String getGallery() {
        return gallery;
    }

    public void setGallery(String gallery) {
        this.gallery = gallery;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public PicturesBean(String id, String src, String gallery) {
        this.id = id;
        this.src = src;
        this.gallery = gallery;
    }

    @Override
    public String toString() {
        return "PicturesBean{" +
                "gallery='" + gallery + '\'' +
                ", id='" + id + '\'' +
                ", src='" + src + '\'' +
                '}';
    }
    public PicturesBean(){

    }
}
