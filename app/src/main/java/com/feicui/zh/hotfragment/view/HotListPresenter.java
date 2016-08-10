package com.feicui.zh.hotfragment.view;

import com.feicui.zh.hotfragment.Language;
import com.feicui.zh.hotfragment.model.Repo;
import com.feicui.zh.hotfragment.model.ReposResult;
import com.feicui.zh.network.GitHubClient;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2016/7/28.
 * P
 */
public class HotListPresenter {

    private HotListView hotListView;
    private int nextPage = 0;
    private Language language;
    private Call<ReposResult> repoCall;

    public HotListPresenter(HotListView hotListView,Language language) {
        this.hotListView = hotListView;
        this.language = language;
    }
    /**刷新*/
    public void refresh(){
        // 隐藏loadmore
        hotListView.hideLoadMoreLoading();
        hotListView.showContentView();
        nextPage = 1; // 永远刷新最新数据
        repoCall = GitHubClient.getInstance().searchRepos(
                "language:" + language.getPath()
                , nextPage);
        repoCall.enqueue(refreshcallback);

    }
    private final Callback<ReposResult> refreshcallback = new Callback<ReposResult>() {
        @Override public void onResponse(Call<ReposResult> call, Response<ReposResult> response) {
            // 视图停止刷新
            hotListView.stopRefresh();
            // 得到响应结果
            ReposResult reposResult = response.body();
            if (reposResult == null) {
                hotListView.showErrorView("结果为空!");
                return;
            }
            // 当前搜索的语言,没有仓库
            if (reposResult.getTotalCount() <= 0) {
                hotListView.refreshData(null);
                hotListView.showEmptyView();
                return;
            }
            // 取出当前语言下的所有仓库
            List<Repo> repoList = reposResult.getRepoList();
            hotListView.refreshData(repoList);
            // 下拉刷新成功(1), 下一面则更新为2
            nextPage = 2;
        }

        @Override public void onFailure(Call<ReposResult> call, Throwable t) {
            // 视图停止刷新
            hotListView.stopRefresh();
            hotListView.showMessage("repoCallback onFailure" + t.getMessage());
        }
    };
    /**加载更多*/
    public void loadMore(){
        hotListView.showLoadMoreLoading();
        repoCall = GitHubClient.getInstance().searchRepos(
                "language:" + language.getPath()
                , nextPage);
        repoCall.enqueue(loadMoreCallback);
    }
    private final Callback<ReposResult> loadMoreCallback = new Callback<ReposResult>(){

        @Override public void onResponse(Call<ReposResult> call, Response<ReposResult> response) {
            hotListView.hideLoadMoreLoading();/////
            // 得到响应结果
            ReposResult reposResult = response.body();
            if (reposResult == null) {
                hotListView.showLoadMoreErro("结果为空!");
                return;
            }
            // 取出当前语言下的所有仓库
            List<Repo> repoList = reposResult.getRepoList();
            hotListView.addMoreData(repoList);
            nextPage++;
        }

        @Override public void onFailure(Call<ReposResult> call, Throwable t) {
            hotListView.hideLoadMoreLoading();
            hotListView.showMessage("repoCallback onFailure" + t.getMessage());
        }
    };
}
