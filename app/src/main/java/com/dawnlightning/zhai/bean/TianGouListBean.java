package com.dawnlightning.zhai.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/5/11.
 */
public class TianGouListBean {

    /**
     * status : true
     * total : 714
     * tngou : [{"count":4445,"fcount":0,"galleryclass":1,"id":728,"img":"/ext/160417/677ab7206386e96d45dfa67e719f4335.jpg","rcount":0,"size":6,"time":1460857234000,"title":"粉嫩少女"},{"count":2462,"fcount":0,"galleryclass":5,"id":727,"img":"/ext/160417/9f77927365e2fc34f895bfe373dc2af2.jpg","rcount":0,"size":7,"time":1460857120000,"title":"赵雨菲清纯白诱惑"},{"count":1451,"fcount":0,"galleryclass":4,"id":726,"img":"/ext/160417/fc9d6b190c5300159d82b4579378bcbf.jpg","rcount":0,"size":5,"time":1460857085000,"title":"媚娘花枝招展的青春"},{"count":1215,"fcount":0,"galleryclass":5,"id":725,"img":"/ext/160417/5980119433ef21b42e40358e26a6ede3.jpg","rcount":0,"size":6,"time":1460857039000,"title":"校花李岩西绝美诱惑组图"},{"count":677,"fcount":0,"galleryclass":4,"id":724,"img":"/ext/160415/e4b0f5a3cb74bdaa9c7e3e01186fa467.jpg","rcount":0,"size":6,"time":1460697398000,"title":"简洁的审美"},{"count":7693,"fcount":0,"galleryclass":6,"id":723,"img":"/ext/160415/11c1b32ba00145b351390941b97c0224.jpg","rcount":0,"size":8,"time":1460697305000,"title":"清纯女孩清新照"},{"count":420,"fcount":0,"galleryclass":4,"id":722,"img":"/ext/160415/35e4a2d4adbdfb61191d67748874c39a.jpg","rcount":0,"size":6,"time":1460697249000,"title":"清纯可人的小美女"},{"count":5404,"fcount":0,"galleryclass":6,"id":721,"img":"/ext/160408/e65ae3efd6d166c3c88e1679c7d5a383.jpg","rcount":0,"size":10,"time":1460114354000,"title":"小萝莉清纯照 "},{"count":3787,"fcount":0,"galleryclass":6,"id":720,"img":"/ext/160408/fcacfc698fc887758af9528c87e111ec.jpg","rcount":0,"size":9,"time":1460114312000,"title":"清凉秋日清纯少女"},{"count":2488,"fcount":0,"galleryclass":6,"id":719,"img":"/ext/160408/728c12c0936d89e4af50719aefd57611.jpg","rcount":0,"size":8,"time":1460114274000,"title":"90后卖萌女女 "}]
     */

    private boolean status;
    private int total;
    private List<TngouEntity> tngou;

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setTngou(List<TngouEntity> tngou) {
        this.tngou = tngou;
    }

    public boolean getStatus() {
        return status;
    }

    public int getTotal() {
        return total;
    }

    public List<TngouEntity> getTngou() {
        return tngou;
    }

    public static class TngouEntity {
        /**
         * count : 4445
         * fcount : 0
         * galleryclass : 1
         * id : 728
         * img : /ext/160417/677ab7206386e96d45dfa67e719f4335.jpg
         * rcount : 0
         * size : 6
         * time : 1460857234000
         * title : 粉嫩少女
         */

        private int count;
        private int fcount;
        private int galleryclass;
        private int id;
        private String img;
        private int rcount;
        private int size;
        private long time;
        private String title;

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

        public void setTime(long time) {
            this.time = time;
        }

        public void setTitle(String title) {
            this.title = title;
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

        public long getTime() {
            return time;
        }

        public String getTitle() {
            return title;
        }
    }
}
