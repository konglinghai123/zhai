package com.dawnlightning.zhai.utils;

import com.dawnlightning.zhai.base.AppApplication;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

public class AsyncHttp {
	private static AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
	private static  ACache mCache=ACache.get(AppApplication.getApp());
	public static void get(String url,RequestParams params,JsonHttpResponseHandler responseHandler){

		client.get(url, params, responseHandler);
	}
	public static void jsoupget(String url,RequestParams params,TextHttpResponseHandler textHttpResponseHandler){
		client.setURLEncodingEnabled(false);
		client.get(url,params,textHttpResponseHandler);
	}
	public static void post(String url,RequestParams params,JsonHttpResponseHandler responseHandler){
		client.post(url, params, responseHandler);
	}
	public static void stopresquest(){
		client.cancelAllRequests(true);
	}
	public static ACache getmCache(){
		return mCache;
	}
	public static void savecache(String url,String object,int duetime){
		mCache.put(url,object,duetime);
	}
	public static String getcache(String url){
		return mCache.getAsString(url);
	}
	public static void removecache(String url){
		mCache.remove(url);
	}

	public static void getnotice(String url,RequestParams params,JsonHttpResponseHandler responseHandler){
		client.setURLEncodingEnabled(false);
		client.get(url, params, responseHandler);
	}
	public static void getpic(String url,RequestParams params,TextHttpResponseHandler textHttpResponseHandler){
		client.get(url,params,textHttpResponseHandler);
	}
}

