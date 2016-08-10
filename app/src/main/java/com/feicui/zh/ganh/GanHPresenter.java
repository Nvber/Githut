package com.feicui.zh.ganh;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;

import com.feicui.zh.ganh.model.GanHItem;
import com.feicui.zh.ganh.model.GanHResult;
import com.feicui.zh.ganh.network.GanHClient;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2016/8/5.
 */
public class GanHPresenter {

    /**
     * 每日干货业务，视图接口
     */
    public interface GanHView {
        void showEmptyView();

        void hideEmptyView();

        void showMessage(String msg);

        void setData(List<GanHItem> ganhItems);
    }

    private Call<GanHResult> call;
    private GanHView ganhView;

    public GanHPresenter(@NonNull GanHView ganhView) {
        this.ganhView = ganhView;
    }

    /**
     * 获取每日干货数据,通过日期
     */
    public void getGanks(Date date) {
        // 得到year,monty,day
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int monty = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        call = GanHClient.getInstance().getDailyData(year, monty, day);
        call.enqueue(callback);
    }

    private Callback<GanHResult> callback = new Callback<GanHResult>() {
        @Override
        public void onResponse(Call<GanHResult> call, Response<GanHResult> response) {
            GanHResult ganHResult = response.body();
            if (ganHResult == null){
                ganhView.showMessage("未知错误");
                return;
            }
            //没数据
            if (ganHResult.isError()
                    || ganHResult.getResults() == null
                    || ganHResult.getResults().getAndroidItems() == null
                    || ganHResult.getResults().getAndroidItems().isEmpty()) {
                ganhView.showEmptyView();
                return;
            }
            List<GanHItem> ganHItems = ganHResult.getResults().getAndroidItems();
            //获取今日干货数据
            ganhView.hideEmptyView();
            ganhView.setData(ganHItems);
        }

        @Override
        public void onFailure(Call<GanHResult> call, Throwable t) {
            ganhView.showMessage("Error:" + t.getMessage());
        }
    };

}
