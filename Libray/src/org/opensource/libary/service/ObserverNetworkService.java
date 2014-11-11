package org.opensource.libary.service;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.opensource.libary.network.ConnectStateCallback;
import org.opensource.libary.utils.Println;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.IBinder;

/**
 * 监听接收网络状态发生改变的service
 * 
 * @author fuqiang 2014/11/2
 */
public class ObserverNetworkService extends Service {
	private Binder mBinder = new NetWorkerBinder();
	private NetWorkChangeReceiver mReceiver = new NetWorkChangeReceiver();
	private ConnectStateCallback mCallback;
	private boolean mConnected = false;

	public void setmCallback(ConnectStateCallback mCallback) {
		this.mCallback = mCallback;
	}

	public class NetWorkerBinder extends Binder {
		public ObserverNetworkService getService() {
			return ObserverNetworkService.this;
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
		IntentFilter filter = new IntentFilter();
		// 添加接收网络连接状态改变的Action
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		// 注册广播
		registerReceiver(mReceiver, filter);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// 销毁广播
		unregisterReceiver(mReceiver);
	}

	private class NetWorkerTimerTask extends TimerTask {
		@Override
		public void run() {
			if (isConnected()) {
				Println.info("网络连接成功");
				mConnected = true;
			} else {
				mConnected = false;
				Println.info("网络连接失败");
			}
			if (mCallback != null) {
				// 通知网络状态发生改变
				mCallback.OnGetConnectState(mConnected);
			}
		}

		// 网络是否可用
		private boolean isConnected() {
			ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo ni = cm.getActiveNetworkInfo();
			return ni != null && ni.isConnectedOrConnecting();
		}
	}

	/**
	 * 监听网络状态的改变
	 */
	private class NetWorkChangeReceiver extends BroadcastReceiver {
		// 接收并处理网络状态发生改变的广播
		@Override
		public void onReceive(Context context, Intent intent) {
			if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent
					.getAction())) {
				Println.info("network change!");
				Timer timer = new Timer();
				timer.schedule(new NetWorkerTimerTask(), new Date());
			}
		}
	}
}