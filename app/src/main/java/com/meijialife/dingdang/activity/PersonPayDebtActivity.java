package com.meijialife.dingdang.activity;

import java.util.HashMap;
import java.util.Map;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.meijialife.dingdang.BaseActivity;
import com.meijialife.dingdang.Constants;
import com.meijialife.dingdang.R;
import com.meijialife.dingdang.alipay.ConsAli;
import com.meijialife.dingdang.alipay.OnAlipayCallback;
import com.meijialife.dingdang.alipay.PayWithAlipay;
import com.meijialife.dingdang.utils.LogOut;
import com.meijialife.dingdang.utils.NetworkUtils;
import com.meijialife.dingdang.utils.SpFileUtil;
import com.meijialife.dingdang.utils.UIUtils;

/**
 * 支付欠款
 * 
 * @author windows
 * 
 */

public class PersonPayDebtActivity extends BaseActivity implements OnClickListener {

    private TextView tv_det_money, btn_topay;
    private String detMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.layout_person_paydebt);
        super.onCreate(savedInstanceState);

        detMoney = getIntent().getStringExtra("detMoney");

        initView();

    }

    private void initView() {
        setTitleName("支付欠款");
        requestBackBtn();

        tv_det_money = (TextView) findViewById(R.id.tv_det_money);
        btn_topay = (TextView) findViewById(R.id.btn_topay);
        btn_topay.setOnClickListener(this);
        tv_det_money.setText(detMoney + "元");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.btn_topay:
            postCardBuy();
            break;
        default:
            break;
        }

    }

    /**
     * 欠款支付接口
     * 
     */
    private void postCardBuy() {
        showDialog();
        if (!NetworkUtils.isNetworkConnected(this)) {
            Toast.makeText(this, getString(R.string.net_not_open), 0).show();
            return;
        }
        String staffid = SpFileUtil.getString(getApplicationContext(), SpFileUtil.FILE_UI_PARAMETER, SpFileUtil.KEY_STAFF_ID, "");
        Map<String, String> map = new HashMap<String, String>();
        map.put("staff_id", staffid);
        map.put("pay_type", "1"); // 支付类型 1 = 支付宝
        AjaxParams param = new AjaxParams(map);

        new FinalHttp().get(Constants.URL_GET_PAY_DEPT, param, new AjaxCallBack<Object>() {
            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                UIUtils.showTestToast(getApplicationContext(), "获取支付信息：" + t );
                dismissDialog();
                Toast.makeText(PersonPayDebtActivity.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(Object t) {
                super.onSuccess(t);
                dismissDialog();
                LogOut.i("========", "onSuccess：" + t);
                UIUtils.showTestToast(getApplicationContext(), "获取支付信息：" + t );
                JSONObject json;
                try {
                    json = new JSONObject(t.toString());
                    int status = Integer.parseInt(json.getString("status"));
                    String msg = json.getString("msg");

                    if (status == Constants.STATUS_SUCCESS) { // 正确
                        parseCardBuyJson(1, json);
                    } else if (status == Constants.STATUS_SERVER_ERROR) { // 服务器错误
                        Toast.makeText(PersonPayDebtActivity.this, getString(R.string.servers_error), Toast.LENGTH_SHORT).show();
                    } else if (status == Constants.STATUS_PARAM_MISS) { // 缺失必选参数
                        Toast.makeText(PersonPayDebtActivity.this, getString(R.string.param_missing), Toast.LENGTH_SHORT).show();
                    } else if (status == Constants.STATUS_PARAM_ILLEGA) { // 参数值非法
                        Toast.makeText(PersonPayDebtActivity.this, getString(R.string.param_illegal), Toast.LENGTH_SHORT).show();
                    } else if (status == Constants.STATUS_OTHER_ERROR) { // 999其他错误
                        Toast.makeText(PersonPayDebtActivity.this, msg, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(PersonPayDebtActivity.this, getString(R.string.servers_error), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(PersonPayDebtActivity.this, getString(R.string.servers_error), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 欠款数据拼接
     * 
     * @param json
     */
    private void parseCardBuyJson(int payType, JSONObject json) {
        String card_order_no = "";// 充值卡订单号
        try {
            JSONObject obj = json.getJSONObject("data");
            order_money = obj.getString("order_money");
            card_order_no = obj.getString("order_no");
            order_id = obj.getString("order_id");
            notify_url = obj.getString("notify_url");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(this, getString(R.string.servers_error), Toast.LENGTH_SHORT).show();
        }
        if (payType == 1) {
            new PayWithAlipay(PersonPayDebtActivity.this, PersonPayDebtActivity.this, memberCallback,notify_url, order_id, ConsAli.PAY_TO_MEMBER,
                    order_money /* "0.01" */, card_order_no).pay();
        }
    }

    /**
     * 会员充值，支付宝回调
     */
    OnAlipayCallback memberCallback = new OnAlipayCallback() {

        public void onAlipayCallback(Activity activity, Context context, boolean isSucceed, String msg) {
            /** 支付宝回调位置 **/
            if (isSucceed) {
                // 支付成功
                String tradeNo = msg;
                Toast.makeText(getApplication(), "支付成功！", 1).show();
                // 支付成功跳转到订单详情页面
                // Intent intent = new Intent(PersonPayDebtActivity.this,OrderDetailsActivity.class);
                // intent.putExtra("orderId",orderId);
                // startActivity(intent);
                // PersonPayDebtActivity.this.finish();
            } else {
                Toast.makeText(getApplication(), msg, 1).show();
            }
        }
    };
    private String order_money;
    private String order_id;
    private String notify_url;

}
