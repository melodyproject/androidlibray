package org.opensource.libary.network;

/**
 * 网络连接状态改变的回调
 * 
 * @author fuqiang
 */
public interface ConnectStateCallback {
	// 网络状态发生改变之后，此接口在Activity中注入实例
	public abstract void OnGetConnectState(boolean isConnected);
}