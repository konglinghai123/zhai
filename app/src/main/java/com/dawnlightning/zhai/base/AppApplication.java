package com.dawnlightning.zhai.base;

import java.io.File;

import com.dawnlightning.zhai.db.SQLHelper;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;


import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class AppApplication extends Application {
	private static AppApplication mAppApplication;
	private SQLHelper sqlHelper;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		initImageLoader(getApplicationContext());
		mAppApplication = this;
	}
	
	/** 获取Application */
	public static AppApplication getApp() {
		return mAppApplication;
	}
	
	/** 获取数据库Helper */
	public SQLHelper getSQLHelper() {
		if (sqlHelper == null)
			sqlHelper = new SQLHelper(mAppApplication);
		return sqlHelper;
	}
	
	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		if (sqlHelper != null)
			sqlHelper.close();
		super.onTerminate();
		//整体摧毁的时候调用这个方法
	}
	public static File cache(String name){
		File cacheDir=null;
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){

			String path="/storage/sdcard1";
//			String name="zhai/Cache";//你要新建的文件夹名或者文件名
			cacheDir=new File(path,name);//方法1和方法2的区别在于此
			boolean is=cacheDir.exists();//判断文件（夹）是否存在
			if(!is){
				cacheDir.mkdir();//创建文件夹
			}
			//存储在外置sd卡中
		}else{
			cacheDir=StorageUtils.getOwnCacheDirectory(getApp(),name);
		}
		return cacheDir;
	}
	/** 初始化ImageLoader */
	public static void initImageLoader(Context context) {


		//创建配置ImageLoader(所有的选项都是可选的,只使用那些你真的想定制)，这个可以设定在APPLACATION里面，设置为全局的配置参数
		ImageLoaderConfiguration config = new ImageLoaderConfiguration
				.Builder(context)
				//.memoryCacheExtraOptions(480, 800) // max width, max height，即保存的每个缓存文件的最大长宽
				//.discCacheExtraOptions(480, 800, CompressFormat.JPEG, 75, null) // Can slow ImageLoader, use it carefully (Better don't use it)设置缓存的详细信息，最好不要设置这个
				.threadPoolSize(5)//线程池内加载的数量
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				//.memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation你可以通过自己的内存缓存实现
				//.memoryCacheSize(5 * 1024 * 1024)
				//.discCacheSize(50 * 1024 * 1024)
				.discCacheFileNameGenerator(new Md5FileNameGenerator())//将保存的时候的URI名称用MD5 加密
				//.discCacheFileNameGenerator(new HashCodeFileNameGenerator())//将保存的时候的URI名称用HASHCODE加密
				.tasksProcessingOrder(QueueProcessingType.FIFO)
				.discCacheFileCount(500) //缓存的File数量
				.discCache(new UnlimitedDiscCache(cache("zhai/Cache")))//自定义缓存路径
				//.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				//.imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
				//.writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);//全局初始化此配置
	}
}
