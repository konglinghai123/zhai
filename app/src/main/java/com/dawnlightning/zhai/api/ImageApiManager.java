package com.dawnlightning.zhai.api;




import android.text.TextUtils;
import android.util.Log;

import com.dawnlightning.zhai.base.AppApplication;
import com.dawnlightning.zhai.bean.BeiLaQiDetailedBean;
import com.dawnlightning.zhai.bean.BeiLaQiListBean;
import com.dawnlightning.zhai.bean.ImageDetailedBean;
import com.dawnlightning.zhai.bean.TianGouImagesBean;
import com.dawnlightning.zhai.bean.TianGouListBean;

import java.io.IOException;

import javax.security.auth.login.LoginException;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2016/5/11.
 */
public class ImageApiManager {
    private   String BaseUrl;
    private  Retrofit sRetrofit;
    private  ImageApiManagerService  apiManager;
    public final  static int  cachesize=10*1024*1024;
    public static OkHttpClient client = new OkHttpClient.Builder()
            .addNetworkInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    Response response= chain.proceed(request);
                    String cacheControl =request.cacheControl().toString();
                    if(TextUtils.isEmpty(cacheControl))
                    {
                        cacheControl ="public, max-age=86400 ,max-stale=2419200";
                    }
                    return response.newBuilder().header("Cache-Control", cacheControl) .removeHeader("Pragma") .build();
                }
            }).cache(new Cache(AppApplication.cache("zhai/Response"),cachesize)).build();
    public  ImageApiManager(String baseurl){
        this.BaseUrl=baseurl;
        sRetrofit=new Retrofit.Builder()
                .baseUrl(BaseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // 使用RxJava作为回调适配器
                .build();
         apiManager= sRetrofit.create(ImageApiManagerService.class);
    }


    public  Observable<TianGouListBean> getTianGouImageList(int page, int classifyid,int rows) {

        return apiManager.getTianGouImageList(page,classifyid, rows);
    }
    public Observable<BeiLaQiListBean> getBeiLaQiImageList(int page, int classifyid) {

        return apiManager.getBeiLaQiImageList(page, classifyid);
    }
    public Observable<ResponseBody> getUmeiImageList(int page){
        return apiManager.getumeiImageList(page);
    }
    public Observable<ResponseBody> getUmeiImagesDetailed(String url){
        return apiManager.getumeiImagesDetailed(url);
    }
    public Observable<TianGouImagesBean> getTianGouImageDetailed(int id){

        return apiManager.getTianGouImageDetailed(id);
    }
    public Observable<BeiLaQiDetailedBean> getBeiLaQiImageDetailed(int id){

        return apiManager.getBeiLaQiImageDetailed(id);
    }
    public interface ImageApiManagerService{
        @GET("/tnfs/api/list")
        Observable<TianGouListBean> getTianGouImageList(@Query("page") int page,@Query("id") int id,@Query("rows") int rows);

        @GET("/tnfs/api/show?")
        Observable<TianGouImagesBean> getTianGouImageDetailed(@Query("id") int id);


        //http://appd.beilaqi.com:832/action/iso_action.php?action=article&id=66711
        /*http://appd.beilaqi.com:832/action/iso_action.php?action=list&id=%s&page=%s*/
        @GET("/action/iso_action.php?action=list")
        Observable<BeiLaQiListBean> getBeiLaQiImageList(@Query("page") int page,@Query("id") int id);
        @GET("/action/iso_action.php?action=article")
        Observable<BeiLaQiDetailedBean> getBeiLaQiImageDetailed(@Query("id") int id);

        @GET("/p/gaoqing/index-{page}.htm")
        Observable<ResponseBody> getumeiImageList(@Path("page") int page);
        @GET("{path}")
        Observable<ResponseBody> getumeiImagesDetailed(@Path("path") String url);
    }


}
