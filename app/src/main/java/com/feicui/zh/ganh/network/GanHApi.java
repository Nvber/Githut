package com.feicui.zh.ganh.network;

import com.feicui.zh.ganh.model.GanHResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Administrator on 2016/8/5.
 */
public interface GanHApi {
    String ENDPOINT = "http://gank.io/api/";

    @GET("day/{year}/{month}/{day}")
    Call<GanHResult> getDailyData(
            @Path("year") int year,
            @Path("month") int month,
            @Path("day") int day);
}
