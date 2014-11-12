package org.opensource.libary.network;

/**
 * 网络连接状态改变的回调接口
 * 
 * @author fuqiang
 * @version 1.0
 * @created 2014/11/11 
 */
public interface ConnectStateCallback {
	// 网络状态发生改变之后，此接口在Activity中注入实例
	public abstract void OnGetConnectState(boolean isConnected);
}