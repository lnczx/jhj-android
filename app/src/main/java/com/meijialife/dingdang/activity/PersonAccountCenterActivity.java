package com.meijialife.dingdang.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.meijialife.dingdang.BaseActivity;
import com.meijialife.dingdang.Constants;
import com.meijialife.dingdang.R;
import com.meijialife.dingdang.bean.SalaryEntity;
import com.meijialife.dingdang.bean.UserAccountBean;
import com.meijialife.dingdang.utils.LogOut;
import com.meijialife.dingdang.utils.NetworkUtils;
import com.meijialife.dingdang.utils.SpFileUtil;
import com.meijialife.dingdang.utils.StringUtils;
import com.meijialife.dingdang.utils.UIUtils;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 账户中心
 */
public class PersonAccountCenterActivity extends BaseActivity implements
        OnClickListener {

    @BindView(R.id.iv_alipay_change)
    TextView ivalipaychange;
    @BindView(R.id.tv_alipay_status)
    TextView tvAlipayStatus;
    @BindView(R.id.tv_date_select)
    TextView tvDateSelect;
    @BindView(R.id.layout_account_view)
    LinearLayout layoutAccountView;
    @BindView(R.id.et_alipay_zhanghao)
    EditText et_alipayzhanghao;
    private TextView tv_det_money, tv_total_incoming, tv_alipay_status;
    private String det_money = "0";
    private String total_money = "0";

    int mYear, mMonth, mDay;
    Button btn;
    final int DATE_DIALOG = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.layout_person_account);
        ButterKnife.bind(this);
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
        tv_alipay_status = (TextView) findViewById(R.id.tv_alipay_status);

        tvDateSelect.setOnClickListener(this);
        ivalipaychange.setOnClickListener(this);
        get_total_dept();
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.layout_qiankuan://欠款
                intent = new Intent(this, PersonPayDebtActivity.class);
                intent.putExtra("detMoney", det_money);
                break;
            case R.id.layout_tixian://提现
                intent = new Intent(this, PersonTixianActivity.class);
                intent.putExtra("totalMoney", total_money);
                break;
            case R.id.layout_mingxi://提现明细
                intent = new Intent(this, PersonPayDetailActivity.class);
                break;
            case R.id.tv_date_select://日期选择
                showDialog(DATE_DIALOG);
                break;
            case R.id.iv_alipay_change://保存

                String zhanghao = et_alipayzhanghao.getText().toString().trim();
                if (zhanghao == null) {
                    UIUtils.showToast(this, "支付宝账号不能为空");
                } else {
                    setAlipay(zhanghao);
                }

                break;
            default:
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }

        final Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG:
                DatePickerDialog datePickerDialog = new DatePickerDialog(this, mdateListener, mYear, mMonth, 0);
//                datePickerDialog.getDatePicker().setMinDate();
                return datePickerDialog;
        }
        return null;
    }

    /**
     * 设置日期 利用StringBuffer追加
     */
    public void display() {
        tvDateSelect.setText(new StringBuffer().append(mYear).append(" ").append(mMonth + 1));
    }

    private DatePickerDialog.OnDateSetListener mdateListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            display();
        }
    };


    /**
     * 获取账户信息
     */
    private void get_total_dept() {
        if (!NetworkUtils.isNetworkConnected(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), getString(R.string.net_not_open), Toast.LENGTH_SHORT).show();
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
                                UserAccountBean userIndexData = new Gson().fromJson(data, UserAccountBean.class);
                                JSONObject jsonObject = new JSONObject(data);
                                String dept = jsonObject.optString("total_dept");
                                String incoming = jsonObject.optString("total_cash");

                                det_money = userIndexData.getTotal_dept();
                                total_money = userIndexData.getTotal_cash();
                                tv_total_incoming.setText("本月收入：" + userIndexData.getSalary_after_tax() + "元\n状态：" + userIndexData.getSalary_status_name());
                                tv_det_money.setText(det_money + "元");
                                tv_alipay_status.setText("状态：" + userIndexData.getAli_pay_lock_name());
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


    /**
     * 保存支付宝接口
     */
    public void setAlipay(String alipay_account) {
        String staffid = SpFileUtil.getString(PersonAccountCenterActivity.this, SpFileUtil.FILE_UI_PARAMETER, SpFileUtil.KEY_STAFF_ID, "");
        Map<String, String> map = new HashMap<String, String>();
        map.put("staff_id", staffid);
        map.put("alipay_account", alipay_account);
        AjaxParams param = new AjaxParams(map);

        showDialog();
        new FinalHttp().post(Constants.URL_SET_ALIPAY, param, new AjaxCallBack<Object>() {
            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                dismissDialog();
                Toast.makeText(PersonAccountCenterActivity.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
                UIUtils.showTestToast(PersonAccountCenterActivity.this, "errorMsg:" + strMsg);
            }

            @Override
            public void onSuccess(Object t) {
                super.onSuccess(t);
                String errorMsg = "";
                dismissDialog();
                LogOut.i("========", "onSuccess：" + t);
                // UIUtils.showTestToast(HistoryOrderActivity.this, "order_from："+t.toString());
                try {
                    if (StringUtils.isNotEmpty(t.toString())) {
                        JSONObject obj = new JSONObject(t.toString());
                        int status = obj.getInt("status");
                        String msg = obj.getString("msg");
                        String data = obj.getString("data");
                        if (status == Constants.STATUS_SUCCESS) { // 正确
                            UIUtils.showToast(PersonAccountCenterActivity.this, "保存成功");
                            get_total_dept();
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
                    errorMsg = "网络繁忙，请稍后再试";

                }
                // 操作失败，显示错误信息|
                if (!StringUtils.isEmpty(errorMsg.trim())) {
                    UIUtils.showToast(PersonAccountCenterActivity.this, errorMsg);
                }
            }
        });

    }


    /**
     * 获取salary列表
     */
    public void getSalary(String salary_month) {
        String staffid = SpFileUtil.getString(PersonAccountCenterActivity.this, SpFileUtil.FILE_UI_PARAMETER, SpFileUtil.KEY_STAFF_ID, "");
        Map<String, String> map = new HashMap<String, String>();
        map.put("staff_id", staffid);
        map.put("salary_month", salary_month);
        AjaxParams param = new AjaxParams(map);

        showDialog();
        new FinalHttp().post(Constants.URL_GET_SALARY, param, new AjaxCallBack<Object>() {
            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                dismissDialog();
                Toast.makeText(PersonAccountCenterActivity.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
                UIUtils.showTestToast(PersonAccountCenterActivity.this, "errorMsg:" + strMsg);
            }

            @Override
            public void onSuccess(Object t) {
                super.onSuccess(t);
                String errorMsg = "";
                dismissDialog();
                LogOut.i("========", "onSuccess：" + t);
                // UIUtils.showTestToast(HistoryOrderActivity.this, "order_from："+t.toString());
                try {
                    if (StringUtils.isNotEmpty(t.toString())) {
                        JSONObject obj = new JSONObject(t.toString());
                        int status = obj.getInt("status");
                        String msg = obj.getString("msg");
                        String data = obj.getString("data");
                        if (status == Constants.STATUS_SUCCESS) { // 正确
                            ArrayList<SalaryEntity> secData = new Gson().fromJson(data, new TypeToken<ArrayList<SalaryEntity>>() {
                            }.getType());
                            if (secData != null && secData.size() > 0) {
                                showDatas(secData);
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
                    errorMsg = "网络繁忙，请稍后再试";

                }
                // 操作失败，显示错误信息|
                if (!StringUtils.isEmpty(errorMsg.trim())) {
                    UIUtils.showToast(PersonAccountCenterActivity.this, errorMsg);
                }
            }
        });

    }

    private void showDatas(ArrayList<SalaryEntity> secData) {

        for (int i = 0; i < secData.size(); i++) {
            TextView textView = new TextView(PersonAccountCenterActivity.this);
            textView.setText(secData.get(i).getName() + ":" + secData.get(i).getValue());
            layoutAccountView.addView(textView);
        }
    }

}
