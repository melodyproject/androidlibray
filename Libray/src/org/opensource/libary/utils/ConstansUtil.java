package org.opensource.libary.utils;

/**
 * 静态工具
 *
 * @author fuqiang
 * @version 1.0
 * @created 2014/11/2
 */
public class ConstansUtil {
	
	/**定义图片缓存空间大小*/
	public static final int MAX_MEMORY_SIZE=getMaxMemorySize();
	
	/**获取运行时最大的缓存空间大小的1/8*/
	public static int getMaxMemorySize(){
		int maxMemory=(int) (Runtime.getRuntime().maxMemory()/1024);
		//使用缓存的1/8作为缓存的空间大小
		int maxSize=maxMemory/8;
		return maxSize;
	}
}
