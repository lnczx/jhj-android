package com.meijialife.dingdang.alipay;

import android.app.Activity;
import android.content.Context;

/**
 * 支付回调
 *
 */
public interface OnAlipayCallback {

	/**
	 *
	 * @param isSucceed
	 *            支付是否成功
	 * @param msg
	 *            当isSucceed为false时返回错误信息 当isSucceed为true时返回订单号
	 */
	public void onAlipayCallback(Activity activity, Context context,
			boolean isSucceed, String msg);

}
