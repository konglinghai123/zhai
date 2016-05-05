/**
 * Copyright (c) 2012-2013, Michael Yang 鏉ㄧ娴? (www.yangfuhai.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dawnlightning.zhai.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * @author Michael Yang锛坵ww.yangfuhai.com锛? update at 2013.08.07
 */
public class ACache {
	public static final int TIME_HOUR = 60 * 60;
	public static final int TIME_DAY = TIME_HOUR * 24;
	private static final int MAX_SIZE = 1000 * 1000 * 50; // 50 mb
	private static final int MAX_COUNT = Integer.MAX_VALUE; // 涓嶉檺鍒跺瓨鏀炬暟鎹殑鏁伴噺
	private static Map<String, ACache> mInstanceMap = new HashMap<String, ACache>();
	private ACacheManager mCache;

	public static ACache get(Context ctx) {
		return get(ctx, "ACache");
	}

	public static ACache get(Context ctx, String cacheName) {
		File f = new File(ctx.getCacheDir(), cacheName);
		return get(f, MAX_SIZE, MAX_COUNT);
	}

	public static ACache get(File cacheDir) {
		return get(cacheDir, MAX_SIZE, MAX_COUNT);
	}

	public static ACache get(Context ctx, long max_zise, int max_count) {
		File f = new File(ctx.getCacheDir(), "ACache");
		return get(f, max_zise, max_count);
	}

	public static ACache get(File cacheDir, long max_zise, int max_count) {
		ACache manager = mInstanceMap.get(cacheDir.getAbsoluteFile() + myPid());
		if (manager == null) {
			manager = new ACache(cacheDir, max_zise, max_count);
			mInstanceMap.put(cacheDir.getAbsolutePath() + myPid(), manager);
		}
		return manager;
	}

	private static String myPid() {
		return "_" + android.os.Process.myPid();
	}

	private ACache(File cacheDir, long max_size, int max_count) {
		if (!cacheDir.exists() && !cacheDir.mkdirs()) {
			throw new RuntimeException("can't make dirs in "
					+ cacheDir.getAbsolutePath());
		}
		mCache = new ACacheManager(cacheDir, max_size, max_count);
	}

	// =======================================
	// ============ String鏁版嵁 璇诲啓 ==============
	// =======================================
	/**
	 * 淇濆瓨 String鏁版嵁 鍒? 缂撳瓨涓?
	 * 
	 * @param key
	 *            淇濆瓨鐨刱ey
	 * @param value
	 *            淇濆瓨鐨凷tring鏁版嵁
	 */
	public void put(String key, String value) {
		File file = mCache.newFile(key);
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new FileWriter(file), 1024);
			out.write(value);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.flush();
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			mCache.put(file);
		}
	}

	/**
	 * 淇濆瓨 String鏁版嵁 鍒? 缂撳瓨涓?
	 * 
	 * @param key
	 *            淇濆瓨鐨刱ey
	 * @param value
	 *            淇濆瓨鐨凷tring鏁版嵁
	 * @param saveTime
	 *            淇濆瓨鐨勬椂闂达紝鍗曚綅锛氱
	 */
	public void put(String key, String value, int saveTime) {
		put(key, Utils.newStringWithDateInfo(saveTime, value));
	}

	/**
	 * 璇诲彇 String鏁版嵁
	 * 
	 * @param key
	 * @return String 鏁版嵁
	 */
	public String getAsString(String key) {
		File file = mCache.get(key);
		if (!file.exists())
			return null;
		boolean removeFile = false;
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(file));
			String readString = "";
			String currentLine;
			while ((currentLine = in.readLine()) != null) {
				readString += currentLine;
			}
			if (!Utils.isDue(readString)) {
				return Utils.clearDateInfo(readString);
			} else {
				removeFile = true;
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (removeFile)
				remove(key);
		}
	}

	// =======================================
	// ============= JSONObject 鏁版嵁 璇诲啓 ==============
	// =======================================
	/**
	 * 淇濆瓨 JSONObject鏁版嵁 鍒? 缂撳瓨涓?
	 * 
	 * @param key
	 *            淇濆瓨鐨刱ey
	 * @param value
	 *            淇濆瓨鐨凧SON鏁版嵁
	 */
	public void put(String key, JSONObject value) {
		put(key, value.toString());
	}

	/**
	 * 淇濆瓨 JSONObject鏁版嵁 鍒? 缂撳瓨涓?
	 * 
	 * @param key
	 *            淇濆瓨鐨刱ey
	 * @param value
	 *            淇濆瓨鐨凧SONObject鏁版嵁
	 * @param saveTime
	 *            淇濆瓨鐨勬椂闂达紝鍗曚綅锛氱
	 */
	public void put(String key, JSONObject value, int saveTime) {
		put(key, value.toString(), saveTime);
	}

	/**
	 * 璇诲彇JSONObject鏁版嵁
	 * 
	 * @param key
	 * @return JSONObject鏁版嵁
	 */
	public JSONObject getAsJSONObject(String key) {
		String JSONString = getAsString(key);
		try {
			JSONObject obj = new JSONObject(JSONString);
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// =======================================
	// ============ JSONArray 鏁版嵁 璇诲啓 =============
	// =======================================
	/**
	 * 淇濆瓨 JSONArray鏁版嵁 鍒? 缂撳瓨涓?
	 * 
	 * @param key
	 *            淇濆瓨鐨刱ey
	 * @param value
	 *            淇濆瓨鐨凧SONArray鏁版嵁
	 */
	public void put(String key, JSONArray value) {
		put(key, value.toString());
	}

	/**
	 * 淇濆瓨 JSONArray鏁版嵁 鍒? 缂撳瓨涓?
	 * 
	 * @param key
	 *            淇濆瓨鐨刱ey
	 * @param value
	 *            淇濆瓨鐨凧SONArray鏁版嵁
	 * @param saveTime
	 *            淇濆瓨鐨勬椂闂达紝鍗曚綅锛氱
	 */
	public void put(String key, JSONArray value, int saveTime) {
		put(key, value.toString(), saveTime);
	}

	/**
	 * 璇诲彇JSONArray鏁版嵁
	 * 
	 * @param key
	 * @return JSONArray鏁版嵁
	 */
	public JSONArray getAsJSONArray(String key) {
		String JSONString = getAsString(key);
		try {
			JSONArray obj = new JSONArray(JSONString);
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// =======================================
	// ============== byte 鏁版嵁 璇诲啓 =============
	// =======================================
	/**
	 * 淇濆瓨 byte鏁版嵁 鍒? 缂撳瓨涓?
	 * 
	 * @param key
	 *            淇濆瓨鐨刱ey
	 * @param value
	 *            淇濆瓨鐨勬暟鎹?
	 */
	public void put(String key, byte[] value) {
		File file = mCache.newFile(key);
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file);
			out.write(value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.flush();
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			mCache.put(file);
		}
	}

	/**
	 * 淇濆瓨 byte鏁版嵁 鍒? 缂撳瓨涓?
	 * 
	 * @param key
	 *            淇濆瓨鐨刱ey
	 * @param value
	 *            淇濆瓨鐨勬暟鎹?
	 * @param saveTime
	 *            淇濆瓨鐨勬椂闂达紝鍗曚綅锛氱
	 */
	public void put(String key, byte[] value, int saveTime) {
		put(key, Utils.newByteArrayWithDateInfo(saveTime, value));
	}

	/**
	 * 鑾峰彇 byte 鏁版嵁
	 * 
	 * @param key
	 * @return byte 鏁版嵁
	 */
	public byte[] getAsBinary(String key) {
		RandomAccessFile RAFile = null;
		boolean removeFile = false;
		try {
			File file = mCache.get(key);
			if (!file.exists())
				return null;
			RAFile = new RandomAccessFile(file, "r");
			byte[] byteArray = new byte[(int) RAFile.length()];
			RAFile.read(byteArray);
			if (!Utils.isDue(byteArray)) {
				return Utils.clearDateInfo(byteArray);
			} else {
				removeFile = true;
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (RAFile != null) {
				try {
					RAFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (removeFile)
				remove(key);
		}
	}

	// =======================================
	// ============= 搴忓垪鍖? 鏁版嵁 璇诲啓 ===============
	// =======================================
	/**
	 * 淇濆瓨 Serializable鏁版嵁 鍒? 缂撳瓨涓?
	 * 
	 * @param key
	 *            淇濆瓨鐨刱ey
	 * @param value
	 *            淇濆瓨鐨剉alue
	 */
	public void put(String key, Serializable value) {
		put(key, value, -1);
	}

	/**
	 * 淇濆瓨 Serializable鏁版嵁鍒? 缂撳瓨涓?
	 * 
	 * @param key
	 *            淇濆瓨鐨刱ey
	 * @param value
	 *            淇濆瓨鐨剉alue
	 * @param saveTime
	 *            淇濆瓨鐨勬椂闂达紝鍗曚綅锛氱
	 */
	public void put(String key, Serializable value, int saveTime) {
		ByteArrayOutputStream baos = null;
		ObjectOutputStream oos = null;
		try {
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(value);
			byte[] data = baos.toByteArray();
			if (saveTime != -1) {
				put(key, data, saveTime);
			} else {
				put(key, data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				oos.close();
			} catch (IOException e) {
			}
		}
	}

	/**
	 * 璇诲彇 Serializable鏁版嵁
	 * 
	 * @param key
	 * @return Serializable 鏁版嵁
	 */
	public Object getAsObject(String key) {
		byte[] data = getAsBinary(key);
		if (data != null) {
			ByteArrayInputStream bais = null;
			ObjectInputStream ois = null;
			try {
				bais = new ByteArrayInputStream(data);
				ois = new ObjectInputStream(bais);
				Object reObject = ois.readObject();
				return reObject;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			} finally {
				try {
					if (bais != null)
						bais.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					if (ois != null)
						ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;

	}

	// =======================================
	// ============== bitmap 鏁版嵁 璇诲啓 =============
	// =======================================
	/**
	 * 淇濆瓨 bitmap 鍒? 缂撳瓨涓?
	 * 
	 * @param key
	 *            淇濆瓨鐨刱ey
	 * @param value
	 *            淇濆瓨鐨刡itmap鏁版嵁
	 */
	public void put(String key, Bitmap value) {
		put(key, Utils.Bitmap2Bytes(value));
	}

	/**
	 * 淇濆瓨 bitmap 鍒? 缂撳瓨涓?
	 * 
	 * @param key
	 *            淇濆瓨鐨刱ey
	 * @param value
	 *            淇濆瓨鐨? bitmap 鏁版嵁
	 * @param saveTime
	 *            淇濆瓨鐨勬椂闂达紝鍗曚綅锛氱
	 */
	public void put(String key, Bitmap value, int saveTime) {
		put(key, Utils.Bitmap2Bytes(value), saveTime);
	}

	/**
	 * 璇诲彇 bitmap 鏁版嵁
	 * 
	 * @param key
	 * @return bitmap 鏁版嵁
	 */
	public Bitmap getAsBitmap(String key) {
		if (getAsBinary(key) == null) {
			return null;
		}
		return Utils.Bytes2Bimap(getAsBinary(key));
	}

	// =======================================
	// ============= drawable 鏁版嵁 璇诲啓 =============
	// =======================================
	/**
	 * 淇濆瓨 drawable 鍒? 缂撳瓨涓?
	 * 
	 * @param key
	 *            淇濆瓨鐨刱ey
	 * @param value
	 *            淇濆瓨鐨刣rawable鏁版嵁
	 */
	public void put(String key, Drawable value) {
		put(key, Utils.drawable2Bitmap(value));
	}

	/**
	 * 淇濆瓨 drawable 鍒? 缂撳瓨涓?
	 * 
	 * @param key
	 *            淇濆瓨鐨刱ey
	 * @param value
	 *            淇濆瓨鐨? drawable 鏁版嵁
	 * @param saveTime
	 *            淇濆瓨鐨勬椂闂达紝鍗曚綅锛氱
	 */
	public void put(String key, Drawable value, int saveTime) {
		put(key, Utils.drawable2Bitmap(value), saveTime);
	}

	/**
	 * 璇诲彇 Drawable 鏁版嵁
	 * 
	 * @param key
	 * @return Drawable 鏁版嵁
	 */
	public Drawable getAsDrawable(String key) {
		if (getAsBinary(key) == null) {
			return null;
		}
		return Utils.bitmap2Drawable(Utils.Bytes2Bimap(getAsBinary(key)));
	}

	/**
	 * 鑾峰彇缂撳瓨鏂囦欢
	 * 
	 * @param key
	 * @return value 缂撳瓨鐨勬枃浠?
	 */
	public File file(String key) {
		File f = mCache.newFile(key);
		if (f.exists())
			return f;
		return null;
	}

	/**
	 * 绉婚櫎鏌愪釜key
	 * 
	 * @param key
	 * @return 鏄惁绉婚櫎鎴愬姛
	 */
	public boolean remove(String key) {
		return mCache.remove(key);
	}

	/**
	 * 娓呴櫎鎵?鏈夋暟鎹?
	 */
	public void clear() {
		mCache.clear();
	}

	/**
	 * @title 缂撳瓨绠＄悊鍣?
	 * @author 鏉ㄧ娴凤紙michael锛? www.yangfuhai.com
	 * @version 1.0
	 */
	public class ACacheManager {
		private final AtomicLong cacheSize;
		private final AtomicInteger cacheCount;
		private final long sizeLimit;
		private final int countLimit;
		private final Map<File, Long> lastUsageDates = Collections
				.synchronizedMap(new HashMap<File, Long>());
		protected File cacheDir;

		private ACacheManager(File cacheDir, long sizeLimit, int countLimit) {
			this.cacheDir = cacheDir;
			this.sizeLimit = sizeLimit;
			this.countLimit = countLimit;
			cacheSize = new AtomicLong();
			cacheCount = new AtomicInteger();
			calculateCacheSizeAndCacheCount();
		}

		/**
		 * 璁＄畻 cacheSize鍜宑acheCount
		 */
		private void calculateCacheSizeAndCacheCount() {
			new Thread(new Runnable() {
				@Override
				public void run() {
					int size = 0;
					int count = 0;
					File[] cachedFiles = cacheDir.listFiles();
					if (cachedFiles != null) {
						for (File cachedFile : cachedFiles) {
							size += calculateSize(cachedFile);
							count += 1;
							lastUsageDates.put(cachedFile,
									cachedFile.lastModified());
						}
						cacheSize.set(size);
						cacheCount.set(count);
					}
				}
			}).start();
		}

		private void put(File file) {
			int curCacheCount = cacheCount.get();
			while (curCacheCount + 1 > countLimit) {
				long freedSize = removeNext();
				cacheSize.addAndGet(-freedSize);

				curCacheCount = cacheCount.addAndGet(-1);
			}
			cacheCount.addAndGet(1);

			long valueSize = calculateSize(file);
			long curCacheSize = cacheSize.get();
			while (curCacheSize + valueSize > sizeLimit) {
				long freedSize = removeNext();
				curCacheSize = cacheSize.addAndGet(-freedSize);
			}
			cacheSize.addAndGet(valueSize);

			Long currentTime = System.currentTimeMillis();
			file.setLastModified(currentTime);
			lastUsageDates.put(file, currentTime);
		}

		private File get(String key) {
			File file = newFile(key);
			Long currentTime = System.currentTimeMillis();
			file.setLastModified(currentTime);
			lastUsageDates.put(file, currentTime);

			return file;
		}

		private File newFile(String key) {
			return new File(cacheDir, key.hashCode() + "");
		}

		private boolean remove(String key) {
			File image = get(key);
			return image.delete();
		}

		private void clear() {
			lastUsageDates.clear();
			cacheSize.set(0);
			File[] files = cacheDir.listFiles();
			if (files != null) {
				for (File f : files) {
					f.delete();
				}
			}
		}

		/**
		 * 绉婚櫎鏃х殑鏂囦欢
		 * 
		 * @return
		 */
		private long removeNext() {
			if (lastUsageDates.isEmpty()) {
				return 0;
			}

			Long oldestUsage = null;
			File mostLongUsedFile = null;
			Set<Entry<File, Long>> entries = lastUsageDates.entrySet();
			synchronized (lastUsageDates) {
				for (Entry<File, Long> entry : entries) {
					if (mostLongUsedFile == null) {
						mostLongUsedFile = entry.getKey();
						oldestUsage = entry.getValue();
					} else {
						Long lastValueUsage = entry.getValue();
						if (lastValueUsage < oldestUsage) {
							oldestUsage = lastValueUsage;
							mostLongUsedFile = entry.getKey();
						}
					}
				}
			}

			long fileSize = calculateSize(mostLongUsedFile);
			if (mostLongUsedFile.delete()) {
				lastUsageDates.remove(mostLongUsedFile);
			}
			return fileSize;
		}

		private long calculateSize(File file) {
			return file.length();
		}
	}

	/**
	 * @title 鏃堕棿璁＄畻宸ュ叿绫?
	 * @author 鏉ㄧ娴凤紙michael锛? www.yangfuhai.com
	 * @version 1.0
	 */
	private static class Utils {

		/**
		 * 鍒ゆ柇缂撳瓨鐨凷tring鏁版嵁鏄惁鍒版湡
		 * 
		 * @param str
		 * @return true锛氬埌鏈熶簡 false锛氳繕娌℃湁鍒版湡
		 */
		private static boolean isDue(String str) {
			return isDue(str.getBytes());
		}

		/**
		 * 鍒ゆ柇缂撳瓨鐨刡yte鏁版嵁鏄惁鍒版湡
		 * 
		 * @param data
		 * @return true锛氬埌鏈熶簡 false锛氳繕娌℃湁鍒版湡
		 */
		private static boolean isDue(byte[] data) {
			String[] strs = getDateInfoFromDate(data);
			if (strs != null && strs.length == 2) {
				String saveTimeStr = strs[0];
				while (saveTimeStr.startsWith("0")) {
					saveTimeStr = saveTimeStr
							.substring(1, saveTimeStr.length());
				}
				long saveTime = Long.valueOf(saveTimeStr);
				long deleteAfter = Long.valueOf(strs[1]);
				if (System.currentTimeMillis() > saveTime + deleteAfter * 1000) {
					return true;
				}
			}
			return false;
		}

		private static String newStringWithDateInfo(int second, String strInfo) {
			return createDateInfo(second) + strInfo;
		}

		private static byte[] newByteArrayWithDateInfo(int second, byte[] data2) {
			byte[] data1 = createDateInfo(second).getBytes();
			byte[] retdata = new byte[data1.length + data2.length];
			System.arraycopy(data1, 0, retdata, 0, data1.length);
			System.arraycopy(data2, 0, retdata, data1.length, data2.length);
			return retdata;
		}

		private static String clearDateInfo(String strInfo) {
			if (strInfo != null && hasDateInfo(strInfo.getBytes())) {
				strInfo = strInfo.substring(strInfo.indexOf(mSeparator) + 1,
						strInfo.length());
			}
			return strInfo;
		}

		private static byte[] clearDateInfo(byte[] data) {
			if (hasDateInfo(data)) {
				return copyOfRange(data, indexOf(data, mSeparator) + 1,
						data.length);
			}
			return data;
		}

		private static boolean hasDateInfo(byte[] data) {
			return data != null && data.length > 15 && data[13] == '-'
					&& indexOf(data, mSeparator) > 14;
		}

		private static String[] getDateInfoFromDate(byte[] data) {
			if (hasDateInfo(data)) {
				String saveDate = new String(copyOfRange(data, 0, 13));
				String deleteAfter = new String(copyOfRange(data, 14,
						indexOf(data, mSeparator)));
				return new String[] { saveDate, deleteAfter };
			}
			return null;
		}

		private static int indexOf(byte[] data, char c) {
			for (int i = 0; i < data.length; i++) {
				if (data[i] == c) {
					return i;
				}
			}
			return -1;
		}

		private static byte[] copyOfRange(byte[] original, int from, int to) {
			int newLength = to - from;
			if (newLength < 0)
				throw new IllegalArgumentException(from + " > " + to);
			byte[] copy = new byte[newLength];
			System.arraycopy(original, from, copy, 0,
					Math.min(original.length - from, newLength));
			return copy;
		}

		private static final char mSeparator = ' ';

		private static String createDateInfo(int second) {
			String currentTime = System.currentTimeMillis() + "";
			while (currentTime.length() < 13) {
				currentTime = "0" + currentTime;
			}
			return currentTime + "-" + second + mSeparator;
		}

		/*
		 * Bitmap 鈫? byte[]
		 */
		private static byte[] Bitmap2Bytes(Bitmap bm) {
			if (bm == null) {
				return null;
			}
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
			return baos.toByteArray();
		}

		/*
		 * byte[] 鈫? Bitmap
		 */
		private static Bitmap Bytes2Bimap(byte[] b) {
			if (b.length == 0) {
				return null;
			}
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		}

		/*
		 * Drawable 鈫? Bitmap
		 */
		private static Bitmap drawable2Bitmap(Drawable drawable) {
			if (drawable == null) {
				return null;
			}
			// 鍙? drawable 鐨勯暱瀹?
			int w = drawable.getIntrinsicWidth();
			int h = drawable.getIntrinsicHeight();
			// 鍙? drawable 鐨勯鑹叉牸寮?
			Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
					: Bitmap.Config.RGB_565;
			// 寤虹珛瀵瑰簲 bitmap
			Bitmap bitmap = Bitmap.createBitmap(w, h, config);
			// 寤虹珛瀵瑰簲 bitmap 鐨勭敾甯?
			Canvas canvas = new Canvas(bitmap);
			drawable.setBounds(0, 0, w, h);
			// 鎶? drawable 鍐呭鐢诲埌鐢诲竷涓?
			drawable.draw(canvas);
			return bitmap;
		}

		/*
		 * Bitmap 鈫? Drawable
		 */
		@SuppressWarnings("deprecation")
		private static Drawable bitmap2Drawable(Bitmap bm) {
			if (bm == null) {
				return null;
			}
			return new BitmapDrawable(bm);
		}
	}

}
