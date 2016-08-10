package com.feicui.zh.hotfragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.feicui.zh.hotfragment.view.HotListFragment;

import java.util.List;

/**
 * Created by Administrator on 2016/7/27.
 * 适配器，用FragmentPagerAdapter比用PagerAdapter更方便
 */
public class HotAdapter extends FragmentPagerAdapter {
    private List<Language> languages;
    public HotAdapter(FragmentManager fm, Context context) {
        super(fm);
        languages = Language.getDefaultLanguages(context);
    }
/**碎片*/
    @Override
    public Fragment getItem(int position) {
        return HotListFragment.getInstance(languages.get(position));
    }
/**碎片个数*/
    @Override
    public int getCount() {
        return languages.size();
    }
/**tablayout标题*/
    @Override
    public CharSequence getPageTitle(int position) {
        return languages.get(position).getName();
    }
}
