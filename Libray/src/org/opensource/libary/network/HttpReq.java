package org.opensource.libary.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.opensource.libary.utils.Println;
import android.os.AsyncTask;

/**
 * 处理网络请求的抽象类，异步发出网络请求;
 * <br/>
 * 返回网络数据后并响应回调函数;
 * @author fuqiang
 */
public abstract class HttpReq extends AsyncTask<Void, Integer, Object> {
	protected String mHost = null;//请求主机地址
	protected int mPort = 8088;//请求端口
	protected String mUrl = null;//网络请求地址
	protected String mMethod = null;//定义网络请求方式
	protected ReqParam mParam = new ReqParam();//参数封装类
	protected HttpCallback mCallBack = null;//回调函数
	private int mServiceTag = -1; 
	private static final String ACCEPT = "Accept-Charset";
	private static final String UTF8_ES = "UTF-8,*";
	private static final String UTF8 = "UTF-8";
	
	public void setServiceTag(int nTag) {
		mServiceTag = nTag;
	}
	
	public int getServiceTag() {
		return mServiceTag;
	}
	
	/**
	 * 返回回调方法
	 */
	protected HttpCallback getCallBack() {
		return mCallBack;
	}

	/**
	 * 响应网络请求得抽象函数
	 * @param response 网络请求返回的数据
	 * @return Object  返回的数据对象
	 * @throws Exception
	 */
	protected abstract Object processResponse(InputStream response)
			throws Exception;

	/**
	 * 设置网络请求参数封装类的函数
	 * @param param 网络请求参数封装类
	 */
	public void setParam(ReqParam param) {
		mParam = param;
	}

	/**
	 * 添加网络请求参数
	 * @param key   请求参数的名称
	 * @param value 请求参数的值，字符串类型
	 */
	public void addParam(String key, String value) {
		mParam.addParam(key, value);
	}
	
	/**
	 * 添加网络请求参数
	 * @param key   请求参数的名称
	 * @param value 请求参数的值，对象类型
	 */
	public void addParam(String key, Object value) {
		mParam.addParam(key, value);
	}

	/**
	 * 网络请求函数，该函数中通过判断网络请求的方式GET
	 * 发出网络请求，并返回响应的数据结果
	 * @return Object 返回的数据对象
	 * @throws Exception
	 */
	public Object runGetReq() throws Exception {
		HttpClient client = new DefaultHttpClient();
		if(null!=mParam&&mParam.toString().length()>1){
			mUrl += "?"+ mParam.toString().substring(0,mParam.toString().length() - 1);
		}
		HttpGet method = new HttpGet(mUrl);
		int statusCode = -1;
		HttpResponse resp=client.execute(method);//执行
	    statusCode=resp.getStatusLine().getStatusCode();//取得状态码
	    Object result =null;
		if (statusCode == HttpStatus.SC_OK) {//200
			result = processResponse(resp.getEntity().getContent());
		}
		return result;
	}
	
	/**
	 * 网络请求函数，该函数中通过判断网络请求的方式POST
	 * 发出网络请求，并返回响应的数据结果
	 * @return Object 返回的数据对象
	 * @throws Exception
	 */
	public Object runPostReq() throws Exception {
		Object result=post(mUrl, mParam.getmParams());
		return result;
	}
	
	/**
	 * 转换编码格式
	 * @param s 需要转换的字符串
	 * @return  转换编码后的字符串
	 */
	public static String decode(String s) {
		if (s == null) {
			return "";
		}
		try {
			return URLDecoder.decode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	/**
	 *  AsyncTask方法重载
	 *  预执行
	 */
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		Println.debug("onPreExecute:"+System.currentTimeMillis());
	}
	
	/**
	 *  AsyncTask方法重载
	 *  后台异步执行
	 */
	@Override
	protected Object doInBackground(Void... params) {
		Println.debug("doInBackground:"+System.currentTimeMillis());
		try {
			Object result = null;
			if ("GET".equals(mMethod)) {
				result=this.runGetReq();
			}else if ("POST".equals(mMethod)) {
				//如果POST方式的pic参数不为空的话,此处是发送图片的操作;
				if (mParam.getmParams().get("pic") != null) {
					return processResponse(picMethod());
				}
				//否则执行普通的POST方式请求;
				result=this.runPostReq();
			}
			return result;
		} catch (Exception e) {
			if(mCallBack!=null){
				mCallBack.onError(e);
			}
			return null;
		}
	}

	/**
	 *  AsyncTask方法重载
	 *  在任务执行成功时回调
	 */
	@Override
	protected void onPostExecute(Object result) {
		Println.debug("onPostExecute:"+System.currentTimeMillis());
		if (mCallBack != null) {
			//回调....
			mCallBack.onResult(result);
		}
		HttpService.getInstance().onReqFinish(this);
	}
	
	/**
	 *  AsyncTask方法重载
	 *  在任务成功取消时回调
	 */
	@Override
	protected void onCancelled() {
		if (mCallBack != null) {
			mCallBack.onResult(null);
		}
		HttpService.getInstance().onReqFinish(this);
	}
	
	/**
	 * post提交数据
	 */
	public String post(String url,Map<String, String> params)
			throws Exception {
		HttpPost httpost = new HttpPost(url);
		List<NameValuePair> pairList = new ArrayList<NameValuePair>();
		if(null!=params){
			for (String name : params.keySet()) {
				//key:name ; value:value ;
				pairList.add(new BasicNameValuePair(name,String.valueOf(params.get(name))));
			}
		}
		if (null != params) {
			params.clear();
			params = null;
		}
		httpost.setEntity(new UrlEncodedFormEntity(pairList, HTTP.UTF_8));
		return execute(httpost);
	}
	
	private String execute(HttpUriRequest req) throws Exception {
		String result = null;
		HttpResponse response = executeLoad(req);
		if (response != null) {
			int statusCode = response.getStatusLine().getStatusCode();
			switch (statusCode) {
			case HttpStatus.SC_OK:
				result = EntityUtils.toString(response.getEntity(), UTF8);
				break;
			case HttpStatus.SC_FORBIDDEN: // 验证未通过
				break;
			case HttpStatus.SC_NOT_FOUND: // 请求错误
				break;
			}
		}
		return result;
	}
	
	private HttpResponse executeLoad(HttpUriRequest req) throws Exception {
		HttpClient httpclient = CustomerHttpClient.getInstance();
		req.addHeader(ACCEPT, UTF8_ES);
		HttpResponse response = httpclient.execute(req);
		return response;
	}
	
	
	/**
	 * 处理发送文字和图片的函数
	 * 
	 * @return InputStream 发送图片后，网络返回的结果
	 */
	private InputStream picMethod() {
		InputStream result = null;
		HttpPost method = new HttpPost(mUrl);
		String strparams = mParam.toString();
		MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE,null,Charset.forName("UTF-8"));
		try {
			//FileBody picBody=null;
			ByteArrayBody byteBody=null;
			if (strparams != null && !strparams.equals("")) {
				// 分割文本类型参数字符串
				String[] params = strparams.split("&");
				for (String str : params) {
					if (str != null && !str.equals("")) {
						if (str.indexOf("=") > -1) {
							String[] p = str.split("=");
							// 获取参数值
							String value = (p.length == 2 ? decode(p[1]) : "");
							//取得当前客户端的字符编码
							Println.debug("当前默认的编码:"+Charset.defaultCharset());
							Println.debug("是否支持UTF8编码:"+Charset.isSupported("UTF-8"));
							entity.addPart(p[0], new StringBody(value,Charset.forName(HTTP.UTF_8)));
						}
					}
				}
				char[] pics = mParam.getmParams().get("pic").toCharArray();
				byte pic[] = new byte[pics.length];
				for (int i = 0; i < pics.length; i++) {
					pic[i] = (byte) pics[i];
				}
				
				byteBody=new ByteArrayBody(pic, "image/jpeg","123456.jpg");
				//picBody=new FileBody(new File(path),"image/jpeg", "utf-8");
				entity.addPart("pic",byteBody);
			}
			method.setEntity(entity);
			try {
				HttpResponse resp = executeLoad(method);
				int statusCode = resp.getStatusLine().getStatusCode();
				if (statusCode != HttpStatus.SC_OK) {
					return null;
				}
				result=resp.getEntity().getContent();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
}
