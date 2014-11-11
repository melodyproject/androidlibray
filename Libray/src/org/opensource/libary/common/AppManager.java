package org.opensource.libary.common;

import java.util.Stack;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

public class AppManager {
	private static Stack<Activity> sActivityStack;
	private static AppManager instance;

	private AppManager() {
	}
	
	/**
	 * 单一实例
	 */
	public static AppManager getAppManager() {
		if (instance == null) {
			instance = new AppManager();
		}
		return instance;
	}
	/**
	 * 添加Activity到堆栈
	 */
	public void addActivity(Activity activity){
		if(sActivityStack==null){
			sActivityStack=new Stack<Activity>();
		}
		sActivityStack.add(activity);
	}
	/**
	 * 获取当前Activity（堆栈中最后一个压入的）
	 */
	public Activity currentActivity(){
		Activity activity=sActivityStack.lastElement();
		return activity;
	}
	/**
	 * 结束当前Activity（堆栈中最后一个压入的）
	 */
	public void finishActivity(){
		Activity activity=sActivityStack.lastElement();
		finishActivity(activity);
	}
	/**
	 * 结束指定的Activity
	 */
	public void finishActivity(Activity activity){
		if(activity!=null){
			sActivityStack.remove(activity);
			activity.finish();
			activity=null;
		}
	}
	/**
	 * 结束指定类名的Activity
	 */
	public void finishActivity(Class<?> cls){
		for (Activity activity : sActivityStack) {
			if(activity.getClass().equals(cls) ){
				finishActivity(activity);
			}
		}
	}
	/**
	 * 结束所有Activity
	 */
	public void finishAllActivity(){
		for (int i = 0, size = sActivityStack.size(); i < size; i++){
            if (null != sActivityStack.get(i)){
            	sActivityStack.get(i).finish();
            }
	    }
		sActivityStack.clear();
	}
	/**
	 * 退出应用程序
	 */
	@SuppressWarnings("deprecation")
	public void AppExit(Context context) {
		try {
			finishAllActivity();
			ActivityManager activityMgr= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			activityMgr.restartPackage(context.getPackageName());
			System.exit(0);
		} catch (Exception e) {	}
	}
}
