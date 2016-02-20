package com.meijialife.dingdang.activity;

import java.util.ArrayList;
import java.util.Calendar;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.meijialife.dingdang.BaseActivity;
import com.meijialife.dingdang.Constants;
import com.meijialife.dingdang.R;
import com.meijialife.dingdang.adapter.OrderPayDetailAdapter;
import com.meijialife.dingdang.bean.OrderPayDetail;
import com.meijialife.dingdang.utils.LogOut;
import com.meijialife.dingdang.utils.NetworkUtils;
import com.meijialife.dingdang.utils.SpFileUtil;
import com.meijialife.dingdang.utils.StringUtils;
import com.meijialife.dingdang.utils.UIUtils;

/**
 * 交易明细
 * 
 * @author windows
 * 
 */

public class PersonPayDetailActivity extends BaseActivity implements OnClickListener {

    private ListView layout_paydetail_list;
    private View loadMoreView;
    private Button loadMoreButton;
    private int pageIndex = 1;// 页码
    private int year = 2016;// 年
    private int month = 1;// 月
    private TextView tv_year;
    private TextView tv_no_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.layout_person_paydetail);
        super.onCreate(savedInstanceState);

        initView();

    }

    private void initView() {
        setTitleName("交易明细");
        requestBackBtn();
        layout_paydetail_list = (ListView) findViewById(R.id.layout_paydetail_list);
        tv_no_data = (TextView) findViewById(R.id.tv_no_data);

        ImageView iv_pre_month = (ImageView) findViewById(R.id.iv_pre_month);
        ImageView iv_next_month = (ImageView) findViewById(R.id.iv_next_month);
        tv_year = (TextView) findViewById(R.id.tv_year);

        Calendar cal = Calendar.getInstance();
        month = cal.get(Calendar.MONTH) + 1;
        year = cal.get(Calendar.YEAR);

        tv_year.setText(year + "年" + month + "月");

        loadMoreView = getLayoutInflater().inflate(R.layout.load_more, null);
        loadMoreButton = (Button) loadMoreView.findViewById(R.id.loadMoreButton);
        loadMoreButton.setOnClickListener(this);
        layout_paydetail_list.addFooterView(loadMoreView); // 设置列表底部视图

        iv_pre_month.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                pageIndex = 1;

                if (month > 1) {
                    month -= 1;
                } else {
                    month = 12;
                    year -= 1;
                }
                getData(pageIndex);
                tv_year.setText(year + "年" + month + "月");

            }
        });
        iv_next_month.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (month < 12) {
                    month += 1;
                } else {
                    month = 1;
                    year += 1;
                }
                pageIndex = 1;
                getData(pageIndex);
                tv_year.setText(year + "年" + month + "月");
            }
        });

        getData(pageIndex);
    }

    /**
     * 设置加载更多按钮状态
     * 
     * @param isLoad
     *            是否在加载
     */
    private void setLoadMoreStatus(boolean isLoad) {
        if (isLoad) {
            loadMoreButton.setText("正在加载...");
            loadMoreButton.setEnabled(false);
            loadMoreButton.setClickable(false);
        } else {
            loadMoreButton.setText("加载更多");
            loadMoreButton.setEnabled(true);
            loadMoreButton.setClickable(true);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
        case R.id.layout_qiankuan:
            intent = new Intent(this, PersonPayDebtActivity.class);
            break;
        case R.id.layout_tixian:
            intent = new Intent();
            break;
        case R.id.layout_mingxi:
            intent = new Intent();
            break;
        case R.id.loadMoreButton:
            setLoadMoreStatus(true);
            getData(pageIndex);
            break;
        default:
            break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }

    private void getData(int page) {
        if (!NetworkUtils.isNetworkConnected(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), getString(R.string.net_not_open), 0).show();
            return;
        }

        String staffid = SpFileUtil.getString(getApplicationContext(), SpFileUtil.FILE_UI_PARAMETER, SpFileUtil.KEY_STAFF_ID, "");
        Map<String, String> map = new HashMap<String, String>();
        map.put("staff_id", staffid);
        map.put("year", year + "");
        map.put("month", month + "");
        map.put("page", page + "");
        AjaxParams param = new AjaxParams(map);

        showDialog();
        new FinalHttp().get(Constants.URL_GET_DETAIL_CASH, param, new AjaxCallBack<Object>() {
            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                UIUtils.showTestToastLong(getApplicationContext(), "errorMsg:" + strMsg);
                dismissDialog();
                setLoadMoreStatus(false);
                Toast.makeText(getApplicationContext(), getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(Object t) {
                super.onSuccess(t);
                String errorMsg = "";
                setLoadMoreStatus(false);
                dismissDialog();
                LogOut.i("========", "onSuccess：" + t);
                // UIUtils.showTestToastLong(getApplicationContext(), "交易明细返回："+t.toString());
                try {
                    if (StringUtils.isNotEmpty(t.toString())) {
                        JSONObject obj = new JSONObject(t.toString());
                        int status = obj.getInt("status");
                        String msg = obj.getString("msg");
                        String data = obj.getString("data");
                        if (status == Constants.STATUS_SUCCESS) { // 正确
                            if (StringUtils.isNotEmpty(data)) {
                                Gson gson = new Gson();
                                ArrayList<OrderPayDetail> secData = gson.fromJson(data, new TypeToken<ArrayList<OrderPayDetail>>() {
                                }.getType());
                                OrderPayDetailAdapter adapter = new OrderPayDetailAdapter(getApplicationContext());

                                if (secData != null && secData.size() > 0) {
                                    pageIndex += 1;
                                    adapter.setData(secData);
                                    layout_paydetail_list.setAdapter(adapter);

                                    layout_paydetail_list.setVisibility(View.VISIBLE);
                                    tv_no_data.setVisibility(View.GONE);

                                } else {
                                    if (pageIndex == 1) {
                                        layout_paydetail_list.setVisibility(View.GONE);
                                        tv_no_data.setVisibility(View.VISIBLE);
                                    } else {
                                        layout_paydetail_list.setVisibility(View.VISIBLE);
                                        tv_no_data.setVisibility(View.GONE);
                                        Toast.makeText(getApplicationContext(), "没有更多数据了", Toast.LENGTH_SHORT).show();
                                    }
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

}
