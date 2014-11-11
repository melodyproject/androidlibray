package org.opensource.libary.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetWorkUtil {
	/**
	 * 判断当前网络是否可用
	 * 
	 * @param activity
	 *            当前Acitivity对象
	 * @return 可用返回true 否则返回false
	 * 
	 * */
	public static boolean isNetworkAvailable(Activity activity) {
		Context context = activity.getApplicationContext();
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm == null) {
			return false;
		} else {
			NetworkInfo info[] = cm.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

}
