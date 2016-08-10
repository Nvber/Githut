package com.feicui.zh.hotfragment.model;

import com.feicui.zh.login.model.User;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/1.
 */
public class Repo implements Serializable {

    private int id;
    // 仓库名称
    private String name;
    // 仓库全名
    @SerializedName("full_name")
    private String fullName;
    // 仓库描述
    private String description;
    // 本仓库的star数量 (在GitHub上被关注的数量)
    @SerializedName("stargazers_count")
    private int starCount;
    // 本仓库的fork数量 (在GitHub上被拷贝的数量)
    @SerializedName("forks_count")
    private int forkCount;

    // 该仓库的拥有者
    private User owner;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFullName() {
        return fullName;
    }

    public String getDescription() {
        return description;
    }

    public int getStarCount() {
        return starCount;
    }

    public int getForkCount() {
        return forkCount;
    }

    public User getOwner() {
        return owner;
    }

}
