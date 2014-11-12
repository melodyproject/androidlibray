package org.opensource.libary.network;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.opensource.libary.model.BaseVO;
import org.opensource.libary.utils.Println;
import android.content.Context;

/**
 * 异步请求网络
 * 
 * @author fuqiang
 * @version 1.0
 * @created 2014/11/11 
 */
@SuppressWarnings("unused")
public class HttpReqAsync extends HttpReq {
	private Class<? extends BaseVO> mTargetClass;// 对象类型
	private Class<? extends BaseVO> mTargetClass2;// 对象类型
	private Integer mResultType = 0;// 结果类型
	private Context mContext;// 上下文

	/**
	 * 构造函数
	 * 
	 * @param context
	 *            上下文
	 * @param url
	 *            请求url
	 * @param function
	 *            回调对象
	 * @param targetClass
	 *            返回数据解析后存储对象，可为空
	 * @param requestMethod
	 *            请求方式
	 * @param resultType
	 *            返回数据类型BaseVO.TYPE_BEAN=0 BaseVO.TYPE_LIST=1
	 *            BaseVO.TYPE_OBJECT=2 BaseVO.TYPE_BEAN_LIST=3
	 *            BaseVO.TYPE_JSON=4
	 * 
	 */
	public HttpReqAsync(Context context, String url, HttpCallback function,
			Class<? extends BaseVO> targetClass, String requestMethod,
			Integer resultType) {
		mContext = context;
		mHost = HttpConfig.CRM_SERVER_NAME;
		mPort = HttpConfig.CRM_SERVER_PORT;
		mUrl = url;
		mCallBack = function;
		mTargetClass = targetClass;
		mResultType = resultType;
		mMethod = requestMethod;
	}

	public void setmTargetClass(Class<? extends BaseVO> mTargetClass) {
		this.mTargetClass = mTargetClass;
	}

	public void setmTargetClass2(Class<? extends BaseVO> mTargetClass2) {
		this.mTargetClass2 = mTargetClass2;
	}

	public void setmResultType(Integer mResultType) {
		this.mResultType = mResultType;
	}
	
	/**
	 * 此处还是相当于在doInBackground中执行一些耗时的任务操作....
	 */
	@Override
	protected Object processResponse(InputStream response) throws Exception {
		//可以进行其他操作...
		if (response != null&&BaseVO.TYPE_JSON==mResultType) {
			String jsonStr=null;
			InputStream is = response;
			InputStreamReader ireader = new InputStreamReader(is);
			BufferedReader breader = new BufferedReader(ireader);
			StringBuffer sb = new StringBuffer();
			String code;
			while ((code = breader.readLine()) != null) {
				sb.append(code);
			}
			breader.close();
			ireader.close();
			jsonStr=sb.toString();
			Println.debug("relst:\n"+jsonStr);
		  /*switch (mResultType) {
			case BaseVO.TYPE_BEAN:
				
				break;
			case BaseVO.TYPE_INPUTSTREAM:
				
				break;
			case BaseVO.TYPE_JSON:
				
				break;
			}*/
			
			/*if(sb.toString().indexOf("errcode")==-1 && sb.toString().indexOf("access_token")!=-1){
				modelResult.setObj(sb.toString());
				return modelResult;
			}*/
			/*JSONObject json = new JSONObject(sb.toString());
			// 具体得json解析过程
			BaseVO baseVO = null;
			if (mTargetClass != null) {
				baseVO = mTargetClass.newInstance();
			}
			BaseVO vo = JsonUtil.jsonToObject(mTargetClass, json);
			System.out.println(vo);*/
			return jsonStr;
		}else if(BaseVO.TYPE_INPUTSTREAM==mResultType){
			return response;
		}
		return null;
	}

}
