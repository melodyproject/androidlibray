package org.opensource.libary.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;
import org.opensource.libary.model.BaseVO;

public class JsonUtil {
	/**
	 * 该方法主要用于解决简单json数据
	 * @param target 解析返回数据类型
	 * @param json 需要解析的json数据
	 * @return 解析结果 BaseVO对象
	 * */
	public static BaseVO jsonToObject(Class<? extends BaseVO> target, JSONObject json) throws Exception {
		Map<String, Method> map = new HashMap<String, Method>();
		Method[] methods = target.getMethods();
		for(Method method:methods) {
			if(method.getName().startsWith("set")) {
				map.put(method.getName().toLowerCase(), method);
			}
		}
		BaseVO vo = target.newInstance();
		Field[] fields = target.getDeclaredFields();
		for(Field field:fields) {
			//如果是基本类型则做处如果有对象则不做处理
			String typeName = field.getType().getName();
			String name = field.getName();
			Method method = null;
			try {
				if (typeName.equals("boolean")) {
					method = map.get("set"+name.toLowerCase());
					method.invoke(vo, json.getBoolean(name));
				} else if (typeName.equals(Boolean.class.getName())) {
					method = map.get("set"+name.toLowerCase());
					method.invoke(vo, json.getBoolean(name));
				} else if (typeName.equals("int") || typeName.equals("byte")) {
					method = map.get("set"+name.toLowerCase());
					method.invoke(vo, json.getInt(name));
				} else if (typeName.equals(Integer.class.getName())
						|| typeName.equals(Byte.class.getName())) {
					method = map.get("set"+name.toLowerCase());
					method.invoke(vo, json.getInt(name));
				} else if (typeName.equals(String.class.getName())) {
					method = map.get("set"+name.toLowerCase());
					method.invoke(vo, json.getString(name));
				} else if (typeName.equals("double")
						|| typeName.equals("float")) {
					method = map.get("set"+name.toLowerCase());
					method.invoke(vo, json.getDouble(name));
				} else if (typeName.equals(Double.class.getName())
						|| typeName.equals(Float.class.getName())) {
					method = map.get("set"+name.toLowerCase());
					method.invoke(vo, json.getDouble(name));
				} else if (typeName.equals("long")) {
					method = map.get("set"+name.toLowerCase());
					method.invoke(vo, json.getLong(name));
				} else if (typeName.equals(Long.class.getName())) {
					method = map.get("set"+name.toLowerCase());
					method.invoke(vo, json.getLong(name));
				}
			} catch (Exception e) {
				continue;
			}
		}
		return vo;
	}
}
