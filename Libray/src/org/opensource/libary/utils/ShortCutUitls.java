package org.opensource.libary.utils;

import org.opensource.libary.R;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;

/**
 * 快捷方式工具类
 * 
 * @author fuqiang
 */
public class ShortCutUitls {
	/**
	 * 创建一个快捷方式
	 * 
	 * @param packageContext
	 *            上下文
	 * @param cls
	 *            要打开的activity
	 * @param resId
	 *            快捷方式的图片
	 */
	public static void CreateShortCut(int resId, Context packageContext,
			Class<?> cls,String filename,String key) {
		//查看是否已经存在了?
		boolean exists = SharePersistent.getInstance(filename).getBoolean(packageContext,key);
		if (!exists) { // 此处判断SharePreferences的xml文件中是否有创建的记录。
			delShortcut(packageContext, cls);// 先删除
			addShortcut(packageContext, cls, resId);// 在创建
			//保存记录
			SharePersistent.getInstance(filename).put(packageContext, key, true);
		}
	}

	/**
	 * 为程序创建桌面快捷方式
	 * 
	 * 同时需要在manifest中设置以下权限：
	 * <p>
	 * &ltuses-permission
	 * android:name="com.android.launcher.permission.INSTALL_SHORTCUT"/&gt
	 * </p>
	 */
	private static void addShortcut(Context context, Class<?> cls, int resId) {
		Intent install_shortcut = new Intent(
				"com.android.launcher.action.INSTALL_SHORTCUT");
		// 快捷方式的名称
		install_shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME,
				context.getString(R.string.app_name));
		// 不允许重复创建
		install_shortcut.putExtra("duplicate", false);
		// 指定当前的Activity为快捷方式启动的对象: 如 com.everest.video.VideoPlayer
		// 这里必须为Intent设置一个action，可以任意(但安装和卸载时该参数必须一致)
		String action = "android.intent.action.MAIN";
		Intent respondIntent = new Intent(context, cls);
		respondIntent.setAction(action);
		install_shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, respondIntent);
		// 快捷方式的图标
		ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(
				context, resId);
		install_shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
		context.sendBroadcast(install_shortcut);
	}

	/**
	 * 删除程序的快捷方式
	 * 
	 * 同时需要在manifest中设置以下权限：
	 * <p>
	 * &ltuses-permission
	 * android:name="com.android.launcher.permission.UNINSTALL_SHORTCUT" /&gt
	 * </p>
	 */
	private static void delShortcut(Context context, Class<?> cls) {
		Intent uninstall_shortcut = new Intent(
				"com.android.launcher.action.UNINSTALL_SHORTCUT");
		// 快捷方式的名称
		uninstall_shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME,
				context.getString(R.string.app_name));
		// 指定当前的Activity为快捷方式启动的对象: 如 com.everest.video.VideoPlayer
		// 这里必须为Intent设置一个action，可以任意(但安装和卸载时该参数必须一致)
		String action = "android.intent.action.MAIN";
		Intent respondIntent = new Intent(context, cls);
		respondIntent.setAction(action);
		uninstall_shortcut
				.putExtra(Intent.EXTRA_SHORTCUT_INTENT, respondIntent);
		context.sendBroadcast(uninstall_shortcut);
	}
}
