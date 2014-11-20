package org.opensource.libary.network;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.opensource.libary.utils.ImageUtils;
import org.opensource.libary.utils.Println;
import android.graphics.Bitmap;

/**
 * 网络请求参数存储类
 * 
 * @author fuqiang
 * @version 1.0
 * @created 2014/11/11 
 */
public class ReqParam {
	private Map<String, String> mParams = new HashMap<String, String>();//参数存储容器map
	public Bitmap mBitmap = null;//上传本地图片bitmap对象
	
	public void setBitmap(Bitmap bm){
		mBitmap = bm;
	}
	
	public Map<String, String> getmParams() {
		return mParams;
	}
 
	public void setmParams(Map<String, String> mParams) {
		this.mParams = mParams;
	}
       
		public void addParam(String key, String val)
		{
			mParams.put(key, val);
		}
		public void addParam(String key, byte[] val){
			if(null==val){
				return;
			}
			double size = val.length / 1024;// KB
			Println.debug("size:"+size);
			Println.debug("size>400吗？"+(size>400));
			if (size > 400 && null!= mBitmap) {// 进行压缩
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				double i = size / 400;
				Bitmap bitmap = ImageUtils.zoomImage(mBitmap,
						mBitmap.getWidth() / Math.sqrt(i),
						mBitmap.getHeight() / Math.sqrt(i));
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
				val = baos.toByteArray();
			}
			StringBuffer buffer=new StringBuffer();
			for (int i = 0; i < val.length; i++) {
				buffer.append((char)val[i]);
			}
			
			Println.debug("buffer======="+ buffer.toString());
			mParams.put(key, buffer.toString());
		}
		public void addParam(String key, Object val)
		{
			mParams.put(key, val.toString());
		}
		
		/**
		 * 拼装请求参数
		 */
		public String toString()
		{
			List<String> keyList = new ArrayList<String>();
			Iterator<String> it = mParams.keySet().iterator();
			while(it.hasNext()) {
				String key = it.next();
				keyList.add(key);
			}

			Collections.sort(keyList, new Comparator<String>() {
				public int compare(String str1, String str2) {
					if(str1.compareTo(str2)>0) {
						return 1;
					}
					if(str1.compareTo(str2)<0) {
						return -1;
					}
					return 0;
				}
			});
			StringBuffer strbuf = new StringBuffer();
			for(String key:keyList) {
				if (!key.equals("pic")) {
					strbuf.append(key);
					strbuf.append("=");
					strbuf.append(mParams.get(key));
					strbuf.append("&");
				}
				
			}
			Println.debug("p-----"+strbuf.toString());
			String p = strbuf.toString().replaceAll("\n", "").replaceAll("\r", "");
			
			return p;
		}
		
}
