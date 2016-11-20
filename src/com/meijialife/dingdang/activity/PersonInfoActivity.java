package com.meijialife.dingdang.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.widget.TextView;

import com.meijialife.dingdang.BaseActivity;
import com.meijialife.dingdang.R;
import com.meijialife.dingdang.bean.UserIndexData;

/**
 * 个人信息
 * 
 * @author windows7
 * 
 */

public class PersonInfoActivity extends BaseActivity {

    private UserIndexData userIndexData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.layout_person_info);
        super.onCreate(savedInstanceState);

        initView();

    }

    private void initView() {
        setTitleName("个人信息");
        requestBackBtn();
        userIndexData = (UserIndexData) getIntent().getSerializableExtra("userIndexData");

        TextView tv_mobile = (TextView) findViewById(R.id.tv_mobile);
        TextView tv_sex = (TextView) findViewById(R.id.tv_sex);
        TextView tv_name = (TextView) findViewById(R.id.tv_name);
        TextView tv_jineng = (TextView) findViewById(R.id.tv_jineng);
        TextView tv_staff_code = (TextView) findViewById(R.id.tv_staff_code);

        if (null != userIndexData) {
            String skillString = "";
            ArrayList<String> skills = userIndexData.getSkills();
            for (String str : skills) {
                skillString += str + "\n" + "\n";
            }
            tv_mobile.setText(userIndexData.getMobile() == null ? " " : userIndexData.getMobile());
            tv_name.setText(userIndexData.getName() == null ? " " : userIndexData.getName());
//            tv_jineng.setText(skillString == null ? " " : skillString);
            int sex = userIndexData.getSex();
            if (sex == 0) {
                tv_sex.setText("男");
            } else if (sex == 1) {
                tv_sex.setText("女");
            }

            tv_staff_code.setText(userIndexData.getStaff_code() == null ? " " : userIndexData.getStaff_code());
        }
    }
}
