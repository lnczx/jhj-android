package com.meijialife.dingdang.fra;

import java.util.HashMap;
import java.util.Map;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.meijialife.dingdang.Constants;
import com.meijialife.dingdang.R;
import com.meijialife.dingdang.activity.MoreActivity;
import com.meijialife.dingdang.activity.PersonAccountCenterActivity;
import com.meijialife.dingdang.activity.PersonInfoActivity;
import com.meijialife.dingdang.activity.PersonPayDetailActivity;
import com.meijialife.dingdang.activity.ShareActivity;
import com.meijialife.dingdang.activity.UserListActivity;
import com.meijialife.dingdang.activity.WebViewActivity;
import com.meijialife.dingdang.bean.UserIndexData;
import com.meijialife.dingdang.ui.RoundImageView;
import com.meijialife.dingdang.utils.LogOut;
import com.meijialife.dingdang.utils.NetworkUtils;
import com.meijialife.dingdang.utils.SpFileUtil;
import com.meijialife.dingdang.utils.StringUtils;
import com.meijialife.dingdang.utils.UIUtils;

/**
 
 * 
 */
public class PersonalPageFragment extends Fragment implements OnClickListener {

    private RoundImageView iv_top_head;
    public static UserIndexData userIndexData;
    private FinalBitmap finalBitmap;
    private BitmapDrawable defDrawable;

    private View v;

    private ProgressDialog m_pDialog;
    private TextView tv_mobile;
    private TextView tv_auth_status;
    private TextView tv_total_order;
    private TextView tv_total_incoming;
    private RoundImageView iv_headimg;
    private String staffid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.index_4, null);
        
        init(v);
        getUserData();
        return v;
    }

    private void init(View view) {
        finalBitmap = FinalBitmap.create(getActivity());

        view.findViewById(R.id.layout_info).setOnClickListener(this);
        view.findViewById(R.id.layout_account_center).setOnClickListener(this);
        view.findViewById(R.id.layout_college).setOnClickListener(this);
        view.findViewById(R.id.layout_share_friend).setOnClickListener(this);
        view.findViewById(R.id.layout_more).setOnClickListener(this);
        view.findViewById(R.id.layout_user_list).setOnClickListener(this);
        view.findViewById(R.id.layout_mingxi_shouru).setOnClickListener(this);

        tv_mobile = (TextView) view.findViewById(R.id.tv_mobile);
        tv_auth_status = (TextView) view.findViewById(R.id.tv_auth_status);
        tv_total_order = (TextView) view.findViewById(R.id.tv_total_order);
        tv_total_incoming = (TextView) view.findViewById(R.id.tv_total_incoming);

        iv_headimg = (RoundImageView) view.findViewById(R.id.iv_headimg);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View arg0) {
        Intent intent = null;
        switch (arg0.getId()) {
        case R.id.layout_info:
            intent = new Intent(getActivity(), PersonInfoActivity.class);
            intent.putExtra("userIndexData", userIndexData);
            break;
        case R.id.layout_account_center:
            intent = new Intent(getActivity(), PersonAccountCenterActivity.class);
            break;
        case R.id.layout_college:
            // intent = new Intent(getActivity(), PersonCollageActivity.class);
            intent = new Intent(getActivity(), WebViewActivity.class);
            intent.putExtra("url", Constants.URL_GET_UNIVERSITY + "?staff_id=" + staffid);
            intent.putExtra("title", "叮当大学");
            break;
        case R.id.layout_share_friend:
            intent = new Intent(getActivity(), ShareActivity.class);
            break;
        case R.id.layout_more:
            intent = new Intent(getActivity(), MoreActivity.class);
            break;
        case R.id.layout_user_list://跳转到用户列表
        	intent = new Intent(getActivity(), UserListActivity.class);
        	break;
        case R.id.layout_mingxi_shouru://跳转到明细
            intent = new Intent(getActivity(), PersonPayDetailActivity.class);
            break;
        default:
            break;
        }
        if (intent != null) {
            startActivity(intent);
        }

    }

    /**
     * 获取个人信息数据
     */
    private void getUserData() {
        if (!NetworkUtils.isNetworkConnected(getActivity())) {
            Toast.makeText(getActivity(), getString(R.string.net_not_open), 0).show();
            return;
        }

        staffid = SpFileUtil.getString(getActivity(), SpFileUtil.FILE_UI_PARAMETER, SpFileUtil.KEY_STAFF_ID, "");
        Map<String, String> map = new HashMap<String, String>();
        map.put("user_id", staffid);
        AjaxParams param = new AjaxParams(map);

        // showDialog();
        new FinalHttp().get(Constants.URL_GET_USER_INFO, param, new AjaxCallBack<Object>() {
            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                UIUtils.showTestToast(getActivity(), "errorMsg:" + strMsg);
                dismissDialog();
                Toast.makeText(getActivity(), getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(Object t) {
                super.onSuccess(t);
                String errorMsg = "";
                // dismissDialog();
                LogOut.i("========", "onSuccess：" + t);
                // UIUtils.showTestToast(getActivity(), t.toString());
                try {
                    if (StringUtils.isNotEmpty(t.toString())) {
                        JSONObject obj = new JSONObject(t.toString());
                        int status = obj.getInt("status");
                        String msg = obj.getString("msg");
                        String data = obj.getString("data");
                        if (status == Constants.STATUS_SUCCESS) { // 正确
                            if (StringUtils.isNotEmpty(data)) {
                                Gson gson = new Gson();
                                userIndexData = gson.fromJson(data, UserIndexData.class);
                                showData(userIndexData);
                            } /*else {
                                UIUtils.showToast(getActivity(), "数据错误");
                            }*/
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
                    UIUtils.showToast(getActivity(), errorMsg);
                }
            }
        });

    }

    private void showData(UserIndexData userIndexData) {
        if (null == userIndexData) {
            return;
        }

        tv_mobile.setText(userIndexData.getMobile());
        int auth_status = userIndexData.getAuth_status();
        if (auth_status == 0) {
            tv_auth_status.setText("身份未认证");

        } else if (auth_status == 1) {
            tv_auth_status.setText("身份已验证");
        }

        tv_total_order.setText(Long.toString(userIndexData.getTotal_order()) + "单");
        tv_total_incoming.setText(userIndexData.getTotal_incoming() + "元");

        String head_img = userIndexData.getHead_img();
        if (StringUtils.isNotEmpty(head_img)) {
            finalBitmap.display(iv_headimg, userIndexData.getHead_img());
        }

    }

    public void showDialog() {
        if (m_pDialog == null) {
            m_pDialog = new ProgressDialog(getActivity());
            m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            m_pDialog.setMessage("请稍等...");
            m_pDialog.setIndeterminate(false);
            m_pDialog.setCancelable(false);
        }
        m_pDialog.show();
    }

    public void dismissDialog() {
        if (m_pDialog != null && m_pDialog.isShowing()) {
            m_pDialog.hide();
        }
    }

}
