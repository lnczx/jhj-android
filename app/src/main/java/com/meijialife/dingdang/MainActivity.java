package com.meijialife.dingdang;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.MyLocationData;
import com.meijialife.dingdang.bean.UpdateInfo;
import com.meijialife.dingdang.broadcastReceiver.PostTrailService;
import com.meijialife.dingdang.fra.Home1Fra;
import com.meijialife.dingdang.fra.Home2Fra;
import com.meijialife.dingdang.fra.PersonalPageFragment;
import com.meijialife.dingdang.service.LocationReportService;
import com.meijialife.dingdang.utils.BasicToolUtil;
import com.meijialife.dingdang.utils.LogOut;
import com.meijialife.dingdang.utils.NetworkUtils;
import com.meijialife.dingdang.utils.SpFileUtil;
import com.meijialife.dingdang.utils.StringUtils;
import com.meijialife.dingdang.utils.UIUtils;
import com.meijialife.dingdang.utils.UpdateInfoProvider;
import com.meijialife.dingdang.utils.Utils;

/**
 * fragment 的切换类
 * 
 * @author RUI
 * 
 */
public class MainActivity extends FragmentActivity implements OnClickListener {

    protected static final String TAG = "MainActivity";

    private TextView tv_header;
    private LinearLayout mBt1, mBt2, mBt3;
    private FragmentManager mFM = null;
    private int currentTabIndex; // 1=首页 2=发现 3=秘友 4=我的
    public static Activity activity;

    private LocationClient mLocClient;// 定位相关
    public MyLocationListenner myListener = new MyLocationListenner(this);
    public static String clientid;

    private final String ACTION_DISPATCH = "dispatch";// 派工
    private final String ACTION_MSG = "msg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.layout_main);
        super.onCreate(savedInstanceState);
        initLocation();
        init();

        // mBt1.performClick();
        layout_mask = findViewById(R.id.layout_mask);
        layout_guide = findViewById(R.id.layout_guide);

        staffid = SpFileUtil.getString(getApplicationContext(), SpFileUtil.FILE_UI_PARAMETER, SpFileUtil.KEY_STAFF_ID, "");

        Timer timer = new Timer();
        TimerTask MyTask = new TimerTask() {
            @Override
            public void run() {
                if (StringUtils.isNotEmpty(clientid)) {
                    bind_user(clientid);
                }
            }
        };
        timer.schedule(MyTask, 2000);

        try {
            String action = getIntent().getExtras().getString("mAction");
            if (StringUtils.isNotEmpty(action) && StringUtils.isEquals(action, ACTION_DISPATCH)) {
                mBt2.performClick();
            } else {
                mBt1.performClick();
            }
        } catch (Exception e) {
            mBt1.performClick();
        }

        /*
         * try {//友盟更新 UmengUpdateAgent.setUpdateOnlyWifi(false); UmengUpdateAgent.update(this);// 友盟自动更新接口 } catch (Exception e) {
         * e.printStackTrace(); }
         */

        checkVersion(true);// 版本更新检测
        // 启动服务发送地理位置
        Intent intent = new Intent(MainActivity.this, PostTrailService.class);
        startService(intent);
    }

    /**
     * 检查是否第一次打开应用
     * 
     * @return
     */
    private boolean getFristFlag() {

        boolean FristFlag = SpFileUtil.getBoolean(this, SpFileUtil.FILE_UI_PARAMETER, SpFileUtil.KEY_FIRST_INTO, true);

        return FristFlag;
    }

    /**
     * 更新第一次打开应用的标识
     */
    private void updateFristFlag() {
        SpFileUtil.saveBoolean(this, SpFileUtil.FILE_UI_PARAMETER, SpFileUtil.KEY_FIRST_INTO, false);

    }

    private void init() {
        activity = this;
        view_title_bar = (RelativeLayout) findViewById(R.id.view_title_bar);
        tv_header = (TextView) findViewById(R.id.header_tv_name);
        mBt1 = (LinearLayout) findViewById(R.id.tab_bt_1);
        mBt2 = (LinearLayout) findViewById(R.id.tab_bt_2);
        mBt3 = (LinearLayout) findViewById(R.id.tab_bt_3);

        mBt1.setOnClickListener(this);
        mBt2.setOnClickListener(this);
        mBt3.setOnClickListener(this);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

    }

    /**
     * 切换到消息Fragment
     */
    public void change2IM() {

        view_title_bar.setVisibility(View.VISIBLE);
        setSelected(mBt3);
        updateTitle(3);

    }

    /**
     * 切换fragement
     */
    private void change(Fragment fragment) {
        if (null == mFM)
            mFM = getSupportFragmentManager();
        FragmentTransaction ft = mFM.beginTransaction();
        ft.replace(R.id.content_container, fragment);
        ft.commit();
    }

    private void setSelected(LinearLayout btn) {
        mBt1.setSelected(false);
        mBt2.setSelected(false);
        mBt3.setSelected(false);

        btn.setSelected(true);
    }

    /**
     * 更新标题内容
     */
    private void updateTitle(int index) {
        switch (index) {
        case 1:
            tv_header.setText("首页");
            break;
        case 2:
            tv_header.setText("订单列表");
            break;
        case 3:
            tv_header.setText("我的");
            break;

        default:
            break;
        }
    }

    private static Boolean isQuit = false;
    private Timer timer = new Timer();
    private RelativeLayout view_title_bar;

    private View layout_mask;

    private View layout_guide;

    private String staffid;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (isQuit == false) {
                isQuit = true;
                Toast.makeText(getBaseContext(), "再次点击确定退出软件", Toast.LENGTH_SHORT).show();
                TimerTask task = null;
                task = new TimerTask() {
                    @Override
                    public void run() {
                        isQuit = false;
                    }
                };
                timer.schedule(task, 2000);
            } else {
                moveTaskToBack(false);
                finish();
            }
        } else {
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStop() {

        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 退出时销毁定位
        mLocClient.stop();
    }

    @Override
    public void onClick(View arg0) {
        if (BasicToolUtil.isFastClick(this)) {
            return;
        }
        Intent intent;
        switch (arg0.getId()) {
        case R.id.tab_bt_1: // 首页

            currentTabIndex = 1;

            change(new Home1Fra());
            setSelected(mBt1);
            updateTitle(1);

            view_title_bar.setVisibility(View.GONE);
            break;
        case R.id.tab_bt_2: // 发现
            currentTabIndex = 2;// //
            change(new Home2Fra());
            setSelected(mBt2);
            updateTitle(2);
            view_title_bar.setVisibility(View.VISIBLE);
            break;
        case R.id.tab_bt_3: // 我的
            currentTabIndex = 3;
            change(new PersonalPageFragment());
            setSelected(mBt3);
            updateTitle(3);
            view_title_bar.setVisibility(View.VISIBLE);
            break;

        default:
            break;
        }
    }

    /**
     * 初始化百度定位
     */
    private void initLocation() {
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        option.setIsNeedAddress(true);
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        private Context context;

        public MyLocationListenner(Context context) {
            this.context = context;
        }

        @Override
        public void onReceiveLocation(BDLocation location) {

            if (location == null)
                return;

            MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius())
            // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude()).longitude(location.getLongitude()).build();

            if (location.hasAddr()) {// 如果有地址信息
                String addStr = location.getAddrStr();// 详细地址信息
                String province = location.getProvince();// 省份
                String city = location.getCity();// 城市
                String street = location.getStreet();// 街道
                String lat = location.getLatitude() + "";
                String lng = location.getLongitude() + "";

                // listener.onLocation(addStr, lat, lng, city);
                // String add = ""+addStr + "\n" + province + "\n" + city + "\n"
                // + street + "\n" + lat + "\n" + lng;
                // LogOut.i("address", add);

                if (StringUtils.isNotEmpty(lat) && StringUtils.isNotEmpty(lng) && StringUtils.isNotEmpty(addStr)) {
                    sendLocation(lat, lng, addStr);
                }
                // 如果已经得到位置，销毁定位
                mLocClient.stop();

            }

        }

        public void onReceivePoi(BDLocation poiLocation) {

        }
    }

    /**
     * 发送地理位置接口
     * 
     * @param city
     * @param lng
     */
    private void sendLocation(String lat, String lng, String city) {

        if (!NetworkUtils.isNetworkConnected(MainActivity.this)) {
            Toast.makeText(MainActivity.this, getString(R.string.net_not_open), 0).show();
            return;
        }

        Map<String, String> map = new HashMap<String, String>();
        map.put("user_id", staffid);
        map.put("user_type", "0");
        map.put("lat", lat);// weidu
        map.put("lng", lng);// jingdu
        map.put("poi_name", city);
        AjaxParams param = new AjaxParams(map);
        new FinalHttp().post(Constants.URL_POST_USER_TRAIL, param, new AjaxCallBack<Object>() {
            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
//                UIUtils.showTestToast(MainActivity.this, "发送位置errorMsg:" + strMsg);
                UIUtils.showToast(MainActivity.this, "发送位置失败");
            }

            @Override
            public void onSuccess(Object t) {
                super.onSuccess(t);
                String errorMsg = "";
                LogOut.i("========", "发送位置 onSuccess：" + t);
                // UIUtils.showTestToastLong(MainActivity.this,
                // "发送位置 返回数据：" + t.toString());
                try {
                    if (StringUtils.isNotEmpty(t.toString())) {
                        JSONObject obj = new JSONObject(t.toString());
                        int status = obj.getInt("status");
                        String msg = obj.getString("msg");
                        String data = obj.getString("data");
                        if (status == Constants.STATUS_SUCCESS) { // 正确
                            /*
                             * UIUtils.showToast(MainActivity.this, "发送位置成功");
                             */
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
                    UIUtils.showToast(MainActivity.this, errorMsg);
                }
            }
        });

    }

    /**
     * 绑定接口
     * 
     * @param date
     */
    private void bind_user(String client_id) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("user_id", staffid);
        map.put("user_type", "0");
        map.put("device_type", "android");
        map.put("client_id", client_id);
        AjaxParams param = new AjaxParams(map);

        new FinalHttp().post(Constants.URL_POST_PUSH_BIND, param, new AjaxCallBack<Object>() {
            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                UIUtils.showTestToast(MainActivity.this, "绑定clientid：" + strMsg);
                Toast.makeText(MainActivity.this, MainActivity.this.getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(Object t) {
                super.onSuccess(t);
                String errorMsg = "";
                LogOut.i("========", "onSuccess：" + t);
                // UIUtils.showTestToast(MainActivity.this, "推送绑定结果：" +
                // t.toString());
                try {
                    if (StringUtils.isNotEmpty(t.toString())) {
                        JSONObject obj = new JSONObject(t.toString());
                        int status = obj.getInt("status");
                        String msg = obj.getString("msg");
                        String data = obj.getString("data");
                        if (status == Constants.STATUS_SUCCESS) { // 正确
                            /*
                             * UIUtils.showToast(MainActivity.this, "推送绑定成功");
                             */
                        } else if (status == Constants.STATUS_SERVER_ERROR) { // 服务器错误
                            errorMsg = MainActivity.this.getString(R.string.servers_error);
                        } else if (status == Constants.STATUS_PARAM_MISS) { // 缺失必选参数
                            errorMsg = MainActivity.this.getString(R.string.param_missing);
                        } else if (status == Constants.STATUS_PARAM_ILLEGA) { // 参数值非法
                            errorMsg = MainActivity.this.getString(R.string.param_illegal);
                        } else if (status == Constants.STATUS_OTHER_ERROR) { // 999其他错误
                            errorMsg = msg;
                        } else {
                            errorMsg = MainActivity.this.getString(R.string.servers_error);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    errorMsg = MainActivity.this.getString(R.string.servers_error);

                }
                // 操作失败，显示错误信息|
                if (!StringUtils.isEmpty(errorMsg.trim())) {
                    UIUtils.showToast(MainActivity.this, errorMsg);
                }
            }
        });
    }

    private UpdateInfo updateInfo; // APP版本更新数据
    private ProgressDialog progDlg;
    // 当前应用版本号
    private String curVersion;
    private static final int CODE_VERSION_MANUAL_OK = 100; // 用户主动检测版本成功
    private static final int CODE_VERSION_AUTO_OK = 101; // 系统自动检测版本
    private static final int CODE_VERSION_ERROR = 102; // 检测版本数据解析失败
    private static final int DOWN_ERROR = 103; // 下载失败

    Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
            case CODE_VERSION_MANUAL_OK:
                if (progDlg != null) {
                    dismissDialog();
                }
                compareVersion(true);
                break;
            case CODE_VERSION_AUTO_OK:
                compareVersion(false);
                break;
            case CODE_VERSION_ERROR:
                if (progDlg != null) {
                    dismissDialog();
                }
                break;
            case DOWN_ERROR:
                Toast.makeText(getApplicationContext(), "下载新版本失败", 1).show();
                break;
            default:
                break;
            }
        };
    };

    /**
     * 检查更新
     * 
     * @param isManual
     *            是否人工点击
     */
    public void checkVersion(final boolean isManual) {
        /*
         * if (isManual) { showDialog(); }
         */
        new Thread(new Runnable() {
            public void run() {
                try {
                    URL url = new URL(Constants.URL_GET_VERSION);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    // 连接超时时间
                    conn.setConnectTimeout(5000);
                    int code = conn.getResponseCode();
                    if (code == 200) {
                        InputStream is = conn.getInputStream();
                        updateInfo = new UpdateInfo();
                        updateInfo = UpdateInfoProvider.getUpdateInfo(is);
                        if (updateInfo != null) {
                            // 解析成功
                            if (isManual) {
                                mHandler.sendEmptyMessage(CODE_VERSION_MANUAL_OK);
                            } else {
                                mHandler.sendEmptyMessage(CODE_VERSION_AUTO_OK);
                            }
                        } else {
                            // 解析失败
                            mHandler.sendEmptyMessage(CODE_VERSION_ERROR);
                        }
                    } else {
                        mHandler.sendEmptyMessage(CODE_VERSION_ERROR);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    mHandler.sendEmptyMessage(CODE_VERSION_ERROR);
                }
            }
        }).start();
    }

    /**
     * 对比APP版本号
     * 
     * @param isManual
     *            是否用户主动对比的
     */
    private void compareVersion(boolean isManual) {
        String newVersion = updateInfo.getVersion();
        curVersion = getCurVersion();
        int visibility;
        if (!newVersion.equals(curVersion) && diffVersion(newVersion, curVersion) > 0) { // 有更新
            visibility = View.VISIBLE;
            if (isManual) {
                showVersionDlg();
            }
        } else {
            visibility = View.INVISIBLE;
            if (isManual) {
                Toast.makeText(this, "已是最新版本！", 0).show();
            }
        }
    }

    public int diffVersion(String s1, String s2) {
        if (s1 == null && s2 == null)
            return 0;
        else if (s1 == null)
            return -1;
        else if (s2 == null)
            return 1;
        String[] arr1 = s1.split("[^a-zA-Z0-9]+"), arr2 = s2.split("[^a-zA-Z0-9]+");
        int i1, i2, i3;
        for (int ii = 0, max = Math.min(arr1.length, arr2.length); ii <= max; ii++) {
            if (ii == arr1.length)
                return ii == arr2.length ? 0 : -1;
            else if (ii == arr2.length)
                return 1;
            try {
                i1 = Integer.parseInt(arr1[ii]);
            } catch (Exception x) {
                i1 = Integer.MAX_VALUE;
            }
            try {
                i2 = Integer.parseInt(arr2[ii]);
            } catch (Exception x) {
                i2 = Integer.MAX_VALUE;
            }

            if (i1 != i2) {
                return i1 - i2;
            }
            i3 = arr1[ii].compareTo(arr2[ii]);

            if (i3 != 0)
                return i3;
        }
        return 0;
    }

    /**
     * 更新提示
     */
    private void showVersionDlg() {
        String msg = "版本：" + updateInfo.getVersion() + "\n" + updateInfo.getDescription();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("发现新版本");
        builder.setMessage(msg);
        builder.setPositiveButton("立即升级", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                downLoadApk();
            }
        });
        builder.setNegativeButton("以后再说", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        AlertDialog dlg = builder.create();
        dlg.show();
    }

    /*
     * 从服务器中下载APK
     */
    protected void downLoadApk() {
        final ProgressDialog pd; // 进度条对话框
        pd = new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("正在下载更新");
        pd.show();
        new Thread() {
            @Override
            public void run() {
                try {
                    File file = getFileFromServer(updateInfo.getPath(), pd);
                    sleep(3000);
                    installApk(file);
                    pd.dismiss(); // 结束掉进度条对话框
                } catch (Exception e) {
                    Message msg = new Message();
                    msg.what = DOWN_ERROR;
                    mHandler.sendMessage(msg);
                    e.printStackTrace();
                }
            }
        }.start();
    }

    // 安装apk
    protected void installApk(File file) {
        Intent intent = new Intent();
        // 执行动作
        intent.setAction(Intent.ACTION_VIEW);
        // 执行的数据类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);
    }

    /**
     * 获取当前版本号
     * 
     */
    private String getCurVersion() {
        if (null == curVersion) {
            curVersion = String.valueOf(Utils.getCurVerName(this));
        }

        return curVersion;
    }

    private ProgressDialog m_pDialog;

    public void dismissDialog() {
        if (m_pDialog != null && m_pDialog.isShowing()) {
            // m_pDialog.hide();
            m_pDialog.dismiss();
            m_pDialog = null;
        }
    }

    public void showDialog() {
        if (m_pDialog == null) {
            m_pDialog = new ProgressDialog(this);
            m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            m_pDialog.setMessage("请稍等...");
            m_pDialog.setIndeterminate(false);
            m_pDialog.setCancelable(false);
        }
        m_pDialog.show();
    }

    public static File getFileFromServer(String path, ProgressDialog pd) throws Exception {
        // 如果相等的话表示当前的sdcard挂载在手机上并且是可用的
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            // 获取到文件的大小
            pd.setMax(conn.getContentLength());
            InputStream is = conn.getInputStream();
            File file = new File(Environment.getExternalStorageDirectory(), "updata.apk");
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            int total = 0;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                total += len;
                // 获取当前下载量
                pd.setProgress(total);
            }
            fos.close();
            bis.close();
            is.close();
            return file;
        } else {
            return null;
        }
    }

}
