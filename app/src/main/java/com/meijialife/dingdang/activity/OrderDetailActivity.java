package com.meijialife.dingdang.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;

import android.Manifest;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.poi.BaiduMapPoiSearch;
import com.baidu.mapapi.utils.poi.PoiParaOption;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.meijialife.dingdang.BaseActivity;
import com.meijialife.dingdang.Constants;
import com.meijialife.dingdang.R;
import com.meijialife.dingdang.adapter.OrderListDetailsAdapter;
import com.meijialife.dingdang.adapter.PublishPhotoAdapter;
import com.meijialife.dingdang.bean.OrderListVo;
import com.meijialife.dingdang.bean.OrderListVo.ServiceAddonsBean;
import com.meijialife.dingdang.bean.ThumbnailImage;
import com.meijialife.dingdang.bean.UserIndexData;
import com.meijialife.dingdang.picker.MultiSelector;
import com.meijialife.dingdang.picker.view.SquaredImageView;
import com.meijialife.dingdang.service.LocationReportAgain;
import com.meijialife.dingdang.ui.FrescoPhotoView;
import com.meijialife.dingdang.ui.ListViewForInner;
import com.meijialife.dingdang.ui.NoScrollGridView;
import com.meijialife.dingdang.ui.TagGroup;
import com.meijialife.dingdang.ui.ToggleButton;
import com.meijialife.dingdang.ui.ToggleButton.OnToggleChanged;
import com.meijialife.dingdang.utils.AgentApi;
import com.meijialife.dingdang.utils.CommonUtil;
import com.meijialife.dingdang.utils.InputMethodUtils;
import com.meijialife.dingdang.utils.KeyBoardUtils;
import com.meijialife.dingdang.utils.LogOut;
import com.meijialife.dingdang.utils.NetworkUtils;
import com.meijialife.dingdang.utils.SpFileUtil;
import com.meijialife.dingdang.utils.StringUtils;
import com.meijialife.dingdang.utils.UIUtils;
import com.meijialife.dingdang.utils.image.BitmapUtil;

import android.support.v4.content.PermissionChecker;

/**
 * 订单详情
 *
 * @author windows7
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
    private String urlString;
    private Short order_type;
    private EditText et_input_content;
    private EditText et_input_money;
    private TextView tv_order_money;
    private Short order_status;
    private LinearLayout layout_fuwu_shichang;
    private TextView tv_order_shichang;
    private TextView tv_service_time_type;
    private TextView tv_order_remarks;
    private TextView tv_order_no;
    private TextView tv_order_time;
    private TextView tv_order_xiangmu;
    private TextView tv_order_incoming;
    private TextView tv_order_status;
    private TextView tv_service_type_name;
    private TextView btn_order_start_work;
    private TextView btn_order_add_hour;
    private TextView tv_call_phone;
    private TextView tv_goto_address;
    private TextView tv_input_money;
    private TextView tv_input_content;
    private TextView tv_order_addr;
    private TextView tv_user_type;
    private String order_id;
    private LinearLayout layout_goutong_des;
    private ToggleButton slipBtn;
    private boolean isSelect;
    private LinearLayout layout_server_details;
    private ListViewForInner listview_details;
    private OrderListDetailsAdapter orderListDetails;
    private LinearLayout layout_add_hour;
    private TextView tv_add_hour, tv_order_from, layout_show_Images_title;
    final String arr[] = new String[]{"0", "1", "2", "3", "4", "5", "6",};

    //photo picker
    private static final int REQUEST_IMAGE = 1;
    private static final int REQUEST_VIDEO = 3;
    private static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION = 101;
    private static final int REQUEST_VIDEO_VIEW = 4;

    private boolean isImage = Boolean.TRUE;
    private ArrayList<String> mSelectPath;
    public static final int REQUEST_IMAGE_VIEW = 2;
    private TextView mTvChoose, mTvChoosePs;
    private ImageView mIvChooseImg, mIvChooseVideo;
    //layout image、video
    private RelativeLayout mLayoutVideo;
    //photo
    private NoScrollGridView mGridViewPhoto;
    private PublishPhotoAdapter mPublishPhotoAdapter;
    //hashtag、choose
    private LinearLayout mLayoutChoose, layout_show_Images;
    private LinkedHashMap<String, File> files = null;
    private ArrayList<ThumbnailImage> tempFiles;
    private RelativeLayout layout_choose_title;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1://compress failed
                    Toast.makeText(OrderDetailActivity.this, "上传失败,请检查图片再次发布", Toast.LENGTH_SHORT).show();
                    break;

                case 3:
                    dismissDialog();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.layout_order_detail);
        super.onCreate(savedInstanceState);

        order_id = getIntent().getExtras().getString("order_id");

        initView();

        getOrderListDetail(order_id);

        mSelectPath = new ArrayList<>();

    }

    private void initView() {
        setTitleName("订单详情");
        requestBackBtn();
        title_btn_right = (ImageView) findViewById(R.id.title_btn_right);

        // 3个图标状态
        tv_order_over = (TextView) findViewById(R.id.tv_order_over);
        tv_order_tiaozheng = (TextView) findViewById(R.id.tv_order_tiaozheng);
        tv_order_start = (TextView) findViewById(R.id.tv_order_start);
        iv_order_over = (ImageView) findViewById(R.id.iv_order_over);
        iv_order_tiaozheng = (ImageView) findViewById(R.id.iv_order_tiaozheng);
        iv_order_start = (ImageView) findViewById(R.id.iv_order_start);

        layout_order_tiaozheng = (LinearLayout) findViewById(R.id.layout_order_tiaozheng);
        layout_fuwu_shichang = (LinearLayout) findViewById(R.id.layout_fuwu_shichang);
        layout_goutong_des = (LinearLayout) findViewById(R.id.layout_goutong_des);
        layout_order_tiaozheng_line = (View) findViewById(R.id.layout_order_tiaozheng_line);

        tv_order_remarks = (TextView) findViewById(R.id.tv_order_remarks);
        tv_order_no = (TextView) findViewById(R.id.tv_order_no);
        tv_order_time = (TextView) findViewById(R.id.tv_order_time);
        tv_order_xiangmu = (TextView) findViewById(R.id.tv_order_xiangmu);
        tv_order_money = (TextView) findViewById(R.id.tv_order_money);
        tv_order_incoming = (TextView) findViewById(R.id.tv_order_incoming);
        tv_order_status = (TextView) findViewById(R.id.tv_order_status);
        tv_service_type_name = (TextView) findViewById(R.id.tv_service_type_name);
        btn_order_start_work = (TextView) findViewById(R.id.btn_order_start_work);
        btn_order_add_hour = (TextView) findViewById(R.id.btn_order_add_hour);
        tv_call_phone = (TextView) findViewById(R.id.tv_call_phone);
        tv_goto_address = (TextView) findViewById(R.id.tv_goto_address);
        iv_order_type = (ImageView) findViewById(R.id.iv_order_type);
        tv_order_shichang = (TextView) findViewById(R.id.tv_order_shichang);
        tv_service_time_type = (TextView) findViewById(R.id.tv_service_time_type);
        tv_order_addr = (TextView) findViewById(R.id.tv_order_addr);
        tv_user_type = (TextView) findViewById(R.id.tv_user_type);
        slipBtn = (ToggleButton) findViewById(R.id.slipBtn_fatongzhi);
        layout_add_hour = (LinearLayout) findViewById(R.id.layout_add_hour);
        tv_add_hour = (TextView) findViewById(R.id.tv_add_hour);
        tv_order_from = (TextView) findViewById(R.id.tv_order_from);
        layout_show_Images_title = (TextView) findViewById(R.id.layout_show_Images_title);

        /*
         * slipBtn.setOnToggleChanged(new OnToggleChanged() {
         * 
         * @Override public void onToggle(boolean on) { if (on) { isSelect = true; } else { isSelect = false; } } });
         */

        // 调整订单

        tv_input_content = (TextView) findViewById(R.id.tv_input_content);
        tv_input_money = (TextView) findViewById(R.id.tv_input_money);
        et_input_content = (EditText) findViewById(R.id.et_input_content);
        et_input_money = (EditText) findViewById(R.id.et_input_order_money);

        layout_server_details = (LinearLayout) findViewById(R.id.layout_server_details);
        listview_details = (ListViewForInner) findViewById(R.id.listview_details);


        //选择照片
        mTvChoose = (TextView) findViewById(R.id.tv_choose);
        mTvChoosePs = (TextView) findViewById(R.id.tv_choose_ps);
        mIvChooseImg = (ImageView) findViewById(R.id.btn_choose_image);
        mGridViewPhoto = (NoScrollGridView) findViewById(R.id.gridview);
        mLayoutChoose = (LinearLayout) findViewById(R.id.layout_choose);
        layout_show_Images = (LinearLayout) findViewById(R.id.layout_show_Images);

        layout_choose_title = (RelativeLayout) findViewById(R.id.layout_choose_title);

        mIvChooseImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                UIUtils.showToast(OrderDetailActivity.this, "去选择图片");
                pickImage();
            }
        });
    }


    /**
     * 挑选图片
     */
    public void pickImage() {
        if (!verifyPermission(Manifest.permission.CAMERA)) {
            Toast.makeText(this, getResources().getString(R.string.mis_permission_no_camera), Toast.LENGTH_LONG).show();
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            isImage = true;
            requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
                    getString(R.string.permission_rationale),
                    REQUEST_STORAGE_READ_ACCESS_PERMISSION);
        } else {

            MultiSelector selector = MultiSelector.create();
            selector.showCamera(true);//附带拍照
            selector.count(3);//最多3张
            selector.multi();//多选
            selector.origin(mSelectPath);//已选取的图片
            selector.start(OrderDetailActivity.this, REQUEST_IMAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_STORAGE_READ_ACCESS_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (isImage) {
                    pickImage();
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * 删除单张图片
     */
    public void deleteSinglePhoto(int position) {
        mSelectPath.remove(position);
        mPublishPhotoAdapter = new PublishPhotoAdapter(OrderDetailActivity.this, mSelectPath);
        mGridViewPhoto.setAdapter(mPublishPhotoAdapter);
        updatePhotoChoosedStatus();
    }

    private void updatePhotoChoosedStatus() {
        if (mSelectPath.size() != 0) {
            mTvChoose.setText("已选择" + mSelectPath.size() + "张照片");
            mTvChoosePs.setVisibility(View.VISIBLE);
            mGridViewPhoto.setVisibility(View.VISIBLE);
            mLayoutChoose.setVisibility(View.GONE);
//            mLayoutChoose.setVisibility(View.VISIBLE);
        } else {
            mTvChoose.setText("添加图片或视频");
            mTvChoosePs.setVisibility(View.GONE);
            mGridViewPhoto.setVisibility(View.GONE);
            mLayoutChoose.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 选取相册返回
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_IMAGE://选取返回
            case REQUEST_IMAGE_VIEW://预览返回
                if (resultCode == RESULT_OK) {
                    mSelectPath.clear();
                    mSelectPath = data.getStringArrayListExtra(MultiSelector.EXTRA_RESULT);
                    if (null != mSelectPath) {
                        mPublishPhotoAdapter = new PublishPhotoAdapter(OrderDetailActivity.this, mSelectPath);
                        mGridViewPhoto.setAdapter(mPublishPhotoAdapter);

                        updatePhotoChoosedStatus();
                    } else {
                        Toast.makeText(OrderDetailActivity.this, "选取失败", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

        }
    }


    private boolean verifyPermission(String permissionRequired) {
        int permission = PermissionChecker.checkSelfPermission(this, permissionRequired);
        return permission == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 请求读取内存卡权限
     */
    private void requestPermission(final String permission, String rationale, final int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.permission_refuse)
                    .setMessage(rationale)
                    .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(OrderDetailActivity.this, new String[]{permission}, requestCode);
                        }
                    })
                    .setNegativeButton(R.string.cancel, null)
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        }
    }

    private void ShowData(final OrderListVo orderBean) {
        if (null != orderBean) {

            List<OrderListVo.images> imagesList = orderBean.getOrder_imgs();
            if (null != imagesList && imagesList.size() > 0) {
                layout_show_Images.setVisibility(View.VISIBLE);
                layout_show_Images_title.setVisibility(View.VISIBLE);
                layout_choose_title.setVisibility(View.GONE);
                mLayoutChoose.setVisibility(View.GONE);

                layout_show_Images.removeAllViews();
                for (OrderListVo.images images : imagesList) {
                    FrescoPhotoView photoView = new FrescoPhotoView(OrderDetailActivity.this);
                    photoView.setImageUri(images.getImg_url(), null);

                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(0, 20, 0, 0);
                    photoView.setLayoutParams(lp);
                    layout_show_Images.addView(photoView);
                }
            } else {
                layout_show_Images_title.setVisibility(View.GONE);
                layout_show_Images.setVisibility(View.GONE);
                layout_choose_title.setVisibility(View.VISIBLE);
                mLayoutChoose.setVisibility(View.VISIBLE);
            }


            et_input_money.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String money = et_input_money.getText().toString().trim();
                    String order_ratio = orderBean.getOrder_ratio();

                    if (StringUtils.isNotEmpty(money) && money.length() > 0) {
                        Double input_money = Double.valueOf(money);
                        Double ratio = Double.valueOf(order_ratio);

                        double result = input_money * ratio;

                        String res = String.valueOf(result);

                        tv_order_incoming.setText(res + "元");
                    } else {
                        tv_order_incoming.setText("0元");
                    }

                }
            });

            title_btn_right.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + orderBean.getTel_staff()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }
            });

            tv_call_phone.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (null != orderBean) {
                        String mobile = orderBean.getMobile();
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mobile));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }
            });

            order_status = orderBean.getOrder_status();
            order_type = orderBean.getOrder_type();

            btn_order_start_work.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    Builder dialog = new AlertDialog.Builder(OrderDetailActivity.this);
                    dialog.setTitle("提示");
                    dialog.setMessage("确认操作吗？");
                    dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if (order_type == 0 || order_type == 1) {// 钟点工
                                if (order_status == 3) {
                                    change_work(START);
                                } else if (order_status == 5) {
                                    int pay_type = orderBean.getPay_type();
                                    if (pay_type == 6 && !isSelect) {
                                        UIUtils.showToast(OrderDetailActivity.this, "请线下收款后再完成服务");
                                    } else {
//                                        change_work(OVER);
                                        startUploadImage();
                                    }
                                }
                            } else if (order_type == 2) {// 助理单
                                if (order_status == 2) {// 已派工
                                    // 调整订单
                                    change_order();
                                } else if (order_status == 4) {// 已支付
                                    change_work(START);
                                } else if (order_status == 5) {
                                    int pay_type = orderBean.getPay_type();
                                    if (pay_type == 6 && !isSelect) {
                                        UIUtils.showToast(OrderDetailActivity.this, "请线下收款后再完成服务");
                                    } else {
//                                        change_work(OVER);
                                        startUploadImage();
                                    }
                                }
                            }
                        }
                    });
                    dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create();
                    dialog.show();

                }
            });
            btn_order_add_hour.setOnClickListener(new OnClickListener() {

                private EditText et_add_hour_money;
                private Spinner sp_add_hour;

                @Override
                public void onClick(View v) {
                    View view = (LinearLayout) getLayoutInflater().inflate(R.layout.order_detail_addhour_dialog, null);
                    sp_add_hour = (Spinner) view.findViewById(R.id.sp_add_hour);
                    et_add_hour_money = (EditText) view.findViewById(R.id.et_add_hour_money);

                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(OrderDetailActivity.this, R.layout.spinner_list_item, arr);
                    sp_add_hour.setAdapter(arrayAdapter);

                    Builder dialog = new AlertDialog.Builder(OrderDetailActivity.this);
                    dialog.setTitle("加时提示");
                    dialog.setView(view);
                    dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String moneyString = et_add_hour_money.getText().toString().trim();
                            String hour = sp_add_hour.getSelectedItem().toString();

                            addHour(order_id, hour, moneyString);

                            KeyBoardUtils.closeKeybord(et_add_hour_money, OrderDetailActivity.this);

                        }
                    });
                    dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create();
                    dialog.show();

                }
            });

            tv_goto_address.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    String service_addrLat = orderBean.getService_addr_lat();
                    String service_addrLng = orderBean.getService_addr_lng();
                    String service_addr = orderBean.getService_addr();
                    startPoiNearbySearch(service_addrLat, service_addrLng, service_addr);

                }
            });

            if (order_type == 0 || order_type == 1) {
                if (order_status < 3 || order_status >= 7) {
                    // 不可点
                    btn_order_start_work.setEnabled(false);
                    // btn_order_start_work.setPressed(true);
                } else {
                    btn_order_start_work.setEnabled(true);
                    // btn_order_start_work.setPressed(false);
                }
            } else if (order_type == 2) {
                if (order_status == 0 || order_status == 1 || order_status == 3 || order_status == 7) {
                    // 不可点
                    btn_order_start_work.setEnabled(false);
                    // btn_order_start_work.setPressed(true);
                } else {
                    btn_order_start_work.setEnabled(true);
                    // btn_order_start_work.setPressed(false);
                }
            }

            if (order_status >= 3 && order_status < 7) {
                btn_order_add_hour.setVisibility(View.VISIBLE);
            } else {
                btn_order_add_hour.setVisibility(View.GONE);
            }


            if (order_status == 5 && orderBean.getPay_type() == 6) {
                slipBtn.setVisibility(View.VISIBLE);
            } else {
                slipBtn.setVisibility(View.GONE);
            }

            slipBtn.setOnToggleChanged(new OnToggleChanged() {
                @Override
                public void onToggle(boolean on) {
                    if (on) {
                        isSelect = true;
                        tv_order_status.setText("线下已支付");
                    } else {
                        isSelect = false;
                        tv_order_status.setText(orderBean.getPay_type_name());
                    }
                }
            });

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
            tv_order_addr.setText(orderBean.getService_addr());
            tv_user_type.setText(orderBean.getUser_type_str());
            tv_order_from.setText(orderBean.getOrder_from_name());

            String add_hour = orderBean.getOver_work_str();
            if (StringUtils.isEmpty(add_hour)) {
                layout_add_hour.setVisibility(View.GONE);
            } else {
                layout_add_hour.setVisibility(View.VISIBLE);
                tv_add_hour.setText(add_hour);
            }

            // 判断哪些展示
            if (order_type == 0 || order_type == 1) {// 钟点工
                ORDERTYPE = ORDERZDG;
                dissmisOrderModify();
                iv_order_type.setBackgroundResource(R.drawable.icon_zhongdiangong);
                layout_fuwu_shichang.setVisibility(View.VISIBLE);
                tv_order_shichang.setText(orderBean.getService_hour() + "小时");
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

            // 列表展示
            List<ServiceAddonsBean> orderBeans = orderBean.getService_addons();
            if (null != orderBeans && orderBeans.size() > 0) {
                layout_server_details.setVisibility(View.VISIBLE);
                orderListDetails = new OrderListDetailsAdapter(OrderDetailActivity.this);
                orderListDetails.setData(orderBeans);
                listview_details.setAdapter(orderListDetails);
            } else {
                layout_server_details.setVisibility(View.GONE);
            }

        }
    }

    /**
     * 加时接口
     */
    public void addHour(final String id, String hour, String money) {


        if (!NetworkUtils.isNetworkConnected(OrderDetailActivity.this)) {
            Toast.makeText(OrderDetailActivity.this, getString(R.string.net_not_open), Toast.LENGTH_SHORT).show();
            return;
        }

        String staffid = SpFileUtil.getString(OrderDetailActivity.this, SpFileUtil.FILE_UI_PARAMETER, SpFileUtil.KEY_STAFF_ID, "");
        Map<String, String> map = new HashMap<String, String>();
        map.put("staff_id", staffid);
        map.put("order_id", id + "");
        map.put("service_hour", hour);
        map.put("order_pay", money);
        AjaxParams param = new AjaxParams(map);

        showDialog();
        new FinalHttp().post(Constants.URL_ADD_HOUR, param, new AjaxCallBack<Object>() {

            private ArrayList<OrderListVo> secData;

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                dismissDialog();
                Toast.makeText(OrderDetailActivity.this, getString(R.string.network_failure), Toast.LENGTH_LONG).show();
                UIUtils.showTestToast(OrderDetailActivity.this, "errorMsg:" + strMsg);
            }

            @Override
            public void onSuccess(Object t) {
                super.onSuccess(t);
                String errorMsg = "";
                dismissDialog();
                LogOut.i("========", "onSuccess：" + t);
                // UIUtils.showTestToast(OrderDetailActivity.this, "order_from："+t.toString());
                try {
                    if (StringUtils.isNotEmpty(t.toString())) {
                        JSONObject obj = new JSONObject(t.toString());
                        int status = obj.getInt("status");
                        String msg = obj.getString("msg");
                        String data = obj.getString("data");
                        if (status == Constants.STATUS_SUCCESS) { // 正确
                            getOrderListDetail(order_id);
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
                    UIUtils.showTestToastLong(OrderDetailActivity.this, errorMsg);
                }
            }
        });

    }

    /**
     * 获取订单详情
     */
    public void getOrderListDetail(final String id) {
        if (!NetworkUtils.isNetworkConnected(OrderDetailActivity.this)) {
            Toast.makeText(OrderDetailActivity.this, getString(R.string.net_not_open), Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            InputMethodUtils.closeSoftKeyboard(OrderDetailActivity.this);
        } catch (Exception e1) {
            e1.printStackTrace();
        }


        String staffid = SpFileUtil.getString(OrderDetailActivity.this, SpFileUtil.FILE_UI_PARAMETER, SpFileUtil.KEY_STAFF_ID, "");
        Map<String, String> map = new HashMap<String, String>();
        map.put("staff_id", staffid);
        map.put("order_id", id + "");
        AjaxParams param = new AjaxParams(map);

        showDialog();
        new FinalHttp().get(Constants.URL_GET_ORDER_DETAIL, param, new AjaxCallBack<Object>() {

            private ArrayList<OrderListVo> secData;

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                dismissDialog();
                Toast.makeText(OrderDetailActivity.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
                UIUtils.showTestToast(OrderDetailActivity.this, "errorMsg:" + strMsg);
            }

            @Override
            public void onSuccess(Object t) {
                super.onSuccess(t);
                String errorMsg = "";
                dismissDialog();
                LogOut.i("========", "onSuccess：" + t);
                // UIUtils.showTestToast(OrderDetailActivity.this, "order_from："+t.toString());
                try {
                    if (StringUtils.isNotEmpty(t.toString())) {
                        JSONObject obj = new JSONObject(t.toString());
                        int status = obj.getInt("status");
                        String msg = obj.getString("msg");
                        String data = obj.getString("data");
                        if (status == Constants.STATUS_SUCCESS) { // 正确
                            if (StringUtils.isNotEmpty(data)) {
                                Gson gson = new Gson();
                                orderBean = gson.fromJson(data, OrderListVo.class);
                                ShowData(orderBean);

                                try {
                                    new LocationReportAgain(OrderDetailActivity.this).reportLocationHttp();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
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
                    UIUtils.showToast(OrderDetailActivity.this, errorMsg);
                }
            }
        });

    }

    /**
     * 显示调整订单
     */
    public void showOrderModify() {
        layout_order_tiaozheng.setVisibility(View.VISIBLE);
        layout_order_tiaozheng_line.setVisibility(View.VISIBLE);
        layout_goutong_des.setVisibility(View.VISIBLE);

    }

    // 隐藏调整订单
    public void dissmisOrderModify() {
        layout_order_tiaozheng.setVisibility(View.GONE);
        layout_order_tiaozheng_line.setVisibility(View.GONE);
        layout_goutong_des.setVisibility(View.GONE);
    }

    // 调整订单点亮
    public void SelectOrderModify() {
        tv_order_tiaozheng.setTextColor(getResources().getColor(R.color.simi_color_orange));
        tv_order_start.setTextColor(getResources().getColor(R.color.simi_color_gray));
        tv_order_over.setTextColor(getResources().getColor(R.color.simi_color_gray));

        iv_order_tiaozheng.setBackgroundResource(R.drawable.icon_order_tiaozheng_selected);
        iv_order_start.setBackgroundResource(R.drawable.icon_order_start);
        iv_order_over.setBackgroundResource(R.drawable.icon_order_over);

    }

    // 开始服务订单点亮
    public void SelectOrderStart() {
        tv_order_tiaozheng.setTextColor(getResources().getColor(R.color.simi_color_orange));
        tv_order_start.setTextColor(getResources().getColor(R.color.simi_color_orange));
        tv_order_over.setTextColor(getResources().getColor(R.color.simi_color_gray));

        iv_order_tiaozheng.setBackgroundResource(R.drawable.icon_order_tiaozheng_selected);
        iv_order_start.setBackgroundResource(R.drawable.icon_order_start_selected);
        iv_order_over.setBackgroundResource(R.drawable.icon_order_over);

    }

    // 已经完成订单点亮
    public void SelectOrderOver() {
        tv_order_tiaozheng.setTextColor(getResources().getColor(R.color.simi_color_orange));
        tv_order_start.setTextColor(getResources().getColor(R.color.simi_color_orange));
        tv_order_over.setTextColor(getResources().getColor(R.color.simi_color_orange));

        iv_order_tiaozheng.setBackgroundResource(R.drawable.icon_order_tiaozheng_selected);
        iv_order_start.setBackgroundResource(R.drawable.icon_order_start_selected);
        iv_order_over.setBackgroundResource(R.drawable.icon_order_over_selected);

    }

    /**
     * 开始服务或者结束服务
     */
    private void change_work(String type) {
        if (!NetworkUtils.isNetworkConnected(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.net_not_open), Toast.LENGTH_SHORT).show();
            return;
        }

        String staffid = SpFileUtil.getString(getApplicationContext(), SpFileUtil.FILE_UI_PARAMETER, SpFileUtil.KEY_STAFF_ID, "");
        Map<String, String> map = new HashMap<String, String>();
        map.put("staff_id", staffid);
        map.put("order_id", order_id);
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
                UIUtils.showTestToastLong(getApplicationContext(), "errorMsg:" + strMsg);
                dismissDialog();
                Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(Object t) {
                super.onSuccess(t);
                String errorMsg = "";
                dismissDialog();
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
                            errorMsg = getApplicationContext().getString(R.string.servers_error);
                        } else if (status == Constants.STATUS_PARAM_MISS) { // 缺失必选参数
                            errorMsg = getApplicationContext().getString(R.string.param_missing);
                        } else if (status == Constants.STATUS_PARAM_ILLEGA) { // 参数值非法
                            errorMsg = getApplicationContext().getString(R.string.param_illegal);
                        } else if (status == Constants.STATUS_OTHER_ERROR) { // 999其他错误
                            errorMsg = msg;
                        } else {
                            errorMsg = getApplicationContext().getString(R.string.servers_error);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    errorMsg = getApplicationContext().getString(R.string.servers_error);

                }
                // 操作失败，显示错误信息|
                if (!StringUtils.isEmpty(errorMsg.trim())) {
                    UIUtils.showToast(getApplicationContext(), errorMsg);
                }

                try {
                    new LocationReportAgain(OrderDetailActivity.this).reportLocationHttp();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * 调整订单
     */
    private void change_order() {
        if (!NetworkUtils.isNetworkConnected(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.net_not_open), Toast.LENGTH_SHORT).show();
            return;
        }
        // if (null != orderBean) {
        // orderid = orderBean.getOrder_id();
        // }

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

        String staffid = SpFileUtil.getString(getApplicationContext(), SpFileUtil.FILE_UI_PARAMETER, SpFileUtil.KEY_STAFF_ID, "");
        Map<String, String> map = new HashMap<String, String>();
        map.put("staff_id", staffid);
        map.put("order_id", order_id);
        map.put("service_content", content);
        map.put("order_money", money);
        AjaxParams param = new AjaxParams(map);

        showDialog();
        new FinalHttp().post(Constants.URL_GET_CHANGE_ORDER_WORK, param, new AjaxCallBack<Object>() {
            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                UIUtils.showTestToastLong(getApplicationContext(), "errorMsg:" + strMsg);
                dismissDialog();
                Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(Object t) {
                super.onSuccess(t);
                String errorMsg = "";
                dismissDialog();
                LogOut.i("========", "onSuccess：" + t);
                UIUtils.showTestToastLong(getApplicationContext(), "调整订单返回：" + t.toString());
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
                            errorMsg = getApplicationContext().getString(R.string.servers_error);
                        } else if (status == Constants.STATUS_PARAM_MISS) { // 缺失必选参数
                            errorMsg = getApplicationContext().getString(R.string.param_missing);
                        } else if (status == Constants.STATUS_PARAM_ILLEGA) { // 参数值非法
                            errorMsg = getApplicationContext().getString(R.string.param_illegal);
                        } else if (status == Constants.STATUS_OTHER_ERROR) { // 999其他错误
                            errorMsg = msg;
                        } else {
                            errorMsg = getApplicationContext().getString(R.string.servers_error);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    errorMsg = getApplicationContext().getString(R.string.servers_error);

                }
                // 操作失败，显示错误信息|
                if (!StringUtils.isEmpty(errorMsg.trim())) {
                    UIUtils.showToastLong(getApplicationContext(), errorMsg);
                }
            }
        });

    }

    /**
     * 启动百度地图Poi周边检索
     */
    public void startPoiNearbySearch(String latStr, String lonStr, String addname) {
        if (StringUtils.isEmpty(latStr) || StringUtils.isEmpty(lonStr) || StringUtils.isEmpty(addname)) {
            Toast.makeText(this, "地址信息错误", Toast.LENGTH_SHORT).show();
            return;
        }

        double lat = Double.parseDouble(latStr);
        double lon = Double.parseDouble(lonStr);

        LatLng ptCenter = new LatLng(lat, lon);
        PoiParaOption para = new PoiParaOption().key(addname).center(ptCenter).radius(2000);

        try {
            BaiduMapPoiSearch.openBaiduMapPoiNearbySearch(para, this);
        } catch (Exception e) {
            e.printStackTrace();
            showDialog();
        }

    }

    /**
     * 上传图片
     */
    private void startUploadImage() {
        if (null != mSelectPath && !mSelectPath.isEmpty()) {
            cachePostPhoto();
        } else {
            Toast.makeText(OrderDetailActivity.this, "请选择至少一张图片", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 缓存发布的图片文件
     */
    private void cachePostPhoto() {

        showDialog();

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (null != mSelectPath && !mSelectPath.isEmpty()) {
                    //folder
//                    files = new HashMap<>();
                    files = new LinkedHashMap<>();
                    tempFiles = new ArrayList<>();
                    String tempPath = Environment.getExternalStorageDirectory().getPath() + Constants.FOLDER_TEMP;
                    File dirFile = new File(tempPath);
                    if (!dirFile.exists()) {
                        dirFile.mkdirs();
                    }
                    for (int i = 0; i < mSelectPath.size(); i++) {
                    /* 获取文件的后缀名 */
                        int dotIndex = mSelectPath.get(i).lastIndexOf(".");
                        String end = mSelectPath.get(i).substring(dotIndex, mSelectPath.get(i).length()).toLowerCase();
                        if (TextUtils.isEmpty(end)) {
                            end = ".jpg";
                        }
                        long time = System.currentTimeMillis();
                        //创建临时目录
                        String toPath = tempPath + File.separator + time + end;
                        boolean success = BitmapUtil.saveJpgFile(mSelectPath.get(i), toPath);
                        if (!success) {
                            mHandler.sendEmptyMessage(1);
                        } else {
                            ThumbnailImage thumbnailImage = new ThumbnailImage();
                            thumbnailImage.setThumbnailBaseUri(toPath);
                            int[] widthAndHeight = BitmapUtil.getImageWidthHeight(toPath);
                            thumbnailImage.setWidth(widthAndHeight[0]);
                            thumbnailImage.setHeight(widthAndHeight[1]);
                            tempFiles.add(thumbnailImage);
                            files.put("image-file-" + i + ".jpg", new File(tempPath + File.separator + time + end));
                        }
                    }
                }
                uploadImage(files);
            }
        }).start();
    }


    /**
     * 上传图片，完成服务
     */
    private void uploadImage(final HashMap<String, File> files) {
        if (!CommonUtil.isFastClick()) {
            String staffid = SpFileUtil.getString(getApplicationContext(), SpFileUtil.FILE_UI_PARAMETER, SpFileUtil.KEY_STAFF_ID, "");

            HashMap<String, String> map = new HashMap<>();
            map.put("staff_id", staffid);
            map.put("order_id", order_id);

            AgentApi.upload(Constants.URL_GET_OVER_ORDER_WORK,
                    map, "imgs", files, "image/jpg", new Callback.CommonCallback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            dismissDialog();
                            String errorMsg = "";
                            LogOut.i("========", "onSuccess：" + result);
                            UIUtils.showToast(OrderDetailActivity.this, "上传图片成功,服务完成");

                            // "开始服务返回："+t.toString());
                            try {
                                if (StringUtils.isNotEmpty(result)) {
                                    JSONObject obj = new JSONObject(result);
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
                                        errorMsg = getApplicationContext().getString(R.string.servers_error);
                                    } else if (status == Constants.STATUS_PARAM_MISS) { // 缺失必选参数
                                        errorMsg = getApplicationContext().getString(R.string.param_missing);
                                    } else if (status == Constants.STATUS_PARAM_ILLEGA) { // 参数值非法
                                        errorMsg = getApplicationContext().getString(R.string.param_illegal);
                                    } else if (status == Constants.STATUS_OTHER_ERROR) { // 999其他错误
                                        errorMsg = msg;
                                    } else {
                                        errorMsg = getApplicationContext().getString(R.string.servers_error);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                errorMsg = getApplicationContext().getString(R.string.servers_error);

                            }
                            // 操作失败，显示错误信息|
                            if (!StringUtils.isEmpty(errorMsg.trim())) {
                                UIUtils.showToast(getApplicationContext(), errorMsg);
                            }

                        }

                        @Override
                        public void onError(Throwable ex, boolean isOnCallback) {
                            dismissDialog();
                            UIUtils.showToast(OrderDetailActivity.this, "上传图片失败");
                        }

                        @Override
                        public void onCancelled(CancelledException cex) {
                        }

                        @Override
                        public void onFinished() {
                            try {
                                new LocationReportAgain(OrderDetailActivity.this).reportLocationHttp();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }

}
