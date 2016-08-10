package com.feicui.zh.login;

/**
 * Created by Administrator on 2016/7/29.
 */
public interface LoginView {
    // 显示进度
    void showProgress();
    void showMessage(String msg);
    // 重置WebView
    void resetWeb();
    // 切换到Main
    void navigateToMain();
}
