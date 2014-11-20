package org.opensource.libary.common;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;
import org.apache.http.HttpException;
import org.opensource.libary.AppStart;
import org.opensource.libary.R;
import org.opensource.libary.utils.ConstansUtil;
import org.opensource.libary.utils.Log4j;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Environment;
import android.widget.Toast;

/**
 * 应用程序异常类：用于捕获异常和提示错误信息
 * @author fuqiang
 * @version 1.0
 * @created 2014/11/11
 */
public class CrashException extends Exception implements UncaughtExceptionHandler{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1122185615558203440L;
	
	/** 定义异常类型 */
	public final static byte TYPE_NETWORK 	= 0x01;
	public final static byte TYPE_SOCKET	= 0x02;
	public final static byte TYPE_HTTP_CODE	= 0x03;
	public final static byte TYPE_HTTP_ERROR= 0x04;
	public final static byte TYPE_XML	 	= 0x05;
	public final static byte TYPE_IO	 	= 0x06;
	public final static byte TYPE_RUN	 	= 0x07;
	public final static byte TYPE_JSON	 	= 0x08;
	
	private byte type;
	private int code;
	
	/** 系统默认的UncaughtException处理类 */
	private Thread.UncaughtExceptionHandler mDefaultHandler;
	
	private CrashException(){
		this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
	}
	
	private CrashException(byte type, int code, Exception excp) {
		super(excp);
		this.type = type;
		this.code = code;		
		if(ConstansUtil.sDEBUG_LOG){
			this.saveErrorLog(excp);
		}
	}
	public int getCode() {
		return this.code;
	}
	public int getType() {
		return this.type;
	}
	
	/**
	 * 提示友好的错误信息
	 * @param ctx
	 */
	public void makeToast(Context ctx){
		switch(this.getType()){
		case TYPE_HTTP_CODE:
			String err = ctx.getString(R.string.http_status_code_error, this.getCode());
			Toast.makeText(ctx, err, Toast.LENGTH_SHORT).show();
			break;
		case TYPE_HTTP_ERROR:
			Toast.makeText(ctx, R.string.http_exception_error, Toast.LENGTH_SHORT).show();
			break;
		case TYPE_SOCKET:
			Toast.makeText(ctx, R.string.socket_exception_error, Toast.LENGTH_SHORT).show();
			break;
		case TYPE_NETWORK:
			Toast.makeText(ctx, R.string.network_not_connected, Toast.LENGTH_SHORT).show();
			break;
		case TYPE_XML:
			Toast.makeText(ctx, R.string.xml_parser_failed, Toast.LENGTH_SHORT).show();
			break;
		case TYPE_JSON:
			Toast.makeText(ctx, R.string.xml_parser_failed, Toast.LENGTH_SHORT).show();
			break;
		case TYPE_IO:
			Toast.makeText(ctx, R.string.io_exception_error, Toast.LENGTH_SHORT).show();
			break;
		case TYPE_RUN:
			Toast.makeText(ctx, R.string.app_run_code_error, Toast.LENGTH_SHORT).show();
			break;
		}
	}
	
	/**
	 * 保存异常日志
	 * @param excp
	 */
	@SuppressWarnings("deprecation")
	public void saveErrorLog(Exception excp) {
		String errorlog = "errorlog.txt";
		String savePath = "";
		String logFilePath = "";
		FileWriter fw = null;
		PrintWriter pw = null;
		try {
			//判断是否挂载了SD卡
			String storageState = Environment.getExternalStorageState();		
			if(storageState.equals(Environment.MEDIA_MOUNTED)){
				savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/crash/Log/";
				File file = new File(savePath);
				if(!file.exists()){
					file.mkdirs();
				}
				logFilePath = savePath + errorlog;
			}
			//没有挂载SD卡，无法写文件
			if(logFilePath == ""){
				return;
			}
			File logFile = new File(logFilePath);
			if (!logFile.exists()) {
				logFile.createNewFile();
			}
			fw = new FileWriter(logFile,true);
			pw = new PrintWriter(fw);
			pw.println("--------------------"+(new Date().toLocaleString())+"---------------------");	
			excp.printStackTrace(pw);
			pw.close();
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();		
		}finally{ 
			if(pw != null){ pw.close(); } 
			if(fw != null){ try { fw.close(); } catch (IOException e) { }}
		}

	}
	
	public static CrashException http(int code) {
		return new CrashException(TYPE_HTTP_CODE, code, null);
	}
	
	public static CrashException http(Exception e) {
		return new CrashException(TYPE_HTTP_ERROR, 0 ,e);
	}

	public static CrashException socket(Exception e) {
		return new CrashException(TYPE_SOCKET, 0 ,e);
	}
	
	public static CrashException io(Exception e) {
		if(e instanceof UnknownHostException || e instanceof ConnectException){
			return new CrashException(TYPE_NETWORK, 0, e);
		}
		else if(e instanceof IOException){
			return new CrashException(TYPE_IO, 0 ,e);
		}
		return run(e);
	}
	
	public static CrashException xml(Exception e) {
		return new CrashException(TYPE_XML, 0, e);
	}
	
	public static CrashException json(Exception e) {
		return new CrashException(TYPE_JSON, 0, e);
	}
	
	public static CrashException network(Exception e) {
		if(e instanceof UnknownHostException || e instanceof ConnectException){
			return new CrashException(TYPE_NETWORK, 0, e);
		}
		else if(e instanceof HttpException){
			return http(e);
		}
		else if(e instanceof SocketException){
			return socket(e);
		}
		return http(e);
	}
	
	public static CrashException run(Exception e) {
		return new CrashException(TYPE_RUN, 0, e);
	}

	/**
	 * 获取APP异常崩溃处理对象
	 * @param context
	 * @return
	 */
	public static CrashException getAppExceptionHandler(){
		return new CrashException();
	}
	
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {

		if(!handleException(ex) && mDefaultHandler != null) {
			mDefaultHandler.uncaughtException(thread, ex);
		}

	}
	/**
	 * 自定义异常处理:收集错误信息&发送错误报告
	 * @param ex
	 * @return true:处理了该异常信息;否则返回false
	 */
	private boolean handleException(Throwable ex) {
		if(ex == null) {
			return false;
		}
		
		final Context context = AppManager.getAppManager().currentActivity();
		
		if(context == null) {
			return false;
		}
		
		final String crashReport = getCrashReport(context, ex);
		//显示异常信息&发送报告
		Log4j.debug(""+crashReport);
		if(ConstansUtil.sDEBUG_LOG){
			new Thread() {
				public void run() {
					try {
						//发送邮件通知,客户端出现错误了.....
						EmailSender sender = new EmailSender();
						sender.setProperties("smtp.163.com", "25");
						sender.setMessage("devloper_melody@163.com", "Android客户端 - 错误报告",
								crashReport);
						sender.setReceiver(new String[] { "1904992259@qq.com" });
						sender.sendEmail("smtp.163.com", "devloper_melody@163.com",
								"fuqiang921230");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}.start();
		}
		return true;
	}
	/**
	 * 获取APP崩溃异常报告
	 * @param ex
	 * @return
	 */
	private String getCrashReport(Context context, Throwable ex) {
		PackageInfo pinfo = ((AppStart)context.getApplicationContext()).getPackageInfo();
		StringBuffer exceptionStr = new StringBuffer();
		exceptionStr.append("Version: "+pinfo.versionName+"("+pinfo.versionCode+")\n");
		exceptionStr.append("Android: "+android.os.Build.VERSION.RELEASE+"("+android.os.Build.MODEL+")\n");
		exceptionStr.append("Exception: "+ex.getMessage()+"\n");
		StackTraceElement[] elements = ex.getStackTrace();
		for (int i = 0; i < elements.length; i++) {
			exceptionStr.append(elements[i].toString()+"\n");
		}
		return exceptionStr.toString();
	}
}
