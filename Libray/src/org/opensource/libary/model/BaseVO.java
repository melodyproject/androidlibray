package org.opensource.libary.model;

import java.io.Serializable;

public abstract class BaseVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8262247390657673697L;
	
//	public static final int TYPE_BEAN = 0;
//	public static final int TYPE_LIST = 1;
//	public static final int TYPE_OBJECT = 2;
//	public static final int TYPE_BEAN_LIST =3;	
	
	/**
	 * 返回结果类型为json
	 */
	public static final int TYPE_JSON =4;
	
	/**
	 * 返回结果类型为InputStream
	 */
	public static final int TYPE_INPUTSTREAM=5;
	
	
	
}
