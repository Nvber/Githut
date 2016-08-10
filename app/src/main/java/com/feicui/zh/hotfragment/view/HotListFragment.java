package com.feicui.zh.hotfragment.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.feicui.zh.R;
import com.feicui.zh.common.ActivityUtils;
import com.feicui.zh.favorite.dao.DBHelp;
import com.feicui.zh.favorite.dao.LocalRepoDao;
import com.feicui.zh.favorite.model.LocalRepo;
import com.feicui.zh.favorite.model.RepoConverter;
import com.feicui.zh.hotfragment.HotFootView;
import com.feicui.zh.hotfragment.Language;
import com.feicui.zh.hotfragment.model.Repo;
import com.feicui.zh.repoinfo.RepoInfoActivity;
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
 * Created by Administrator on 2016/7/27.
 */
public class HotListFragment extends Fragment implements HotListView {


    private static final String KEY_LANGUAGE = "key_language";

    public static HotListFragment getInstance(Language language) {
        HotListFragment fragment = new HotListFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_LANGUAGE, language);
        fragment.setArguments(args);
        return fragment;
    }

    private Language getLanguage() {
        return (Language) getArguments().getSerializable(KEY_LANGUAGE);
    }
    @BindView(R.id.lvRepos) ListView lvRepos;
    @BindView(R.id.ptrClassicFrameLayout) PtrClassicFrameLayout ptrFrameLayout;
    @BindView(R.id.emptyView) TextView emptyView;
    @BindView(R.id.errorView) TextView errorView;

    private HotListAdapter adapter;
     /** P 用来做当前页面业务逻辑及视图更新的*/
    private HotListPresenter presenter;
    /**上拉加载更多的视图*/
    private HotFootView hotFootView;
    private ActivityUtils activityUtils;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_repo_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        activityUtils = new ActivityUtils(this);
        presenter = new HotListPresenter(this,getLanguage());
        adapter = new HotListAdapter();
        lvRepos.setAdapter(adapter);

        /**按下某个仓库后，进入此仓库详情*/
        lvRepos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Repo repo = adapter.getItem(position);
                RepoInfoActivity.open(getContext(), repo);
            }
        });
        /**长按某个仓库后，加入收藏*/
        lvRepos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // 热门仓库列表上的Repo
                Repo repo = adapter.getItem(position);
                LocalRepo localRepo = RepoConverter.convert(repo);
                // 添加到本地仓库表中去(只认本地仓库实体LocalRepo)
                new LocalRepoDao(DBHelp.getInstance(getContext())).createOrUpdate(localRepo);
                activityUtils.showToast("收藏成功");
                return true;
            }
        });

        initPullfresh();
        initLoadMore();

        if (adapter.getCount() == 0) {
            ptrFrameLayout.postDelayed(new Runnable() {
                @Override public void run() {
                    ptrFrameLayout.autoRefresh();
                }
            }, 200);
        }
    }
/**上拉加载更多的方法*/
    private void initLoadMore() {
        hotFootView = new HotFootView(getContext());
        /**下拉第三方包 必记*/
        Mugen.with(lvRepos, new MugenCallbacks() {
           /**listview，滚动到底部,将触发此方法*/
            @Override
            public void onLoadMore() {
                // 执行上拉加载数据的业务处理
                presenter.loadMore();
            }
            /**是否正在加载中,其内部将用此方法来判断是否触发onLoadMore*/
            @Override
            public boolean isLoading() {
                return lvRepos.getFooterViewsCount()>0 && hotFootView.isLoading();
            }

            @Override
            public boolean hasLoadedAllItems() {
                return lvRepos.getFooterViewsCount()>0 && hotFootView.isComplete();
            }
        }).start();

    }

    /**下拉刷新的方法*/
    private void initPullfresh() {
        // 使用当前对象做为key，来记录上一次的刷新时间,如果两次下拉太近，将不会触发新刷新
        ptrFrameLayout.setLastUpdateTimeRelateObject(this);
        // 关闭header所用时长
        ptrFrameLayout.setDurationToCloseHeader(1000);
       /** 下拉刷新监听处理*/
        ptrFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                // 去做数据的加载，做具体的业务
                // 也就是说，你要抛开视图，到后台线程去做你的业务处理(数据刷新加载)
                presenter.refresh();
            }
        });

        // 以下代码（只是修改了header样式）
        StoreHouseHeader header= new StoreHouseHeader(getContext());
        header.initWithString("I LIKE " + " Android");
        header.setPadding(0,60,0,60);
        /**修改Ptr的HeaderView效果*/
        ptrFrameLayout.setHeaderView(header);
        ptrFrameLayout.addPtrUIHandler(header);
        ptrFrameLayout.setBackgroundResource(R.color.colorRefresh);
    }

    // 显示内容 or 错误 or 空白 , 三选一
    @Override
    public void showContentView(){
        ptrFrameLayout.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);
        emptyView.setVisibility(View.GONE);
    }

    @Override
    public void showErrorView(String errorMsg){
        ptrFrameLayout.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
    }
    @Override
    public void showEmptyView(){
        ptrFrameLayout.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
    }
    // 显示提示信息，如：Toast， 直接在当前页面上页面
    @Override
    public void showMessage(String msg){
        activityUtils.showToast(msg);
    }
    /**停止刷新*/
    @Override
    public void stopRefresh(){
        ptrFrameLayout.refreshComplete();
    }
    /**刷新数据，将后台线程更新加载到的数据，刷新显示到视图(listview)上来显示给用户看*/
    @Override
    public void refreshData(List<Repo> data) {
        adapter.clear();
        adapter.addAll(data);
    }

    // 上拉加载更多视图实现----------------------
    /**显示更多*/
    @Override
    public void showLoadMoreLoading() {
        if (lvRepos.getFooterViewsCount()==0){
            lvRepos.addFooterView(hotFootView);
        }
        hotFootView.showLoading();
    }
    /**隐藏数据*/
    @Override
    public void hideLoadMoreLoading() {
//        lvRepos.removeFooterView(hotFootView);/////
    }
    /**显示错误*/
    @Override
    public void showLoadMoreErro(String erroMsg) {
        if (lvRepos.getFooterViewsCount()==0){
            lvRepos.addFooterView(hotFootView);
        }
        hotFootView.showError(erroMsg);
    }
    /**添加更多数据*/
    @Override
    public void addMoreData(List<Repo> data) {
        adapter.addAll(data);
    }
}
