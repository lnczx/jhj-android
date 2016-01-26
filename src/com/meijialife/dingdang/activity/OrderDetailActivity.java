package com.meijialife.dingdang.activity;

import java.util.HashMap;
import java.util.Map;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.poi.BaiduMapPoiSearch;
import com.baidu.mapapi.utils.poi.PoiParaOption;
import com.meijialife.dingdang.BaseActivity;
import com.meijialife.dingdang.Constants;
import com.meijialife.dingdang.R;
import com.meijialife.dingdang.bean.OrderListVo;
import com.meijialife.dingdang.bean.UserIndexData;
import com.meijialife.dingdang.utils.LogOut;
import com.meijialife.dingdang.utils.NetworkUtils;
import com.meijialife.dingdang.utils.SpFileUtil;
import com.meijialife.dingdang.utils.StringUtils;
import com.meijialife.dingdang.utils.UIUtils;

/**
 * 订单详情
 * 
 * @author windows7
 * 
 */

public class OrderDetailActivity extends BaseActivity {

	private UserIndexData userIndexData;
	private ImageView title_btn_right;
	private OrderListVo orderBean;
	private ImageView iv_order_type;
	private static String ORDERZDG = "zhongdiangong";
	private static String ORDERZL = "zhuli";
	private String ORDERTYPE = ORDERZDG;
	private static String START = "start";
	private static String OVER = "over";
	private TextView tv_order_over;
	private TextView tv_order_tiaozheng;
	private TextView tv_order_start;
	private ImageView iv_order_over;
	private ImageView iv_order_tiaozheng;
	private ImageView iv_order_start;
	private LinearLayout layout_order_tiaozheng;
	private View layout_order_tiaozheng_line;
	private long orderid = 0;
	private String urlString;
	private Short order_type;
	private EditText et_input_content;
	private EditText et_input_money;
	private TextView tv_order_money;
	private Short order_status;
	private LinearLayout layout_fuwu_shichang;
	private TextView tv_order_shichang;
	private TextView tv_service_time_type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.layout_order_detail);
		super.onCreate(savedInstanceState);

		orderBean = (OrderListVo) getIntent().getSerializableExtra("orderBean");

		initView();

	}

	private void initView() {
		setTitleName("订单详情");
		requestBackBtn();
		title_btn_right = (ImageView) findViewById(R.id.title_btn_right);

		title_btn_right.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
						+ "010-58734880"));
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);

			}
		});

		// 3个图标状态
		tv_order_over = (TextView) findViewById(R.id.tv_order_over);
		tv_order_tiaozheng = (TextView) findViewById(R.id.tv_order_tiaozheng);
		tv_order_start = (TextView) findViewById(R.id.tv_order_start);
		iv_order_over = (ImageView) findViewById(R.id.iv_order_over);
		iv_order_tiaozheng = (ImageView) findViewById(R.id.iv_order_tiaozheng);
		iv_order_start = (ImageView) findViewById(R.id.iv_order_start);

		layout_order_tiaozheng = (LinearLayout) findViewById(R.id.layout_order_tiaozheng);
		layout_fuwu_shichang = (LinearLayout) findViewById(R.id.layout_fuwu_shichang);
		layout_order_tiaozheng_line = (View) findViewById(R.id.layout_order_tiaozheng_line);

		TextView tv_order_remarks = (TextView) findViewById(R.id.tv_order_remarks);
		TextView tv_order_no = (TextView) findViewById(R.id.tv_order_no);
		TextView tv_order_time = (TextView) findViewById(R.id.tv_order_time);
		TextView tv_order_xiangmu = (TextView) findViewById(R.id.tv_order_xiangmu);
		tv_order_money = (TextView) findViewById(R.id.tv_order_money);
		TextView tv_order_incoming = (TextView) findViewById(R.id.tv_order_incoming);
		TextView tv_order_status = (TextView) findViewById(R.id.tv_order_status);
		TextView tv_service_type_name = (TextView) findViewById(R.id.tv_service_type_name);
		TextView btn_order_start_work = (TextView) findViewById(R.id.btn_order_start_work);
		TextView tv_call_phone = (TextView) findViewById(R.id.tv_call_phone);
		TextView tv_goto_address = (TextView) findViewById(R.id.tv_goto_address);
		iv_order_type = (ImageView) findViewById(R.id.iv_order_type);
		tv_order_shichang = (TextView) findViewById(R.id.tv_order_shichang);
		tv_service_time_type = (TextView) findViewById(R.id.tv_service_time_type);

		// 调整订单
		TextView tv_input_content = (TextView) findViewById(R.id.tv_input_content);
		TextView tv_input_money = (TextView) findViewById(R.id.tv_input_money);
		et_input_content = (EditText) findViewById(R.id.et_input_content);
		et_input_money = (EditText) findViewById(R.id.et_input_order_money);

		tv_call_phone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (null != orderBean) {
					String mobile = orderBean.getMobile();
					Intent intent = new Intent(Intent.ACTION_DIAL, Uri
							.parse("tel:" + mobile));
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
				}
			}
		});

		order_status = orderBean.getOrder_status();
		order_type = orderBean.getOrder_type();

		if (order_type == 0) {
			if (order_status < 3 || order_status >= 7) {
				// 不可点
				btn_order_start_work.setClickable(false);
				// btn_order_start_work.setPressed(true);
			} else {
				btn_order_start_work.setClickable(true);
				// btn_order_start_work.setPressed(false);
			}
		} else if (order_type == 2) {
			if (order_status == 0 || order_status == 1 || order_status == 3
					|| order_status == 7) {
				// 不可点
				btn_order_start_work.setClickable(false);
				// btn_order_start_work.setPressed(true);
			} else {
				btn_order_start_work.setClickable(true);
				// btn_order_start_work.setPressed(false);
			}
		}

		btn_order_start_work.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (order_type == 0) {// 钟点工
					if (order_status == 3) {
						change_work(START);
					} else if (order_status == 5) {
						change_work(OVER);
					}
				} else if (order_type == 2) {// 助理单
					if (order_status == 2) {// 已派工
						// 调整订单
						change_order();
					} else if (order_status == 4) {// 已支付
						change_work(START);
					} else if (order_status == 5) {
						change_work(OVER);
					}
				}
			}
		});
		tv_goto_address.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String service_addrLat = orderBean.getService_addrLat();
				String service_addrLng = orderBean.getService_addrLng();
				String service_addr = orderBean.getService_addr();
				startPoiNearbySearch(service_addrLat, service_addrLng,
						service_addr);

			}
		});

		if (null != orderBean) {
			tv_order_remarks.setText(orderBean.getRemarks());
			tv_order_no.setText(orderBean.getOrder_no());
			tv_order_time.setText(orderBean.getService_date());
			tv_order_xiangmu.setText(orderBean.getService_content());
			tv_order_money.setText(orderBean.getOrder_money() + "元");
			tv_order_incoming.setText(orderBean.getOrder_incoming() + "元");
			tv_order_status.setText(orderBean.getPay_type_name());
			tv_service_type_name.setText(orderBean.getService_type_name());
			btn_order_start_work.setText(orderBean.getButton_word());
			tv_input_content.setText(orderBean.getRemarks_confirm());

			// 判断哪些展示
			if (order_type == 0) {// 钟点工
				ORDERTYPE = ORDERZDG;
				dissmisOrderModify();
				iv_order_type
						.setBackgroundResource(R.drawable.icon_zhongdiangong);
				layout_fuwu_shichang.setVisibility(View.VISIBLE);
				tv_order_shichang.setText(orderBean.getService_hour()+"小时");
				tv_service_time_type.setText("服务时间：");

			} else if (order_type == 2) {// 助理单
				ORDERTYPE = ORDERZL;
				iv_order_type.setBackgroundResource(R.drawable.icon_shenfen);
				showOrderModify();
				layout_fuwu_shichang.setVisibility(View.GONE);
				tv_service_time_type.setText("下单时间：");
			}

			// 哪些选中
			if (order_status >= 7) {
				SelectOrderOver();
			} else if (order_status >= 5) {
				SelectOrderStart();
			} else if (order_status >= 3) {
				SelectOrderModify();
			}

			if (order_type == 2 && order_status == 2) {
				tv_input_content.setVisibility(View.GONE);
				tv_order_money.setVisibility(View.GONE);
				et_input_content.setVisibility(View.VISIBLE);
				et_input_money.setVisibility(View.VISIBLE);
			} else {
				tv_input_content.setVisibility(View.VISIBLE);
				tv_order_money.setVisibility(View.VISIBLE);
				et_input_content.setVisibility(View.GONE);
				et_input_money.setVisibility(View.GONE);
			}

		}

	}

	/**
	 * 显示调整订单
	 */
	public void showOrderModify() {
		layout_order_tiaozheng.setVisibility(View.VISIBLE);
		layout_order_tiaozheng_line.setVisibility(View.VISIBLE);

	}

	// 隐藏调整订单
	public void dissmisOrderModify() {
		layout_order_tiaozheng.setVisibility(View.GONE);
		layout_order_tiaozheng_line.setVisibility(View.GONE);
	}

	// 调整订单点亮
	public void SelectOrderModify() {
		tv_order_tiaozheng.setTextColor(getResources().getColor(
				R.color.simi_color_orange));
		tv_order_start.setTextColor(getResources().getColor(
				R.color.simi_color_gray));
		tv_order_over.setTextColor(getResources().getColor(
				R.color.simi_color_gray));

		iv_order_tiaozheng
				.setBackgroundResource(R.drawable.icon_order_tiaozheng_selected);
		iv_order_start.setBackgroundResource(R.drawable.icon_order_start);
		iv_order_over.setBackgroundResource(R.drawable.icon_order_over);

	}

	// 开始服务订单点亮
	public void SelectOrderStart() {
		tv_order_tiaozheng.setTextColor(getResources().getColor(
				R.color.simi_color_orange));
		tv_order_start.setTextColor(getResources().getColor(
				R.color.simi_color_orange));
		tv_order_over.setTextColor(getResources().getColor(
				R.color.simi_color_gray));

		iv_order_tiaozheng
				.setBackgroundResource(R.drawable.icon_order_tiaozheng_selected);
		iv_order_start
				.setBackgroundResource(R.drawable.icon_order_start_selected);
		iv_order_over.setBackgroundResource(R.drawable.icon_order_over);

	}

	// 已经完成订单点亮
	public void SelectOrderOver() {
		tv_order_tiaozheng.setTextColor(getResources().getColor(
				R.color.simi_color_orange));
		tv_order_start.setTextColor(getResources().getColor(
				R.color.simi_color_orange));
		tv_order_over.setTextColor(getResources().getColor(
				R.color.simi_color_orange));

		iv_order_tiaozheng
				.setBackgroundResource(R.drawable.icon_order_tiaozheng_selected);
		iv_order_start
				.setBackgroundResource(R.drawable.icon_order_start_selected);
		iv_order_over
				.setBackgroundResource(R.drawable.icon_order_over_selected);

	}

	/**
	 * 开始服务或者结束服务
	 */
	private void change_work(String type) {
		if (!NetworkUtils.isNetworkConnected(getApplicationContext())) {
			Toast.makeText(getApplicationContext(),
					getApplicationContext().getString(R.string.net_not_open), 0)
					.show();
			return;
		}
		if (null != orderBean) {
			orderid = orderBean.getOrder_id();
		}
		String staffid = SpFileUtil.getString(getApplicationContext(),
				SpFileUtil.FILE_UI_PARAMETER, SpFileUtil.KEY_STAFF_ID, "");
		Map<String, String> map = new HashMap<String, String>();
		map.put("staff_id", staffid);
		map.put("order_id", orderid + "");
		AjaxParams param = new AjaxParams(map);

		if (StringUtils.isEquals(type, START)) {
			urlString = Constants.URL_GET_START_ORDER_WORK;
		} else if (StringUtils.isEquals(type, OVER)) {
			urlString = Constants.URL_GET_OVER_ORDER_WORK;
		}
		// showDialog();
		new FinalHttp().post(urlString, param, new AjaxCallBack<Object>() {
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				UIUtils.showTestToastLong(getApplicationContext(), "errorMsg:"
						+ strMsg);
				// dismissDialog();
				Toast.makeText(
						getApplicationContext(),
						getApplicationContext().getString(
								R.string.network_failure), Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public void onSuccess(Object t) {
				super.onSuccess(t);
				String errorMsg = "";
				// dismissDialog();
				LogOut.i("========", "onSuccess：" + t);
				// UIUtils.showTestToastLong(getApplicationContext(),
				// "开始服务返回："+t.toString());
				try {
					if (StringUtils.isNotEmpty(t.toString())) {
						JSONObject obj = new JSONObject(t.toString());
						int status = obj.getInt("status");
						String msg = obj.getString("msg");
						String data = obj.getString("data");
						if (status == Constants.STATUS_SUCCESS) { // 正确
							OrderDetailActivity.this.finish();
							// if (StringUtils.isNotEmpty(data)) {
							// } else {
							// UIUtils.showToast(getApplicationContext(),
							// "数据错误");
							// }
						} else if (status == Constants.STATUS_SERVER_ERROR) { // 服务器错误
							errorMsg = getApplicationContext().getString(
									R.string.servers_error);
						} else if (status == Constants.STATUS_PARAM_MISS) { // 缺失必选参数
							errorMsg = getApplicationContext().getString(
									R.string.param_missing);
						} else if (status == Constants.STATUS_PARAM_ILLEGA) { // 参数值非法
							errorMsg = getApplicationContext().getString(
									R.string.param_illegal);
						} else if (status == Constants.STATUS_OTHER_ERROR) { // 999其他错误
							errorMsg = msg;
						} else {
							errorMsg = getApplicationContext().getString(
									R.string.servers_error);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					errorMsg = getApplicationContext().getString(
							R.string.servers_error);

				}
				// 操作失败，显示错误信息|
				if (!StringUtils.isEmpty(errorMsg.trim())) {
					UIUtils.showToast(getApplicationContext(), errorMsg);
				}
			}
		});

	}

	/**
	 * 调整订单
	 */
	private void change_order() {
		if (!NetworkUtils.isNetworkConnected(getApplicationContext())) {
			Toast.makeText(getApplicationContext(),
					getApplicationContext().getString(R.string.net_not_open), 0)
					.show();
			return;
		}
		if (null != orderBean) {
			orderid = orderBean.getOrder_id();
		}

		String content = et_input_content.getText().toString().trim();
		String money = et_input_money.getText().toString().trim();

		if (StringUtils.isEmpty(content)) {
			UIUtils.showToast(getApplicationContext(), "内容不能为空");
			return;
		}
		if (StringUtils.isEmpty(money)) {
			UIUtils.showToast(getApplicationContext(), "金额不能为空");
			return;
		}

		String staffid = SpFileUtil.getString(getApplicationContext(),
				SpFileUtil.FILE_UI_PARAMETER, SpFileUtil.KEY_STAFF_ID, "");
		Map<String, String> map = new HashMap<String, String>();
		map.put("staff_id", staffid);
		map.put("order_id", orderid + "");
		map.put("service_content", content);
		map.put("order_money", money);
		AjaxParams param = new AjaxParams(map);

		showDialog();
		new FinalHttp().post(Constants.URL_GET_CHANGE_ORDER_WORK, param,
				new AjaxCallBack<Object>() {
					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						super.onFailure(t, errorNo, strMsg);
						UIUtils.showTestToastLong(getApplicationContext(),
								"errorMsg:" + strMsg);
						dismissDialog();
						Toast.makeText(
								getApplicationContext(),
								getApplicationContext().getString(
										R.string.network_failure),
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(Object t) {
						super.onSuccess(t);
						String errorMsg = "";
						dismissDialog();
						LogOut.i("========", "onSuccess：" + t);
						UIUtils.showTestToastLong(getApplicationContext(),
								"调整订单返回：" + t.toString());
						try {
							if (StringUtils.isNotEmpty(t.toString())) {
								JSONObject obj = new JSONObject(t.toString());
								int status = obj.getInt("status");
								String msg = obj.getString("msg");
								String data = obj.getString("data");
								if (status == Constants.STATUS_SUCCESS) { // 正确
									OrderDetailActivity.this.finish();
									// if (StringUtils.isNotEmpty(data)) {
									// } else {
									// UIUtils.showToast(getApplicationContext(),
									// "数据错误");
									// }
								} else if (status == Constants.STATUS_SERVER_ERROR) { // 服务器错误
									errorMsg = getApplicationContext()
											.getString(R.string.servers_error);
								} else if (status == Constants.STATUS_PARAM_MISS) { // 缺失必选参数
									errorMsg = getApplicationContext()
											.getString(R.string.param_missing);
								} else if (status == Constants.STATUS_PARAM_ILLEGA) { // 参数值非法
									errorMsg = getApplicationContext()
											.getString(R.string.param_illegal);
								} else if (status == Constants.STATUS_OTHER_ERROR) { // 999其他错误
									errorMsg = msg;
								} else {
									errorMsg = getApplicationContext()
											.getString(R.string.servers_error);
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
							errorMsg = getApplicationContext().getString(
									R.string.servers_error);

						}
						// 操作失败，显示错误信息|
						if (!StringUtils.isEmpty(errorMsg.trim())) {
							UIUtils.showToast(getApplicationContext(), errorMsg);
						}
					}
				});

	}

	/**
	 * 启动百度地图Poi周边检索
	 */
	public void startPoiNearbySearch(String latStr, String lonStr,
			String addname) {
		if (StringUtils.isEmpty(latStr) || StringUtils.isEmpty(lonStr)
				|| StringUtils.isEmpty(addname)) {
			Toast.makeText(this, "地址信息错误", Toast.LENGTH_SHORT).show();
			return;
		}

		double lat = Double.parseDouble(latStr);
		double lon = Double.parseDouble(lonStr);

		LatLng ptCenter = new LatLng(lat, lon);
		PoiParaOption para = new PoiParaOption().key(addname).center(ptCenter)
				.radius(2000);

		try {
			BaiduMapPoiSearch.openBaiduMapPoiNearbySearch(para, this);
		} catch (Exception e) {
			e.printStackTrace();
			showDialog();
		}

	}

}
