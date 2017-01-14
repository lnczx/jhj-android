package com.meijialife.dingdang.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.meijialife.dingdang.BaseActivity;
import com.meijialife.dingdang.R;
import com.meijialife.dingdang.utils.StringUtils;

/**
 * 网页专用Activity
 * 
 */
public class WebViewActivity extends BaseActivity implements OnClickListener {

    private WebView webview;
    private ImageView btn_back;
    private TextView tv_title;

    private String url;
    private String titleStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.webview_activity);
        super.onCreate(savedInstanceState);

        init();
        requestBackBtn();
    }

    @SuppressLint({ "JavascriptInterface", "NewApi" })
    private void init() {
        url = getIntent().getStringExtra("url");
        titleStr = getIntent().getStringExtra("title");

        btn_back = (ImageView) findViewById(R.id.title_btn_left);
        btn_back.setOnClickListener(this);
        tv_title = (TextView) findViewById(R.id.header_tv_name);

        if (StringUtils.isEmpty(url)) {
            Toast.makeText(getApplicationContext(), "数据错误", 0).show();
            return;
        }
        if (!StringUtils.isEmpty(titleStr)) {
            tv_title.setText(titleStr);
        } 
        //获取页面中的title
//        WebChromeClient wvcc = new WebChromeClient() {
//            @Override
//            public void onReceivedTitle(WebView view, String title) {
//                super.onReceivedTitle(view, title);
//               String title1  =title;
//                tv_title.setText(title);
//            }
//        };
        webview = (WebView) findViewById(R.id.webview);
//        webview.setWebChromeClient(wvcc);//负责显示页面title
        webview.loadUrl(url);
        WebSettings webSettings = webview.getSettings();
        webview.addJavascriptInterface(this, "Koolearn");
        webview.setBackgroundColor(Color.parseColor("#00000000"));
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);// 设置js可以直接打开窗口，如window.open()，默认为false
        webview.getSettings().setJavaScriptEnabled(true);// 是否允许执行js，默认为false。设置true时，会提醒可能造成XSS漏洞
        webview.getSettings().setSupportZoom(true);// 是否可以缩放，默认true
        webview.getSettings().setBuiltInZoomControls(true);// 是否显示缩放按钮，默认false
        webview.getSettings().setUseWideViewPort(true);// 设置此属性，可任意比例缩放。大视图模式
        webview.getSettings().setLoadWithOverviewMode(true);// 和setUseWideViewPort(true)一起解决网页自适应问题
        webview.getSettings().setAppCacheEnabled(true);// 是否使用缓存
        webview.getSettings().setDomStorageEnabled(true);// DOM Storage
        webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (webview != null && (keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
            webview.goBack();
            return true;
        } else {
            finish();
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.title_btn_left: // 返回
            finish();
            break;

        default:
            break;
        }
    }

}
