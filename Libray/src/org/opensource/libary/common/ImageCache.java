package org.opensource.libary.common;

import android.graphics.Bitmap;

/** 图片缓存要实现的接口 **/
public interface ImageCache {
	
	/**取得一张缓存图片的bitmap*/
	public Bitmap getBitmap(String url);
	
	/**往缓存空间里面存入bitmap*/
	public void putBitmap(String url, Bitmap bitmap);
}
