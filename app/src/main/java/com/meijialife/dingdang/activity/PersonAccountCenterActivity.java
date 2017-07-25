package com.meijialife.dingdang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
    @BindView(R.id.sp_select_date)
    Spinner sp_selectdate;
    @BindView(R.id.layout_account_view)
    LinearLayout layoutAccountView;
    @BindView(R.id.et_alipay_zhanghao)
    EditText et_alipayzhanghao;
    private TextView tv_det_money, tv_total_incoming, tv_alipay_status;
    private String det_money = "0";
    private String total_money = "0";


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

        ivalipaychange.setOnClickListener(this);
        get_total_dept();

        sp_selectdate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String time = sp_selectdate.getSelectedItem().toString();
                getSalary(time);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        getSalaryDate();
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
            case R.id.iv_alipay_change://保存
                String zhanghao = et_alipayzhanghao.getText().toString().trim();
                if (StringUtils.isNotEmpty(zhanghao)) {
                    setAlipay(zhanghao);
                } else {
                    UIUtils.showToast(this, "支付宝账号不能为空");
                }

                break;
            default:
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }

    }


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

                                det_money = userIndexData.getTotal_dept();
                                total_money = userIndexData.getTotal_cash();
                                tv_total_incoming.setText("本月收入：" + userIndexData.getSalary_after_tax() + "元\n状态：" + userIndexData.getSalary_status_name());
                                tv_det_money.setText(det_money + "元");
                                tv_alipay_status.setText("状态：" + userIndexData.getAli_pay_lock_name());

                                String account = userIndexData.getAli_pay_account();
                                if (StringUtils.isEmpty(account)) {
                                    et_alipayzhanghao.setText("");
                                    ivalipaychange.setVisibility(View.VISIBLE);
                                } else {
                                    et_alipayzhanghao.setText(account);
                                    ivalipaychange.setVisibility(View.GONE);
                                }
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
        new FinalHttp().get(Constants.URL_GET_SALARY, param, new AjaxCallBack<Object>() {
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
        if (layoutAccountView != null) {
            layoutAccountView.removeAllViews();
            for (int i = 0; i < secData.size(); i++) {
                View mView = getLayoutInflater().inflate(R.layout.item_center_list, null);
                TextView tv_name= (TextView) mView.findViewById(R.id.tv_name);
                TextView tv_value= (TextView) mView.findViewById(R.id.tv_value);
                String name = secData.get(i).getName();
                String value = secData.get(i).getValue();
                tv_name.setText(name);
                tv_value.setText(value);

                layoutAccountView.addView(mView);
            }
        }

    }


    /**
     * 获取salary列表
     */
    public void getSalaryDate() {
        String staffid = SpFileUtil.getString(PersonAccountCenterActivity.this, SpFileUtil.FILE_UI_PARAMETER, SpFileUtil.KEY_STAFF_ID, "");
        Map<String, String> map = new HashMap<String, String>();
        map.put("staff_id", staffid);
        AjaxParams param = new AjaxParams(map);

        showDialog();
        new FinalHttp().get(Constants.URL_GET_SALARY_DATE, param, new AjaxCallBack<Object>() {
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

                            ArrayList<String> listData = new Gson().fromJson(data, new TypeToken<ArrayList<String>>() {
                            }.getType());
                            if (listData != null && listData.size() > 0) {
                                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(PersonAccountCenterActivity.this, R.layout.spinner_list_item, listData);
                                sp_selectdate.setAdapter(arrayAdapter);
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
}
