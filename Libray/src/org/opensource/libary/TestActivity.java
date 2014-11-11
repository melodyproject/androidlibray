package org.opensource.libary;

import java.io.File;
import org.opensource.libary.model.BaseVO;
import org.opensource.libary.network.HttpCallback;
import org.opensource.libary.network.HttpReqAsync;
import org.opensource.libary.network.HttpService;
import org.opensource.libary.network.ReqParam;
import org.opensource.libary.utils.FileUtil;
import org.opensource.libary.utils.ImageUtils;
import android.graphics.Bitmap;
import android.os.Bundle;

public class TestActivity extends BaseActivity {
	
	//private ImageView mCaseView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//mCaseView = (ImageView) findViewById(R.id.iv_case);
		
		//test
		//get:
		HttpReqAsync getAsync = new HttpReqAsync(this, "http://image.so.com/zj", mCallBack, null,"GET",BaseVO.TYPE_JSON);
		ReqParam params=new ReqParam();
		params.addParam("ch", "beauty");
		params.addParam("listtype", "new");
		params.addParam("sn", "");
		getAsync.setParam(params);
		HttpService.getInstance().addImmediateReq(getAsync);
		
		//post:
		HttpReqAsync postAsync=new HttpReqAsync(this, "http://192.168.10.112:8080/nuomi/TServlet", mCallBack, null, "POST",BaseVO.TYPE_JSON);
		ReqParam params2=new ReqParam();
		params2.addParam("name", "张三");
		params2.addParam("pwd", "123456");
		postAsync.setParam(params2);
		HttpService.getInstance().addImmediateReq(postAsync);
		
		//uploadFile:http://localhost:8080/testweb/UploadServlert
		HttpReqAsync uploadAsync=new HttpReqAsync(this, "http://192.168.10.112:8080/testweb/UploadServlert", mCallBack, null, "POST",BaseVO.TYPE_JSON);
		ReqParam params3=new ReqParam();
		params3.addParam("name", "张三");
		params3.addParam("pwd", "123456");
		Bitmap bitmap=ImageUtils.readBitMap("/mnt/sdcard/aa.jpg");
		params3.setBitmap(bitmap);//必须要设置
		params3.addParam("pic", ImageUtils.getByteStream(bitmap).toByteArray());
		uploadAsync.setParam(params3);
		HttpService.getInstance().addImmediateReq(uploadAsync);
	}
	
	private HttpCallback mCallBack = new HttpCallback() {
   		@Override
   		public void onResult(Object object) {
   			if(null!=object){
   				System.out.println(object);
				FileUtil.writeFile(FileUtil.CURRENT_PATH+ File.separator + "aa.txt",
						String.valueOf(object).getBytes(), 0,
						String.valueOf(object).getBytes().length);
   			}
   			//mCaseView.setImageBitmap(BitmapFactory.decodeStream((InputStream)object));
   		}
   		
		@Override
		public void onError(Object object) {
			
		}
	};
}
