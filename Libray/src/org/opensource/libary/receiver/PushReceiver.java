package org.opensource.libary.receiver;

import org.opensource.libary.utils.Log4j;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class PushReceiver extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
			Log4j.debug("手机开机了...bootComplete!");
		} else if (Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction())) {
			Log4j.debug("新安装了应用程序....pakageAdded!");
		} else if (Intent.ACTION_PACKAGE_REMOVED.equals(intent.getAction())) {
			Log4j.debug("应用程序被卸载了....pakageRemoved!");
		} else if (Intent.ACTION_USER_PRESENT.equals(intent.getAction())) {
			Log4j.debug("手机被唤醒了.....userPresent");
			//startService(service);
		}
	}
}