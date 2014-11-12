package org.opensource.libary.common;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

import org.opensource.libary.utils.ConstansUtil;
import org.opensource.libary.utils.Println;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * 图片缓存助手类
 * 
 * @author fuqiang
 * @version 1.0
 * @created 2014/11/12
 */
public class CacheHelpher implements ImageCache {
	// 缓存助手类实例
	private static CacheHelpher sCacheHelpher = null;
	// 强引用
	private LruCache<String, Bitmap> mLruCache;
	// 软引用
	private Map<String, SoftReference<Bitmap>> mSoftCache;
	
	// 取得缓存助手的实例
	public static CacheHelpher getCacheHelpherInstance() {
		if (sCacheHelpher == null) {
			sCacheHelpher = new CacheHelpher();
		}
		return sCacheHelpher;
	}

	private CacheHelpher() {
		mSoftCache = new HashMap<String, SoftReference<Bitmap>>();
		mLruCache = new LruCache<String, Bitmap>(ConstansUtil.MAX_MEMORY_SIZE) {
			@Override
			protected void entryRemoved(boolean evicted, String key,
					Bitmap oldValue, Bitmap newValue) {
				SoftReference<Bitmap> soft = new SoftReference<Bitmap>(oldValue);
				// 添加到软引用中去
				mSoftCache.put(key, soft);
			}

			@Override
			protected int sizeOf(String key, Bitmap value) {
				// 以图片大小定义大小
				return value.getRowBytes() * value.getHeight() / 1024;
			}
		};
	}

	/**
	 * 从引用缓存中取出图片
	 */
	@Override
	public Bitmap getBitmap(String url) {
		SoftReference<Bitmap> soft = null;
		Bitmap bitmap = null;
		if (((bitmap = mLruCache.get(url)) != null)
				|| ((soft = mSoftCache.get(url)) != null)
				&& ((bitmap = soft.get()) != null)) {
			Println.debug("从内存中取");
			if (soft != null) {
				mLruCache.put(url, bitmap);// 添加到强引用缓存中去
				mSoftCache.remove(url);// 从软引用缓存中移除
			}
		}
		return bitmap;
	}

	/**
	 * 将图片添加到缓存引用中
	 */
	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		mLruCache.put(url, bitmap);// 添加到强引用缓存中
	}
}