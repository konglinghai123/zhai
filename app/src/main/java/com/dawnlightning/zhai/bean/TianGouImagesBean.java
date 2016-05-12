package com.dawnlightning.zhai.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/5/12.
 */
public class TianGouImagesBean {

    /**
     * count : 1512
     * fcount : 0
     * galleryclass : 4
     * id : 100
     * img : /ext/150728/d937d0a43006fbda2dc9615b5b538716.jpg
     * list : [{"gallery":100,"id":1435,"src":"/ext/150728/d937d0a43006fbda2dc9615b5b538716.jpg"},{"gallery":100,"id":1436,"src":"/ext/150728/620b0a20feb1357ad6ea20cd1c2afe7e.jpg"},{"gallery":100,"id":1437,"src":"/ext/150728/21a5f7a8c5a9d9a433a58a18e37b3884.jpg"},{"gallery":100,"id":1438,"src":"/ext/150728/9c06dd8c5c8f5c087d77b1abbf236f10.jpg"},{"gallery":100,"id":1439,"src":"/ext/150728/42ecea5064e41df136df25949f67f5d4.jpg"},{"gallery":100,"id":1440,"src":"/ext/150728/20eda1f23699a11285122e022dc599e4.jpg"},{"gallery":100,"id":1441,"src":"/ext/150728/646b2df4e333bb953e1e27ecf83630db.jpg"},{"gallery":100,"id":1442,"src":"/ext/150728/f8447cb62a5858e95e752ef7a2e66de5.jpg"},{"gallery":100,"id":1443,"src":"/ext/150728/1a4128b78411a4499515ccc71b9f2238.jpg"},{"gallery":100,"id":1444,"src":"/ext/150728/3b33d07f5e29579b3703da861586c828.jpg"},{"gallery":100,"id":1445,"src":"/ext/150728/a7a69a1fa664c4fa1e63744b661b67d0.jpg"},{"gallery":100,"id":1446,"src":"/ext/150728/211440dd6ab4f3bd8bf6e5bd1981b40d.jpg"},{"gallery":100,"id":1447,"src":"/ext/150728/9351a645001a4ee6e3c333a77676cb6f.jpg"}]
     * rcount : 0
     * size : 13
     * status : true
     * time : 1438080571000
     * title : 美貌苗条的身材那短裙包臀
     * url : http://www.tngou.net/tnfs/show/100
     */

    private int count;
    private int fcount;
    private int galleryclass;
    private int id;
    private String img;
    private int rcount;
    private int size;
    private boolean status;
    private long time;
    private String title;
    private String url;
    private List<ListEntity> list;

    public void setCount(int count) {
        this.count = count;
    }

    public void setFcount(int fcount) {
        this.fcount = fcount;
    }

    public void setGalleryclass(int galleryclass) {
        this.galleryclass = galleryclass;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setRcount(int rcount) {
        this.rcount = rcount;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setList(List<ListEntity> list) {
        this.list = list;
    }

    public int getCount() {
        return count;
    }

    public int getFcount() {
        return fcount;
    }

    public int getGalleryclass() {
        return galleryclass;
    }

    public int getId() {
        return id;
    }

    public String getImg() {
        return img;
    }

    public int getRcount() {
        return rcount;
    }

    public int getSize() {
        return size;
    }

    public boolean getStatus() {
        return status;
    }

    public long getTime() {
        return time;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public List<ListEntity> getList() {
        return list;
    }

    public static class ListEntity {
        /**
         * gallery : 100
         * id : 1435
         * src : /ext/150728/d937d0a43006fbda2dc9615b5b538716.jpg
         */

        private int gallery;
        private int id;
        private String src;

        public void setGallery(int gallery) {
            this.gallery = gallery;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setSrc(String src) {
            this.src = src;
        }

        public int getGallery() {
            return gallery;
        }

        public int getId() {
            return id;
        }

        public String getSrc() {
            return src;
        }
    }
}
