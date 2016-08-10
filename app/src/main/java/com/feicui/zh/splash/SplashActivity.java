package com.feicui.zh.splash;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.feicui.zh.R;
import com.feicui.zh.MainActivity;
import com.feicui.zh.common.ActivityUtils;
import com.feicui.zh.login.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.btnEnter)
    Button btnEnter;
    private ActivityUtils activityUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        activityUtils = new ActivityUtils(this);
        ButterKnife.bind(this);
    }
/**直接登陆*/
    @OnClick(R.id.btnEnter)
    public void enter() {
        activityUtils.startActivity(MainActivity.class);
        finish();
    }
/**登陆*/
    @OnClick(R.id.btnLogin)
    public void login() {
        activityUtils.startActivity(LoginActivity.class);
    }
}
