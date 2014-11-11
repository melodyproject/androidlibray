package org.opensource.libary.utils;

import android.util.Log;

/**
 * <p>日志工具类</p>
 * <h1>注意：在使用Pritln的时候需要先设置debug模式</h1>
 * <p>首先使用:<b>Println.setDebug(boolean)</b></p>
 * <ol>
 * 	<li>true为调试模式;</li>
 *  <li>发布的时候要设置为false;</li>
 * </ol>
 * @author fuqiang 2014/11/2
 */
public class Println {

	private static boolean isDebug = false;// 调试模式，发布的时候要设置为false;

	private static final String TAG = "LogUtils";

	/**
	 * 设置此处是否为调试模式
	 * 
	 * @param debug  true | false
	 */
	public static void setDebug(boolean debug) {
		isDebug = debug;
	}

	/**
	 * 输出错误日志
	 */
	public static void error(String message) {
		if (isDebug) {
			Log.e(TAG, message);
		}
	}

	/**
	 * 输出info日志
	 */
	public static void info(String message) {
		if (isDebug) {
			Log.i(TAG, message);
		}
	}

	/**
	 * 输出debug日志
	 */
	public static void debug(String message) {
		if (isDebug) {
			Log.d(TAG, message);
		}
	}

	/**
	 * 输出警告日志
	 */
	public static void warn(String message) {
		if (isDebug) {
			Log.w(TAG, message);
		}
	}

	/**
	 * 输出verbose日志
	 */
	public static void verbose(String message) {
		if (isDebug) {
			Log.v(TAG, message);
		}
	}

}
