package com.feicui.zh.ganh.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/8/5.
 */
public class GanHResult {
    private boolean error;

    private List<String> category;

    private Results results;

    public boolean isError() {
        return error;
    }

    public List<String> getCategory() {
        return category;
    }

    public Results getResults() {
        return results;
    }

    public static class Results {

        @SerializedName("Android")
        private List<GanHItem> androidItems;

        public List<GanHItem> getAndroidItems() {
            return androidItems;
        }
    }
}
