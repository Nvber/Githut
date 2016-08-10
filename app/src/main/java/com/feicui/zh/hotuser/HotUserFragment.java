package com.feicui.zh.hotuser;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.feicui.zh.R;
import com.feicui.zh.common.ActivityUtils;
import com.feicui.zh.hotfragment.HotFootView;
import com.feicui.zh.login.model.User;
import com.mugen.Mugen;
import com.mugen.MugenCallbacks;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.header.StoreHouseHeader;

/**
 * Created by Administrator on 2016/8/5.
 */
public class HotUserFragment extends Fragment implements HotUserPresenter.HotUsersView {

    @BindView(R.id.lvRepos)
    ListView listView;
    @BindView(R.id.ptrClassicFrameLayout)
    PtrClassicFrameLayout ptrFrameLayout;
    @BindView(R.id.emptyView)
    TextView emptyView;
    @BindView(R.id.errorView)
    TextView errorView;
    private ActivityUtils activityUtils;
    private HotFootView footerView; // 上拉加载更多的视图
    private HotUserPresenter presenter;
    private HotUserAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hot_user, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activityUtils = new ActivityUtils(this);
        presenter = new HotUserPresenter(this);
        //
        adapter = new HotUserAdapter();
        listView.setAdapter(adapter);
        // 初始下拉刷新
        initPullfresh();
        // 初始上拉加载更多
        initLoadMore();
        /**如果当前页面没有数据，开始自动刷新*/
        if (adapter.getCount() == 0) {
            ptrFrameLayout.postDelayed(new Runnable() {
                @Override public void run() {
                    ptrFrameLayout.autoRefresh();
                }
            }, 200);
        }
    }
    private void initPullfresh() {
        /**使用当前对象做为key，来记录上一次的刷新时间,如果两次下拉太近，将不会触发新刷新*/
        ptrFrameLayout.setLastUpdateTimeRelateObject(this);
        /**关闭header所用时间*/
        ptrFrameLayout.setDurationToCloseHeader(1500);
        /**下拉刷新监听*/
        ptrFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override public void onRefreshBegin(PtrFrameLayout frame) {
                /**数据刷新加载*/
                presenter.refresh();
            }
        });
        /**修改header样式*/
        StoreHouseHeader header = new StoreHouseHeader(getContext());
        header.initWithString("I LIKE " + " JAVA");
        header.setPadding(0, 60, 0, 60);
        /**修改Ptr的HeaderView效果*/
        ptrFrameLayout.setHeaderView(header);
        ptrFrameLayout.addPtrUIHandler(header);
        ptrFrameLayout.setBackgroundResource(R.color.colorRefresh);
    }

    private void initLoadMore() {
        footerView = new HotFootView(getContext());
        Mugen.with(listView, new MugenCallbacks() {
            /**listview滚动到底部,将触发此方法*/
            @Override public void onLoadMore() {
                /**上拉加载数据的业务处理*/
                presenter.loadMore();
            }

            /**是否正在加载中*/
            @Override public boolean isLoading() {
                return listView.getFooterViewsCount() > 0 && footerView.isLoading();
            }

            /**是否已加载完成所有数据*/
            @Override public boolean hasLoadedAllItems() {
                return listView.getFooterViewsCount() > 0 && footerView.isComplete();
            }
        }).start();
    }

    /**下拉刷新-------------------------------------*/
    @Override
    public void showRefreshView() {
        ptrFrameLayout.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
    }

    @Override
    public void showErrorView(String errorMsg) {
        ptrFrameLayout.setVisibility(View.GONE);
        emptyView.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);
    }

    @Override public void hideRefreshView() {
        ptrFrameLayout.refreshComplete();
    }

    @Override
    public void showMessage(String msg) {
        activityUtils.showToast(msg);
    }

    @Override
    public void refreshData(List<User> datas) {
        adapter.clear();
        adapter.addAll(datas);
    }

    /**上拉加载更多-------------------------------------*/
    @Override public void showLoadMoreLoading() {
        if (listView.getFooterViewsCount() == 0) {
            listView.addFooterView(footerView);
        }
        footerView.showLoading();
    }

    @Override public void hideLoadMoreLoading() {
//        listView.removeFooterView(footerView);
    }

    @Override public void showLoadMoreErro(String erroMsg) {
        if (listView.getFooterViewsCount() == 0) {
            listView.addFooterView(footerView);
        }
        footerView.showError(erroMsg);
    }

    @Override public void addMoreData(List<User> datas) {
        adapter.addAll(datas);
    }
}
