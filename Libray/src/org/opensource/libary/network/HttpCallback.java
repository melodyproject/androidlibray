package org.opensource.libary.network;

/**
 *	访问网络的回调接口
 */
public interface HttpCallback {
	/**
	 * 返回成功
	 * @param object 返回的数据对像	 
	 */
	public void onResult(Object object); 
	/**
	 * 返回错误
	 * @param object 返回的数据对像
	 */
	public void onError(Object object);
}