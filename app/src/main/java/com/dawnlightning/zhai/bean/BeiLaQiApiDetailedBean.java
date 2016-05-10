package com.dawnlightning.zhai.bean;

/**
 * Created by Administrator on 2016/5/9.
 */
public class BeiLaQiApiDetailedBean {

    /**
     * url : http://p81.beilaqi.com:832/uploads/tu/xl/tt/20160415/0ngf0ioeg4f.jpg
     * albumId : 105418
     * width : 0
     * height : 0
     */

    private String url;
    private String albumId;
    private String width;
    private String height;

    public void setUrl(String url) {
        this.url = url;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getUrl() {
        return url;
    }

    public String getAlbumId() {
        return albumId;
    }

    public String getWidth() {
        return width;
    }

    public String getHeight() {
        return height;
    }
}
