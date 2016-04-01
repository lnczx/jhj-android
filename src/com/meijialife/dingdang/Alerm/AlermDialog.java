package com.meijialife.dingdang.Alerm;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.meijialife.dingdang.MainActivity;
import com.meijialife.dingdang.R;
import com.meijialife.dingdang.bean.ReceiverBean;
import com.meijialife.dingdang.utils.StringUtils;

public class AlermDialog extends Dialog {

    private Context context;
    private String title;
    private String msg;
    private String mAction;
    private ReceiverBean bean;

    /**
     * 
     * @param context
     * @param theme
     *            dialog主题
     * @param title
     *            标题
     * @param msg
     *            消息
     * @param action
     */
    public AlermDialog(Context context, ReceiverBean rBean, String action) {
        super(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        setContentView(R.layout.layout_alerm_dialog);
        getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);// 设置系统权限，否则在服务中弹不出来

        this.context = context;
        this.bean = rBean;
        this.mAction = action;

        initView();
    }

    private void initView() {
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_server_addr = (TextView) findViewById(R.id.tv_server_addr);
        tv_server_time = (TextView) findViewById(R.id.tv_server_time);
        tv_server_hour = (TextView) findViewById(R.id.tv_server_hour);
        tv_server_content = (TextView) findViewById(R.id.tv_server_content);
        Button btn_ok = (Button) findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(okListener);

        tv_title.setText(title);
        String service_addr = bean.getService_addr();
        String service_time = bean.getService_time();
        String service_hour = bean.getService_hour();
        String service_content = bean.getService_content();
        String order_money = bean.getOrder_money();
        String order_type = bean.getOrder_type();

        tv_server_addr.setText(service_addr);
        tv_server_time.setText(service_time);
        tv_server_content.setText(service_content);
        if (StringUtils.isEquals(order_type, "2")) {
            tv_title.setText("订单金额："+order_money+"元\n(助理)");
            tv_server_hour.setVisibility(View.GONE);
        } else {
            tv_title.setText("订单金额："+order_money+"元\n(钟点工)");
            tv_server_hour.setVisibility(View.VISIBLE);
            tv_server_hour.setText(service_hour);
        }
    }

    /**
     * 确认按钮监听事件
     */
    View.OnClickListener okListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("mAction", mAction);
            context.startActivity(intent);
            dismiss();
        }
    };
    private TextView tv_server_addr;
    private TextView tv_server_time;
    private TextView tv_server_hour;
    private TextView tv_server_content;

}
