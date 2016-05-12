package com.dawnlightning.zhai.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/5/12.
 */
public class BeiLaQiDetailedBean {

    /**
     * status : 1
     * data : [{"url":"http://p81.beilaqi.com:832/uploads/allimg/20150730/pyyruohuued.jpg","albumId":"66711","width":"0","height":"0"},{"url":"http://p81.beilaqi.com:832/uploads/allimg/20150730/q0rbipaj0gq.jpg","albumId":"66711","width":"0","height":"0"},{"url":"http://p81.beilaqi.com:832/uploads/allimg/20150730/nappaglgbeq.jpg","albumId":"66711","width":"0","height":"0"},{"url":"http://p81.beilaqi.com:832/uploads/allimg/20150730/wgrfvza3scn.jpg","albumId":"66711","width":"0","height":"0"},{"url":"http://p81.beilaqi.com:832/uploads/allimg/20150730/zc1xpr03dvt.jpg","albumId":"66711","width":"0","height":"0"},{"url":"http://p81.beilaqi.com:832/uploads/allimg/20150730/30izuixvolt.jpg","albumId":"66711","width":"0","height":"0"},{"url":"http://p81.beilaqi.com:832/uploads/allimg/20150730/uu4r5xscgl4.jpg","albumId":"66711","width":"0","height":"0"},{"url":"http://p81.beilaqi.com:832/uploads/allimg/20150730/k22ddnanirn.jpg","albumId":"66711","width":"0","height":"0"},{"url":"http://p81.beilaqi.com:832/uploads/allimg/20150730/wpvbxin51ul.jpg","albumId":"66711","width":"0","height":"0"},{"url":"http://p81.beilaqi.com:832/uploads/allimg/20150730/r5gylstww03.jpg","albumId":"66711","width":"0","height":"0"},{"url":"http://p81.beilaqi.com:832/uploads/allimg/20150730/3ktlscdvdnp.jpg","albumId":"66711","width":"0","height":"0"},{"url":"http://p81.beilaqi.com:832/uploads/allimg/20150730/jv2fmx4xspd.jpg","albumId":"66711","width":"0","height":"0"},{"url":"http://p81.beilaqi.com:832/uploads/allimg/20150730/xr4dsk311h3.jpg","albumId":"66711","width":"0","height":"0"},{"url":"http://p81.beilaqi.com:832/uploads/allimg/20150730/0mxrerdb0iz.jpg","albumId":"66711","width":"0","height":"0"},{"url":"http://p81.beilaqi.com:832/uploads/allimg/20150730/rrr0ljz5f4a.jpg","albumId":"66711","width":"0","height":"0"},{"url":"http://p81.beilaqi.com:832/uploads/allimg/20150730/hvnt1ttslgk.jpg","albumId":"66711","width":"0","height":"0"},{"url":"http://p81.beilaqi.com:832/uploads/allimg/20150730/iyuqjq4mfx4.jpg","albumId":"66711","width":"0","height":"0"},{"url":"http://p81.beilaqi.com:832/uploads/allimg/20150730/q0w150ghg0z.jpg","albumId":"66711","width":"0","height":"0"},{"url":"http://p81.beilaqi.com:832/uploads/allimg/20150730/aqhziikk4yj.jpg","albumId":"66711","width":"0","height":"0"},{"url":"http://p81.beilaqi.com:832/uploads/allimg/20150730/ewjy0eyivi3.jpg","albumId":"66711","width":"0","height":"0"},{"url":"http://p81.beilaqi.com:832/uploads/allimg/20150730/lgqzwithqna.jpg","albumId":"66711","width":"0","height":"0"},{"url":"http://p81.beilaqi.com:832/uploads/allimg/20150730/hsdohzf51pj.jpg","albumId":"66711","width":"0","height":"0"},{"url":"http://p81.beilaqi.com:832/uploads/allimg/20150730/wnvfsiufan3.jpg","albumId":"66711","width":"0","height":"0"}]
     */

    private String status;
    private List<DataEntity> data;

    public void setStatus(String status) {
        this.status = status;
    }

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public List<DataEntity> getData() {
        return data;
    }

    public static class DataEntity {
        /**
         * url : http://p81.beilaqi.com:832/uploads/allimg/20150730/pyyruohuued.jpg
         * albumId : 66711
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
}
