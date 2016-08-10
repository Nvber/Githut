package com.feicui.zh;

import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.feicui.zh.common.ActivityUtils;
import com.feicui.zh.favorite.FavoriteFragment;
import com.feicui.zh.ganh.GanHFragment;
import com.feicui.zh.hotfragment.HotFragment;
import com.feicui.zh.hotuser.HotUserFragment;
import com.feicui.zh.login.LoginActivity;
import com.feicui.zh.login.UserRepo;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.drawerLayout) DrawerLayout drawerLayout;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.navigationView) NavigationView navigationView;

    HotFragment hotFragment;
    private HotUserFragment hotUserFragment;
    private FavoriteFragment favoriteFragment;
    private GanHFragment ganHFragment;

    private Button btnLogin;
    private ImageView ivIcon;
    private ActivityUtils activityUtils;
    ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activityUtils = new ActivityUtils(this);
        ButterKnife.bind(this);
        /**抽屉内容监听*/
        navigationView.setNavigationItemSelectedListener(this);

        setSupportActionBar(toolbar);
        /**给左图标加上返回*/
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        /**同步*/
        drawerToggle.syncState();
        /**图标动画改变*/
        drawerLayout.addDrawerListener(drawerToggle);
        btnLogin = ButterKnife.findById(navigationView.getHeaderView(0), R.id.btnLogin);
        ivIcon = ButterKnife.findById(navigationView.getHeaderView(0), R.id.ivIcon);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                activityUtils.startActivity(LoginActivity.class);
                finish();
            }
        });

        hotFragment = new HotFragment();
        replaceFragment(hotFragment);
    }

    @Override
    protected void onStart() {
        super.onStart();
        /**没有授权的登陆*/
        if (UserRepo.isEmpty()) {
            btnLogin.setText(R.string.login_github);
            return;
        }
        btnLogin.setText(R.string.switch_account);
        /**设置Title*/
        getSupportActionBar().setTitle(UserRepo.getUser().getName());
        /**设置用户头像*/
        String photoUrl = UserRepo.getUser().getAvatar();
        ImageLoader.getInstance().displayImage(photoUrl, ivIcon);
    }

    /**碎片替换*/
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container,fragment);
        transaction.commit();
    }
    /**抽屉侧菜单*/
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // 将默认选中项“手动”设置为false
        if (item.isChecked()) {
            item.setChecked(false);
        }
        /**根据选择做切换*/
        switch (item.getItemId()){
            case R.id.github_hot_repo:
                if (!hotFragment.isAdded()) {
                    replaceFragment(hotFragment);
                }
                break;
            /**热门开发者*/
            case R.id.github_hot_coder:
                if (hotUserFragment == null) hotUserFragment = new HotUserFragment();
                if (!hotUserFragment.isAdded()) {
                    replaceFragment(hotUserFragment);
                }
                break;
            /**我的收藏*/
            case R.id.arsenal_my_repo:
                if(favoriteFragment == null)favoriteFragment = new FavoriteFragment();
                if(!favoriteFragment.isAdded()){
                    replaceFragment(favoriteFragment);
                }
                break;
            /**每日干货*/
            case R.id.tips_daily:
                if(ganHFragment == null)ganHFragment = new GanHFragment();
                if(!ganHFragment.isAdded()){
                    replaceFragment(ganHFragment);
                }
                break;
        }
        /**关闭drawerLayout*/
        drawerLayout.post(new Runnable() {
            @Override public void run() {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
        /**返回true，代表将该菜单项变为checked状态*/
        return true;
    }
}
