package org.opensource.libary;

import org.opensource.libary.common.CrashException;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

public class AppStart extends Application {
	private static AppStart sInstance;
	
	public static AppStart getInstance() {
		return sInstance;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		sInstance = this;
		//注册App异常崩溃处理器
        Thread.setDefaultUncaughtExceptionHandler(CrashException.getAppExceptionHandler());
	}
	
	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}
	
	/**
	 * 获取App安装包信息
	 * @return
	 */
	public PackageInfo getPackageInfo() {
		PackageInfo info = null;
		try { 
			info = getPackageManager().getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {    
			e.printStackTrace(System.err);
		} 
		if(info == null) info = new PackageInfo();
		return info;
	}
}