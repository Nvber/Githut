package com.feicui.zh.splash;

import android.animation.ArgbEvaluator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.feicui.zh.R;
import com.feicui.zh.splash.pager.Pager2;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;

/**
 * Created by Administrator on 2016/7/26.
 */
public class SplashPagerFragment extends Fragment {

    @BindView(R.id.viewPager) ViewPager viewPager;
    /**指示器圆点*/
    @BindView(R.id.indicator) CircleIndicator circleIndicator;
    /**颜色*/
    @BindView(R.id.content) FrameLayout frameLayout;//当前页面Layout(主要为了更新其背景颜色)
    @BindColor(R.color.colorGreen) int colorGreen;
    @BindColor(R.color.colorRed) int colorRed;
    @BindColor(R.color.colorYellow) int coloryellow;
    /**手机*/
    @BindView(R.id.layoutPhone) FrameLayout phonelayout;// 屏幕中央的"手机"
    @BindView(R.id.ivPhoneFont) ImageView phonefont;//手机中的字体
    SplashPagerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash_pager,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);

        adapter = new SplashPagerAdapter(getContext());
        viewPager.setAdapter(adapter);
        circleIndicator.setViewPager(viewPager);
        /**添加ViewPager监听(为了动画)*/
        viewPager.addOnPageChangeListener(PagerColorListener);
        viewPager.addOnPageChangeListener(PhoneViewListener);

    }
    /**背景颜色渐变处理*/
    private ViewPager.OnPageChangeListener PagerColorListener = new ViewPager.OnPageChangeListener() {
       final ArgbEvaluator argbEvaluator = new ArgbEvaluator();
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            /**第一页面与第二页面之间*/
            if (position==0){
                int color = (int) argbEvaluator.evaluate(positionOffset, colorGreen, colorRed);
                frameLayout.setBackgroundColor(color);
            }
            /**第二页面与第三页面之间*/
            if (position==1){
                int color = (int) argbEvaluator.evaluate(positionOffset,colorRed,coloryellow);
                frameLayout.setBackgroundColor(color);
            }

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
    /**手机动画处理(平移、缩放、透明度)*/
    private ViewPager.OnPageChangeListener PhoneViewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            /**第一页面与第二页面之间*/
            if (position==0){
                /**手机缩放*/
                float scale = 0.3f+0.7f*positionOffset;
                phonelayout.setScaleX(scale);
                phonelayout.setScaleY(scale);
                /**手机平移*/
                int scroll = (int) ((positionOffset-1)*200);
                phonelayout.setTranslationX(scroll);
                /**手机字体渐变*/
                phonefont.setAlpha(positionOffset);
                return;
            }
            /**第二页面与第三页面之间*/
            if (position==1){
                phonelayout.setTranslationX(-positionOffsetPixels);
            }
        }
        /**当显示出最后一个pager时，播放条目动画*/
        @Override
        public void onPageSelected(int position) {
            if (position==2){
                Pager2 pager2 = (Pager2) adapter.getViews(position);
                pager2.showAnimation();
            }
        }
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
