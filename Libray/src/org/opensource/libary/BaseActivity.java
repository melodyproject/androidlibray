package org.opensource.libary;

import org.opensource.libary.common.AppManager;
import org.opensource.libary.network.ConnectStateCallback;
import org.opensource.libary.service.ObserverNetworkService;
import org.opensource.libary.service.ObserverNetworkService.NetWorkerBinder;
import org.opensource.libary.utils.Println;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

/**
 * <h1>程序界面基类</h1><br/>
 * @author fuqiang
 */
public class BaseActivity extends Activity {
	
	// 监听网络状态变化的service
	private ObserverNetworkService mService;
	// 标识当前网络状态
	private boolean mConncetState = true;
	private boolean flag=false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 添加Activity到堆栈
		AppManager.getAppManager().addActivity(this);
		
		Println.setDebug(true);//设置为调试模式
		Intent service = new Intent();
		service.setClass(this, ObserverNetworkService.class);
		flag=bindService(service, conn, BIND_AUTO_CREATE);// 绑定service
		
	}
	
	ServiceConnection conn = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {
		}
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mService = ((NetWorkerBinder) service).getService();
			mService.setmCallback(new ConnectStateCallback() {
				@Override
				public void OnGetConnectState(boolean isConnected) {
					// 如果两者标识状态不同，才进行通知显示
					if (mConncetState != isConnected) {
						mConncetState = isConnected;
						if (mConncetState) {
							// mHandler.sendEmptyMessage(Constant.WHAT_CONNECT_SUCCESS);
						} else {
							// mHandler.sendEmptyMessage(Constant.WHAT_CONNECT_ERROR);
						}
					}
				}
			});
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 结束Activity&从堆栈中移除
		AppManager.getAppManager().finishActivity(this);
		if (null!=conn && flag) {
			flag=false;
			unbindService(conn);
		}
	}

}
