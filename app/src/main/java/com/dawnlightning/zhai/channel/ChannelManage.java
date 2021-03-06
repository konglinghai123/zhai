package com.dawnlightning.zhai.channel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import android.database.SQLException;
import android.util.Log;

import com.dawnlightning.zhai.base.Classify;
import com.dawnlightning.zhai.dao.ChannelDao;
import com.dawnlightning.zhai.db.SQLHelper;


public class ChannelManage {
	public static ChannelManage channelManage;
	/**
	 * 默认的用户选择频道列表
	 * */
	public static List<ChannelItem> defaultUserChannels;
	/**
	 * 默认的其他频道列表
	 * */
	public static List<ChannelItem> defaultOtherChannels;
	private ChannelDao channelDao;
	/** 判断数据库中是否存在用户数据 */
	private boolean userExist = false;
	static {
		defaultUserChannels = new ArrayList<ChannelItem>();
		defaultOtherChannels = new ArrayList<ChannelItem>();
		defaultUserChannels.add(new ChannelItem(1, "首页", 14, 1, Classify.Home));
		defaultUserChannels.add(new ChannelItem(1, "性感美女1", 13, 1, Classify.ApiGrils));
		defaultUserChannels.add(new ChannelItem(2, "韩日美女1", 12, 1, Classify.ApiGrils));
		defaultUserChannels.add(new ChannelItem(3, "丝袜美腿1", 11, 1, Classify.ApiGrils));
		defaultUserChannels.add(new ChannelItem(4, "美女照片1", 10, 1, Classify.ApiGrils));
		defaultUserChannels.add(new ChannelItem(5, "美女写真1", 9, 1, Classify.ApiGrils));
		defaultUserChannels.add(new ChannelItem(6, "清纯美女1", 8, 1, Classify.ApiGrils));
		defaultUserChannels.add(new ChannelItem(7, "性感车模1", 7, 1, Classify.ApiGrils));
		defaultUserChannels.add(new ChannelItem(8, "专业腿模", 6, 1,  Classify.BeautyLeg));
		defaultUserChannels.add(new ChannelItem(10, "性感美女2",5 , 1, Classify.NewApiGrils));
		defaultUserChannels.add(new ChannelItem(11, "丝袜美女2", 4, 1, Classify.NewApiGrils));
		defaultUserChannels.add(new ChannelItem(15, "内衣美女2", 3, 1, Classify.NewApiGrils));
		defaultUserChannels.add(new ChannelItem(16, "清纯美女2", 2, 1, Classify.NewApiGrils));
		defaultUserChannels.add(new ChannelItem(17, "长腿美女2", 1, 1, Classify.NewApiGrils));




		/*
		*
		* "data": [
        {
            "id": "10",
            "name": "性感美女"
        },
        {
            "id": "11",
            "name": "丝袜美女"
        },
        {
            "id": "15",
            "name": "内衣美女"
        },
        {
            "id": "16",
            "name": "清纯美女"
        },
        {
            "id": "17",
            "name": "长腿美女"
        }
		* */
//		defaultOtherChannels.add(new ChannelItem(8, "财经", 1, 0));
//		defaultOtherChannels.add(new ChannelItem(9, "汽车", 2, 0));
//		defaultOtherChannels.add(new ChannelItem(10, "房产", 3, 0));
//		defaultOtherChannels.add(new ChannelItem(11, "社会", 4, 0));
//		defaultOtherChannels.add(new ChannelItem(12, "情感", 5, 0));
//		defaultOtherChannels.add(new ChannelItem(13, "女人", 6, 0));
//		defaultOtherChannels.add(new ChannelItem(14, "旅游", 7, 0));
//		defaultOtherChannels.add(new ChannelItem(15, "健康", 8, 0));
//		defaultOtherChannels.add(new ChannelItem(16, "美女", 9, 0));
//		defaultOtherChannels.add(new ChannelItem(17, "游戏", 10, 0));
//		defaultOtherChannels.add(new ChannelItem(18, "数码", 11, 0));
//		defaultUserChannels.add(new ChannelItem(19, "娱乐", 12, 0));


	}

	private ChannelManage(SQLHelper paramDBHelper) throws SQLException {
		if (channelDao == null)
			channelDao = new ChannelDao(paramDBHelper.getContext());
		// NavigateItemDao(paramDBHelper.getDao(NavigateItem.class));
		return;
	}

	/**
	 * 初始化频道管理类
	 * @paramparamDBHelper
	 * @throws SQLException
	 */
	public static ChannelManage getManage(SQLHelper dbHelper)throws SQLException {
		if (channelManage == null)
			channelManage = new ChannelManage(dbHelper);
		return channelManage;
	}

	/**
	 * 清除所有的频道
	 */
	public void deleteAllChannel() {
		channelDao.clearFeedTable();
	}
	/**
	 * 获取其他的频道
	 * @return 数据库存在用户配置 ? 数据库内的用户选择频道 : 默认用户选择频道 ;
	 */
	public List<ChannelItem> getUserChannel() {
		Object cacheList = channelDao.listCache(SQLHelper.SELECTED + "= ?",new String[] { "1" });
		if (cacheList != null && !((List) cacheList).isEmpty()) {
			userExist = true;
			List<Map<String, String>> maplist = (List) cacheList;
			int count = maplist.size();
			List<ChannelItem> list = new ArrayList<ChannelItem>();
			for (int i = 0; i < count; i++) {
				ChannelItem navigate = new ChannelItem();
				navigate.setId(Integer.valueOf(maplist.get(i).get(SQLHelper.ID)));
				navigate.setName(maplist.get(i).get(SQLHelper.NAME));
				navigate.setOrderId(Integer.valueOf(maplist.get(i).get(SQLHelper.ORDERID)));
				navigate.setSelected(Integer.valueOf(maplist.get(i).get(SQLHelper.SELECTED)));
				if (maplist.get(i).get(SQLHelper.CLASSIFY).equals("ApiGrils")){
					navigate.setClassify(Classify.ApiGrils);
				}else if(maplist.get(i).get(SQLHelper.CLASSIFY).equals("BeautyLeg")){
					navigate.setClassify(Classify.BeautyLeg);
				}else if (maplist.get(i).get(SQLHelper.CLASSIFY).equals("Home")){
					navigate.setClassify(Classify.Home);;
				}else if (maplist.get(i).get(SQLHelper.CLASSIFY).equals("News")){
					navigate.setClassify(Classify.News);
				}else if (maplist.get(i).get(SQLHelper.CLASSIFY).equals("Weather")){
					navigate.setClassify(Classify.Weather);
				}else if(maplist.get(i).get(SQLHelper.CLASSIFY).equals("NewApiGrils")){
					navigate.setClassify(Classify.NewApiGrils);
				}
				list.add(navigate);
			}
			return list;
		}
		initDefaultChannel();
		return defaultUserChannels;
	}
	
	/**
	 * 获取其他的频道
	 * @return 数据库存在用户配置 ? 数据库内的其它频道 : 默认其它频道 ;
	 */
	public List<ChannelItem> getOtherChannel() {
		Object cacheList = channelDao.listCache(SQLHelper.SELECTED + "= ?" ,new String[] { "0" });
		List<ChannelItem> list = new ArrayList<ChannelItem>();
		if (cacheList != null && !((List) cacheList).isEmpty()){
			List<Map<String, String>> maplist = (List) cacheList;
			int count = maplist.size();
			for (int i = 0; i < count; i++) {
				ChannelItem navigate= new ChannelItem();
				navigate.setId(Integer.valueOf(maplist.get(i).get(SQLHelper.ID)));
				navigate.setName(maplist.get(i).get(SQLHelper.NAME));
				navigate.setOrderId(Integer.valueOf(maplist.get(i).get(SQLHelper.ORDERID)));
				navigate.setSelected(Integer.valueOf(maplist.get(i).get(SQLHelper.SELECTED)));
				list.add(navigate);
			}
			return list;
		}
		if(userExist){
			return list;
		}
		cacheList = defaultOtherChannels;
		return (List<ChannelItem>) cacheList;
	}
	
	/**
	 * 保存用户频道到数据库
	 * @param userList
	 */
	public void saveUserChannel(List<ChannelItem> userList) {
		for (int i = 0; i < userList.size(); i++) {
			ChannelItem channelItem = (ChannelItem) userList.get(i);
			channelItem.setOrderId(i);
			channelItem.setSelected(Integer.valueOf(1));
			channelDao.addCache(channelItem);
		}
	}
	
	/**
	 * 保存其他频道到数据库
	 * @param otherList
	 */
	public void saveOtherChannel(List<ChannelItem> otherList) {
		for (int i = 0; i < otherList.size(); i++) {
			ChannelItem channelItem = (ChannelItem) otherList.get(i);
			channelItem.setOrderId(i);
			channelItem.setSelected(Integer.valueOf(0));
			channelDao.addCache(channelItem);
		}
	}
	
	/**
	 * 初始化数据库内的频道数据
	 */
	private void initDefaultChannel(){
		Log.d("deleteAll", "deleteAll");
		deleteAllChannel();
		saveUserChannel(defaultUserChannels);
		saveOtherChannel(defaultOtherChannels);
	}
}
