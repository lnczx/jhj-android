package com.meijialife.dingdang.activity;

import java.util.HashMap;
import java.util.Map;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.meijialife.dingdang.BaseActivity;
import com.meijialife.dingdang.Constants;
import com.meijialife.dingdang.R;
import com.meijialife.dingdang.utils.LogOut;
import com.meijialife.dingdang.utils.NetworkUtils;
import com.meijialife.dingdang.utils.SpFileUtil;
import com.meijialife.dingdang.utils.StringUtils;
import com.meijialife.dingdang.utils.UIUtils;

/**
 * 账户中心
 * 
 */
public class PersonAccountCenterActivity extends BaseActivity implements
		OnClickListener {

	private TextView tv_det_money,tv_total_incoming;
	private String det_money="0";
	private String total_money="0";

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.layout_person_account);
		super.onCreate(savedInstanceState);

		initView();

	}

	private void initView() {
		setTitleName("账户中心");
		requestBackBtn();

		findViewById(R.id.layout_qiankuan).setOnClickListener(this);
		findViewById(R.id.layout_tixian).setOnClickListener(this);
		findViewById(R.id.layout_mingxi).setOnClickListener(this);
		tv_det_money = (TextView) findViewById(R.id.tv_det_money);
		tv_total_incoming = (TextView) findViewById(R.id.tv_total_incoming);
		
		get_total_dept();
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.layout_qiankuan://欠款
			intent = new Intent(this,PersonPayDebtActivity.class);
			intent.putExtra("detMoney", det_money);
			break;
		case R.id.layout_tixian://提现
			intent = new Intent(this,PersonTixianActivity.class);
			intent.putExtra("totalMoney", total_money);
			break;
		case R.id.layout_mingxi://提现明细
			intent = new Intent(this,PersonPayDetailActivity.class);
			break;
		default:
			break;
		}
		if (intent != null) {
			startActivity(intent);
		}
	}
	
	/**
	 * 获取欠款总额
	 */
    private void get_total_dept() {
        if (!NetworkUtils.isNetworkConnected(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), getString(R.string.net_not_open), 0).show();
            return;
        }

        String staffid = SpFileUtil.getString(getApplicationContext(), SpFileUtil.FILE_UI_PARAMETER, SpFileUtil.KEY_STAFF_ID, "");
        Map<String, String> map = new HashMap<String, String>();
        map.put("staff_id", staffid);
        AjaxParams param = new AjaxParams(map);

//        showDialog();
        new FinalHttp().get(Constants.URL_GET_TOTAL_DEPT, param, new AjaxCallBack<Object>() {
            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                UIUtils.showTestToastLong(getApplicationContext(), "欠款总额errorMsg:" + strMsg);
                dismissDialog();
                Toast.makeText(getApplicationContext(), getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(Object t) {
                super.onSuccess(t);
                String errorMsg = "";
//                dismissDialog();
                LogOut.i("========", "onSuccess：" + t);
//                UIUtils.showTestToastLong(getApplicationContext(), "欠款总额返回："+t.toString());
                try {
                    if (StringUtils.isNotEmpty(t.toString())) {
                        JSONObject obj = new JSONObject(t.toString());
                        int status = obj.getInt("status");
                        String msg = obj.getString("msg");
                        String data = obj.getString("data");
                        if (status == Constants.STATUS_SUCCESS) { // 正确
                            if (StringUtils.isNotEmpty(data)) {
//                                Gson gson = new Gson();
//                                userIndexData = gson.fromJson(data, UserIndexData.class);
//                                showData();
                                
                                JSONObject jsonObject=new JSONObject(data);
                                String dept = jsonObject.optString("total_dept");
                                String incoming = jsonObject.optString("total_cash");
                                
                                det_money=dept;
                                total_money=incoming;
                                tv_total_incoming.setText("可提现资金"+total_money+"元");
                                tv_det_money.setText(det_money+"元");
                            } else {
                                UIUtils.showToast(getApplicationContext(), "数据错误");
                            }
                        } else if (status == Constants.STATUS_SERVER_ERROR) { // 服务器错误
                            errorMsg = getString(R.string.servers_error);
                        } else if (status == Constants.STATUS_PARAM_MISS) { // 缺失必选参数
                            errorMsg = getString(R.string.param_missing);
                        } else if (status == Constants.STATUS_PARAM_ILLEGA) { // 参数值非法
                            errorMsg = getString(R.string.param_illegal);
                        } else if (status == Constants.STATUS_OTHER_ERROR) { // 999其他错误
                            errorMsg = msg;
                        } else {
                            errorMsg = getString(R.string.servers_error);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    errorMsg = getString(R.string.servers_error);

                }
                // 操作失败，显示错误信息|
                if (!StringUtils.isEmpty(errorMsg.trim())) {
                    UIUtils.showToast(getApplicationContext(), errorMsg);
                }
            }
        });

    }

}
