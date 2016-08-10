package com.feicui.zh.hotfragment.view;


import com.feicui.zh.hotfragment.model.Repo;

import java.util.List;

/**
 * Created by Administrator on 2016/7/28.
 */
public interface  HotListFragmentView {
    //内容
     void showContentView();
    //错误
     void showErrorView(String errorMsg);
    //空白
     void showEmptyView();
    //信息
     void showMessage(String msg);
    //停止刷新
     void stopRefresh();
    //刷新加数据
     void refreshData(List<Repo> data);

}
