package org.opensource.libary.utils;

import android.util.Log;

/**
 * <p>
 * 日志工具类<br/>
 * 调试模式，发布的时候要设置为false;</b>
 * </p>
 * <ol>
 * <li>true为调试模式;</li>
 * <li>发布的时候要设置为false;</li>
 * </ol>
 * 
 * @author fuqiang 2014/11/2
 */
public class Println {
	private static final String TAG = "LogUtils";

	/**
	 * 输出错误日志
	 */
	public static void error(String message) {
		if (ConstansUtil.sDEBUG_LOG) {
			Log.e(TAG, message);
		}
	}

	/**
	 * 输出info日志
	 */
	public static void info(String message) {
		if (ConstansUtil.sDEBUG_LOG) {
			Log.i(TAG, message);
		}
	}

	/**
	 * 输出debug日志
	 */
	public static void debug(String message) {
		if (ConstansUtil.sDEBUG_LOG) {
			Log.d(TAG, message);
		}
	}

	/**
	 * 输出警告日志
	 */
	public static void warn(String message) {
		if (ConstansUtil.sDEBUG_LOG) {
			Log.w(TAG, message);
		}
	}

	/**
	 * 输出verbose日志
	 */
	public static void verbose(String message) {
		if (ConstansUtil.sDEBUG_LOG) {
			Log.v(TAG, message);
		}
	}

}
