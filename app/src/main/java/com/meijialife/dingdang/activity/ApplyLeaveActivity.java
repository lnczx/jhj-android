package com.meijialife.dingdang.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.meijialife.dingdang.BaseActivity;
import com.meijialife.dingdang.Constants;
import com.meijialife.dingdang.R;
import com.meijialife.dingdang.adapter.LeaveListAdapter;
import com.meijialife.dingdang.bean.LeaveEntity;
import com.meijialife.dingdang.utils.LogOut;
import com.meijialife.dingdang.utils.NetworkUtils;
import com.meijialife.dingdang.utils.SpFileUtil;
import com.meijialife.dingdang.utils.StringUtils;
import com.meijialife.dingdang.utils.UIUtils;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.ButterKnife;

/**
 * 申请下线
 */

public class ApplyLeaveActivity extends BaseActivity implements OnClickListener {


    private RadioGroup radiogroup;
    private ArrayList<LeaveEntity> secData;
    private int order_from_main = 2;

    private View loadMoreView;
    private Button loadMoreButton;
    private int pageIndex = 1;//页码
    private ArrayList<LeaveEntity> orderList;

    private ProgressDialog m_pDialog;

    private LinearLayout layout_no_order;

    private ListView layout_order_list;
    private LinearLayout layout_order_detail;
    private LeaveListAdapter listadapter;
    private int mYear, mMonth, mDay;
    private Button btn;
    private final int START_DATE_DIALOG = 1;
    private final int END_DATE_DIALOG = 2;

    private TextView sp_start_day;
    private TextView sp_end_day;
    private Spinner sp_leave_day_type;
    private EditText et_more;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.layout_history);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);

        initView();
        getLeaveList(pageIndex);

        EventBus.getDefault().register(this);
    }

    private void initView() {
        setTitleName("下线申请");
        requestBackBtn();

        setRightText("申请下线");

        getRightView().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
//                getApplyLeave();

                showApplyDialog();
            }
        });

        setHelpText();
        getHelView().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                showHelpDialog();
            }
        });


        layout_order_list = (ListView) findViewById(R.id.layout_order_list);
        layout_no_order = (LinearLayout) findViewById(R.id.layout_no_order);

        orderList = new ArrayList<LeaveEntity>();
        listadapter = new LeaveListAdapter(this);
        layout_order_list.setAdapter(listadapter);

        loadMoreView = this.getLayoutInflater().inflate(R.layout.load_more, null);
        loadMoreButton = (Button) loadMoreView.findViewById(R.id.loadMoreButton);

        layout_order_list.addFooterView(loadMoreView);   //设置列表底部视图
        loadMoreButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setLoadMoreStatus(true);
                getLeaveList(pageIndex);
            }
        });


        final Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);


    }

    private void showApplyDialog() {

        View view = (LinearLayout) getLayoutInflater().inflate(R.layout.apply_leave_dialog, null);

        sp_start_day = (TextView) view.findViewById(R.id.sp_start_day);
        sp_end_day = (TextView) view.findViewById(R.id.sp_end_day);
        sp_leave_day_type = (Spinner) view.findViewById(R.id.sp_leave_day_type);
        et_more = (EditText) view.findViewById(R.id.et_more);

        sp_start_day.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(START_DATE_DIALOG);

            }
        });
        sp_end_day.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(END_DATE_DIALOG);
            }
        });
        List<String> list = new ArrayList<>();
        list.add("全天");
        list.add("上午");
        list.add("下午");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ApplyLeaveActivity.this, R.layout.spinner_list_item, list);
        sp_leave_day_type.setAdapter(arrayAdapter);

        AlertDialog.Builder dialog = new AlertDialog.Builder(ApplyLeaveActivity.this);
        dialog.setTitle("申请下线");
        dialog.setView(view);
        dialog.setPositiveButton("提交申请", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String leave_day_type = sp_leave_day_type.getSelectedItem().toString();
                String startDay = sp_start_day.getText().toString();
                String end_day = sp_end_day.getText().toString();
                String more = et_more.getText().toString();
                if (sp_end_day == null || leave_day_type == null || startDay == null) {
                    UIUtils.showToast(ApplyLeaveActivity.this, "请选择下线日期和类型");
                } else {

                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
                    Date nowDate = new Date();
                    String today = formatter.format(nowDate);

                    Calendar c = Calendar.getInstance();
                    c.add(Calendar.DAY_OF_MONTH, 1);
                    Date tomorrowDate = c.getTime();
                    String tomorrowStr = formatter.format(tomorrowDate);

                    String alertMsg = "";

                    try {
                        Date startDate = formatter.parse(startDay);
                        Date endDate = formatter.parse(end_day);

                        String startDateStr = formatter.format(startDate);
                        String endDateStr = formatter.format(endDate);



                        if (today.equals(startDateStr)) {
                            //2.当天请当天假，可以申请通过，但是后两单将按照30%比例提成
                            //（当天请当天假，后两单将按照30%比例提成）（弹出确定和取消按键）
                            alertMsg = "当天请当天假，后两单将按照30%比例提成";
                        } else if (tomorrowStr.equals(startDateStr)) {
                            //当天请第二天假，可以申请通过，但是后两单将按照30%比例提成
                            // （当天请第二天假，后两单将按照30%比例提成）（弹出确定和取消按键）
                            alertMsg = "当天请第二天假，后两单将按照30%比例提成";
                        }

                        int days = getGapCount(startDate, endDate);
                        if (days >= 3) {
                            alertMsg+= "\n 下线天数超过3天（包含3天），需要店长审批后才能通过";
                        }


                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                    if (alertMsg == "") alertMsg = "您确定要下线吗？";


                    AlertDialog.Builder builder = new AlertDialog.Builder(ApplyLeaveActivity.this);  //先得到构造器
                    builder.setTitle("提示"); //设置标题
                    builder.setMessage(alertMsg); //设置内容
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { //设置确定按钮
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss(); //关闭dialog

                            String leave_day_type = sp_leave_day_type.getSelectedItem().toString();
                            String startDay = sp_start_day.getText().toString();
                            String end_day = sp_end_day.getText().toString();
                            String more = et_more.getText().toString();

                            getApplyLeave(startDay, end_day, leave_day_type, more);
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { //设置取消按钮
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();



                }


//                KeyBoardUtils.closeKeybord(sp_leave_day_type, ApplyLeaveActivity.this);

            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create();
        dialog.show();
    }


    private void showHelpDialog() {




        AlertDialog.Builder dialog = new AlertDialog.Builder(ApplyLeaveActivity.this);
        dialog.setTitle("下线管理规则");
        dialog.setMessage("亲爱的服务人员，下线需要提前两天下线，下线的天数不超过3天，在下线中没有需要服务的订单，就可以自己操作申请下线了，下列情况请大家注意一下：\n" +
                "1.下线日期中有需要服务的订单，不能申请下线。如需下线请联系客服进行调单处理.\n" +
                "2.当天操作下线，可以申请通过，但是后两单将按照30%比例提成.\n" +
                "3.当天操作第二天下线，可以申请通过，但是后两单将按照30%比例提成.\n" +
                "4.下线天数超过3天（包含3天），需要在APP上申请，店长审批通过后才能休假.\n" +
                "5.提前取消下线，当天和前一天下线的也按照30%提成执行。正常审批通过后下线的提成不变.");

        dialog.setNegativeButton("关闭", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create();
        dialog.show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case START_DATE_DIALOG:
                DatePickerDialog startDatePickerDialog = new DatePickerDialog(this, mStartdateListener, mYear, mMonth, mDay);
//                datePickerDialog.getDatePicker().setMinDate();
                return startDatePickerDialog;
            case END_DATE_DIALOG:
                DatePickerDialog endDatePickerDialog = new DatePickerDialog(this, mEnddateListener, mYear, mMonth, mDay);
//                datePickerDialog.getDatePicker().setMinDate();
                return endDatePickerDialog;
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener mStartdateListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            sp_start_day.setText(new StringBuffer().append(mYear).append("-").append(mMonth + 1).append("-").append(mDay));
        }
    };
    private DatePickerDialog.OnDateSetListener mEnddateListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            sp_end_day.setText(new StringBuffer().append(mYear).append("-").append(mMonth + 1).append("-").append(mDay));
        }
    };

    /**
     * 设置加载更多按钮状态
     *
     * @param isLoad 是否在加载
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

    /**
     * 获取订单列表
     */
    public void getLeaveList(int page) {
        if (!NetworkUtils.isNetworkConnected(ApplyLeaveActivity.this)) {
            Toast.makeText(ApplyLeaveActivity.this, getString(R.string.net_not_open), Toast.LENGTH_SHORT).show();
            return;
        }

        String staffid = SpFileUtil.getString(ApplyLeaveActivity.this, SpFileUtil.FILE_UI_PARAMETER, SpFileUtil.KEY_STAFF_ID, "");
        Map<String, String> map = new HashMap<String, String>();
        map.put("staff_id", staffid);
        map.put("page", page + "");
        AjaxParams param = new AjaxParams(map);

        showDialog();
        new FinalHttp().get(Constants.URL_GET_LEAVE_LIST, param, new AjaxCallBack<Object>() {


            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                setLoadMoreStatus(false);
                dismissDialog();
                Toast.makeText(ApplyLeaveActivity.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
                UIUtils.showTestToast(ApplyLeaveActivity.this, "errorMsg:" + strMsg);
            }

            @Override
            public void onSuccess(Object t) {
                super.onSuccess(t);
                setLoadMoreStatus(false);
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
                            if (pageIndex == 1) {
                                orderList.clear();
                            }
                            if (StringUtils.isNotEmpty(data)) {
                                Gson gson = new Gson();
                                secData = gson.fromJson(data, new TypeToken<ArrayList<LeaveEntity>>() {
                                }.getType());

                                if (null != secData && secData.size() > 0) {
                                    pageIndex += 1;
                                    layout_no_order.setVisibility(View.GONE);
                                    layout_order_list.setVisibility(View.VISIBLE);
                                    for (int i = 0; i < secData.size(); i++) {
                                        orderList.add(secData.get(i));
                                    }
                                    if (orderList.size() >= 10) {
                                        loadMoreButton.setVisibility(View.VISIBLE);
                                    } else {
                                        loadMoreButton.setVisibility(View.GONE);
                                    }

                                } else {
                                    if (pageIndex == 1) {
                                        layout_no_order.setVisibility(View.VISIBLE);
                                        layout_order_list.setVisibility(View.GONE);
                                    } else {
                                        layout_order_list.setVisibility(View.VISIBLE);
                                        layout_no_order.setVisibility(View.GONE);
                                        Toast.makeText(ApplyLeaveActivity.this, "没有更多数据了", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                listadapter.setData(orderList);
                            } else {
                                layout_no_order.setVisibility(View.VISIBLE);
                                layout_order_list.setVisibility(View.GONE);
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
                    UIUtils.showToast(ApplyLeaveActivity.this, errorMsg);
                }
            }
        });

    }

    /**
     * 申请下线接口
     *
     * @param startDay
     * @param sp_end_day
     * @param leave_day_type
     * @param more
     */
    public void getApplyLeave(String startDay, String sp_end_day, String leave_day_type, String more) {
        String staffid = SpFileUtil.getString(ApplyLeaveActivity.this, SpFileUtil.FILE_UI_PARAMETER, SpFileUtil.KEY_STAFF_ID, "");
        Map<String, String> map = new HashMap<String, String>();
        map.put("staff_id", staffid);
        map.put("id", "0");
        map.put("leaveDate", startDay);
        map.put("leaveDateEnd", sp_end_day);
        if (StringUtils.isEquals(leave_day_type, "全天")) {
            map.put("halfDay", "0");
        } else if (StringUtils.isEquals(leave_day_type, "上午")) {
            map.put("halfDay", "1");
        } else if (StringUtils.isEquals(leave_day_type, "下午")) {
            map.put("halfDay", "2");
        }

        map.put("leaveStatus", "1");
        map.put("remarks", more);
        AjaxParams param = new AjaxParams(map);

        showDialog();
        new FinalHttp().post(Constants.URL_GET_DO_LEAVE, param, new AjaxCallBack<Object>() {


            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                setLoadMoreStatus(false);
                dismissDialog();
                Toast.makeText(ApplyLeaveActivity.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
                UIUtils.showTestToast(ApplyLeaveActivity.this, "errorMsg:" + strMsg);
            }

            @Override
            public void onSuccess(Object t) {
                super.onSuccess(t);
                setLoadMoreStatus(false);
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
                            UIUtils.showToast(ApplyLeaveActivity.this, "申请成功");
                            pageIndex = 1;
                            getLeaveList(pageIndex);
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
                    UIUtils.showToast(ApplyLeaveActivity.this, errorMsg);
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

    }


    /**
     * 刷新
     */
    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefresh(LeaveEntity leaveEntity) {
        pageIndex = 1;
        getLeaveList(pageIndex);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 获取两个日期之间的间隔天数
     * @return
     */
    public static int getGapCount(Date startDate, Date endDate) {
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(startDate);
        fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
        fromCalendar.set(Calendar.MINUTE, 0);
        fromCalendar.set(Calendar.SECOND, 0);
        fromCalendar.set(Calendar.MILLISECOND, 0);

        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(endDate);
        toCalendar.set(Calendar.HOUR_OF_DAY, 0);
        toCalendar.set(Calendar.MINUTE, 0);
        toCalendar.set(Calendar.SECOND, 0);
        toCalendar.set(Calendar.MILLISECOND, 0);

        return (int) ((toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (1000 * 60 * 60 * 24));
    }

}
