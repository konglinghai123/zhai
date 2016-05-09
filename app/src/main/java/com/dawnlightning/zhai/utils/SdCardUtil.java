package com.dawnlightning.zhai.utils;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.io.InputStream;
public class SdCardUtil {

	
	//项目文件根目录
	public static final String FILEDIR="/lightup";
	
	//照相机照片目录
	public static final String FILEPHOTO="/photos";
	
	//应用程序图片存放
	public static final String FILEAPK="apk";
	
	//应用程序缓存
	public static final String FILECACHE="cache";
	
	//用户信息目录
	public static final String FILEUSER="user";
	
	/**
	 * 检测sd卡是否可以用
	 * 
	 * @return
	 */
	public static boolean checkSdCard() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			// sd card 可用
			return true;
		} else {
			// 当前不可用
			return false;
		}
	}

	/**
	 * 获取sd卡文件路径
	 * 
	 * @return
	 */
	public static String getSdPath() {
		return Environment.getExternalStorageDirectory()+"";
	}

	/**
	 * 创建一个项目文件夹
	 * 
	 * @param fileDir
	 *            文件目录名
	 */
	public static void createFileDir(String fileDir) {
		String path = getSdPath() + fileDir;
		File path1 = new File(path);
		if (!path1.exists()) {
			path1.mkdirs();
			
		}
	}
	public static File updateDir = null;
	public static File updateFile = null;
	public static boolean isCreateFileSucess;

	/**
	 * ����������createFile����
	 * @param
	 * @return
	 * @seeFileUtil
	 */
	public static void createFile(String app_name) {

		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			isCreateFileSucess = true;

			updateDir = new File(Environment.getExternalStorageDirectory()+"/lightup"+ "/" + FILEAPK +"/");
			updateFile = new File(updateDir + "/" + app_name + ".apk");

			if (!updateDir.exists()) {
				updateDir.mkdirs();
			}
			if (!updateFile.exists()) {
				try {
					updateFile.createNewFile();
				} catch (IOException e) {
					isCreateFileSucess = false;
					e.printStackTrace();
				}
			}

		}else{
			isCreateFileSucess = false;
		}
	}
	/**
	 * 获取外置SD卡路径
	 * @return	应该就一条记录或空
	 */
	public static  List getExtSDCardPath()
	{
		List lResult = new ArrayList();
		try {
			Runtime rt = Runtime.getRuntime();
			Process proc = rt.exec("mount");
			InputStream is = proc.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line;
			while ((line = br.readLine()) != null) {
				if (line.contains("extSdCard"))
				{
					String [] arr = line.split(" ");
					String path = arr[1];
					File file = new File(path);
					if (file.isDirectory())
					{
						lResult.add(path);
					}
				}
			}
			isr.close();
		} catch (Exception e) {
		}
		return lResult;
	}


}
