package com.feicui.zh.hotfragment.view;

import com.feicui.zh.hotfragment.model.Repo;

import java.util.List;

/**
 * Created by Administrator on 2016/7/28.
 */
public interface HotLoadMoreView {
    void showLoadMoreLoading();
    void hideLoadMoreLoading();
    void showLoadMoreErro(String erroMsg);
    void addMoreData(List<Repo> datas);
}
