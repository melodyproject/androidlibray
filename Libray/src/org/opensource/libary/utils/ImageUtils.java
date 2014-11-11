package org.opensource.libary.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

/**
 * 图片进行二次采样工具类
 * <ol>
 * <li>ALPHA_8：每个像素占用1byte内存</li>
 * <li>ARGB_4444:每个像素占用2byte内存</li>
 * <li>ARGB_8888:每个像素占用4byte内存</li>
 * <li>RGB_565:每个像素占用2byte内存</li>
 * </ol>
 * 
 * @author fuqiang
 */
public class ImageUtils {
	
	/**
	 * 取得采样的比例
	 * 
	 * @param in
	 *            InputStream
	 * @param defaultW
	 *            宽度(可以是屏幕的宽度,也可以是自己设定的宽度)
	 * @param defualtH
	 *            高度(同上....)
	 * @return
	 */
	public static int getSampleSize(InputStream in, int defaultW, int defualtH) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		// 如果为true允许查询图片,不是按照像素分配给内存的。
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(in, null, options);
		int bitmapW = options.outWidth;
		int bitmapH = options.outHeight;
		int sample = handleSampleSize(defaultW, defualtH, bitmapW, bitmapH);
		return sample;
	}
	
	/**
	 * 取得采样之后的bitmap
	 * 
	 * @param in
	 *            InputStream
	 * @param sampleSize
	 *            采样比例
	 * @return
	 */
	public static Bitmap subSampleBitmap(InputStream in, int sampleSize) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = false;
		options.inSampleSize = sampleSize;
		Bitmap bitmap = BitmapFactory.decodeStream(in, null, options);
		return bitmap;
	}
	
	/**
	 * 处理取得采样比例
	 * 
	 * @param defaultW
	 * @param defualtH
	 * @param bitmapW
	 * @param bitmapH
	 * @return
	 */
	private static int handleSampleSize(int defaultW, int defualtH,
			int bitmapW, int bitmapH) {
		int sample = 1;
		while ((bitmapW / defaultW > sample) || (bitmapH / defualtH > sample)) {
			sample *= 2;
		}
		return sample;
	}
	
	/**
	 * 读取资源图片
	 * 
	 * @param context
	 * @param resId
	 * @return
	 */
	public static Bitmap readBitMap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	/**
	 * 以最省内存的方式读取图片
	 */
	public static Bitmap readBitMap(final String path) {
		try {
			FileInputStream stream = new FileInputStream(new File(path));
			BitmapFactory.Options opts = new BitmapFactory.Options();
		    opts.inPreferredConfig=Bitmap.Config.RGB_565;
			//opts.inSampleSize = 8;
			// 当isPurgable设为true时，系统中内存不足时，可以回收部分Bitmap占据的内存空间，这时一般不会出现OutOfMemory错误。
			opts.inPurgeable = true;
			
			// 与inPurgeable一起使用;
			// 如果inPurgeable为false那该设置将被忽略;
			// 如果为true，那么它可以决定位图是否能够共享一个指向数据源的引用，或者是进行一份拷贝;
			opts.inInputShareable = true;
			
			Bitmap bitmap = BitmapFactory.decodeStream(stream, null, opts);
			
			return bitmap;
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 压缩图片
	 * */
	public static Bitmap zoomImage(Bitmap bm, double newWidth, double newHeight) {
		// 获取这个图片的宽和高
		float width = bm.getWidth();
		float height = bm.getHeight();
		// 创建操作图片用的matrix对象
		Matrix matrix = new Matrix();
		// 计算宽高缩放率
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 缩放图片动作
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap bitmap = Bitmap.createBitmap(bm, 0, 0, (int) width,
				(int) height, matrix, true);
		return bitmap;
	}
	
	/**
	 * 取得图片字节数组
	 * @param bitmap
	 * @return byte[]
	 */
	public static byte[] getPicBytes(Bitmap bitmap) {
		ByteArrayOutputStream baos =null;
		byte[] bytes=null;
		if(null!=bitmap){
			baos = new ByteArrayOutputStream();
			//其他地方还有这个Bitmap的引用,此处不能recyle.....
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			bytes=baos.toByteArray();
		}
		/*if(bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)){
			if(null!=bitmap){
				bitmap.recycle();
				bitmap=null;
			}
		}*/
		return bytes;
	}
}