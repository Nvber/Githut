package com.feicui.zh.ganh.network;

import com.feicui.zh.common.LoggingInterceptor;
import com.feicui.zh.ganh.model.GanHResult;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2016/8/5.
 */
public class GanHClient implements GanHApi {

    private static GanHClient sClient;

    public static GanHClient getInstance(){
        if (sClient == null) {
            sClient = new GanHClient();
        }
        return sClient;
    }

    private final GanHApi gankApi;

    private GanHClient(){
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new LoggingInterceptor())
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ENDPOINT)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        gankApi = retrofit.create(GanHApi.class);

    }

    @Override public Call<GanHResult> getDailyData(int year, int month, int day) {
        return gankApi.getDailyData(year, month, day);
    }
}
