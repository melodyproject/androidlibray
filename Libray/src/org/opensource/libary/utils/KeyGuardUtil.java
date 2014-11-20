package org.opensource.libary.utils;

import java.util.Timer;
import java.util.TimerTask;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * 键盘操作工具类
 * <P>
 * 弹起键盘：optionKeyGuardShow
 * </p>
 * <P>
 * 隐藏键盘：optionKeyGuardHiden
 * </p>
 * 
 * @author fuqiang
 * 
 */
@SuppressWarnings("static-access")
public class KeyGuardUtil {

	/**
	 * 隐藏键盘
	 * 
	 * @param ctx
	 * @param view
	 */
	public void optionKeyGuardHiden(Context ctx, final View view) {
		final InputMethodManager imm = (InputMethodManager) ctx
				.getSystemService(ctx.INPUT_METHOD_SERVICE);
		Log4j.debug("mkl:" + imm.isActive() + "");
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}
	
	/**
	 * 弹起键盘
	 * 
	 * @param ctx
	 * @param view
	 */
	public void optionKeyGuardShow(Context ctx, final View view) {
		final InputMethodManager imm = (InputMethodManager) ctx
				.getSystemService(ctx.INPUT_METHOD_SERVICE);
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
			}
		}, 400);
	}
}
