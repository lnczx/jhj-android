package com.meijialife.dingdang.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
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

import com.meijialife.dingdang.R;
import com.meijialife.dingdang.utils.StringUtils;


/**
 * @description：工具箱--更多--应用中心专属webView
 * @author： kerryg
 * @date:2015年12月3日 
 */
public class WebViewsActivity  extends Activity{

    private String url;

    private WebView webview;
    private ImageView iv_person_left;
    private TextView tv_person_title;
    private ImageView iv_person_close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.webview_personal_fragment);
        super.onCreate(savedInstanceState);

        init();
    }

    @SuppressLint({ "JavascriptInterface", "NewApi" })
    private void init() {
        url = getIntent().getStringExtra("url");

        iv_person_left = (ImageView) findViewById(R.id.iv_person_left);
        iv_person_close = (ImageView)findViewById(R.id.iv_person_close);
        tv_person_title = (TextView)findViewById(R.id.tv_person_title);
        webview = (WebView)findViewById(R.id.webview);

        if (StringUtils.isEmpty(url)) {
            Toast.makeText(getApplicationContext(), "数据错误", 0).show();
            return;
        }

        WebChromeClient wvcc = new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                tv_person_title.setText(title);
            }

        };
        // 设置WebChromeClinent对象

        webview.setWebChromeClient(wvcc);
       /* webview.requestFocusFromTouch();
        webview.requestFocus(View.FOCUS_DOWN);*/
        webview.loadUrl(url);

        WebSettings webSettings = webview.getSettings();
        webview.addJavascriptInterface(this, "Koolearn");
        webview.setBackgroundColor(Color.parseColor("#00000000"));
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);// 设置js可以直接打开窗口，如window.open()，默认为false
        webview.getSettings().setJavaScriptEnabled(true);// 是否允许执行js，默认为false。设置true时，会提醒可能造成XSS漏洞
        webview.getSettings().setSupportZoom(true);// 是否可以缩放，默认true
        // popwindow显示webview不能设置缩放按钮，否则触屏就会报错。
        // webview.getSettings().setBuiltInZoomControls(true);// 是否显示缩放按钮，默认false
        webview.getSettings().setUseWideViewPort(true);// 设置此属性，可任意比例缩放。大视图模式
        webview.getSettings().setLoadWithOverviewMode(true);// 和setUseWideViewPort(true)一起解决网页自适应问题
        webview.getSettings().setAppCacheEnabled(false);// 是否使用缓存
        webview.getSettings().setDomStorageEnabled(true);// DOM Storage
        webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        iv_person_close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv_person_left.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webview != null && webview.canGoBack()) {
                    webview.goBack();
                } else {
                    finish();
                }
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

}
