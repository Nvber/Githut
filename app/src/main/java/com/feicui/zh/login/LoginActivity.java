package com.feicui.zh.login;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.feicui.zh.R;
import com.feicui.zh.MainActivity;
import com.feicui.zh.common.ActivityUtils;
import com.feicui.zh.network.GitHubApi;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by Administrator on 2016/7/29.
 */
public class LoginActivity extends AppCompatActivity implements LoginView {

    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.gifImageView)
    GifImageView gifImageView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private LoginPresent present;
    private ActivityUtils activityUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        activityUtils = new ActivityUtils(this);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        present = new LoginPresent(this);
        initWeb();
    }

    private void initWeb() {
        /**删除所有的Cookie,主要为了清除以前的登陆记录*/
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        //授权登陆URL
        webView.loadUrl(GitHubApi.AUTH_RUL);
        webView.setFocusable(true);
        webView.setFocusableInTouchMode(true);
        //监听进度
        webView.setWebChromeClient(webChromeClient);
        //监听webview
        webView.setWebViewClient(webViewClient);

    }

    private WebChromeClient webChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress >= 100) {
                gifImageView.setVisibility(View.GONE);
            }
        }
    };

    private WebViewClient webViewClient = new WebViewClient() {
        // 每当webview"刷新"时,此方法将触发 (密码输错了时！输对了时！等等情况web页面都会刷新变化的)
        @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //检测是不是CALL_BACK
            Uri uri = Uri.parse(url);
            if (GitHubApi.CALL_BACK.equals(uri.getScheme())) {
                //获取code
                String code = uri.getQueryParameter("code");
                //用code做登陆业务工作
                present.login(code);
                return true;
            }
            return super.shouldOverrideUrlLoading(view, url);
        }
    };

    @Override public void showProgress() {
        gifImageView.setVisibility(View.VISIBLE);
    }

    @Override public void showMessage(String msg) {
        activityUtils.showToast(msg);
    }

    @Override public void resetWeb() {
        initWeb();
    }

    @Override public void navigateToMain() {
        activityUtils.startActivity(MainActivity.class);
        finish();
    }

}
