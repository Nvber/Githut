package com.feicui.zh.favorite.dao;

import com.feicui.zh.favorite.model.LocalRepo;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Administrator on 2016/8/5.
 */
public class LocalRepoDao {
    private Dao<LocalRepo, Long> dao;

    public LocalRepoDao(DBHelp dbHelp) {
        try {
            dao = dbHelp.getDao(LocalRepo.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /***
     * 添加或更新本地仓库数据
     */
    public void createOrUpdate(LocalRepo localRepo) {
        try {
            dao.createOrUpdate(localRepo);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /***
     * 添加或更新本地仓库数据
     */
    public void createOrUpdate(List<LocalRepo> localRepos) {
        for (LocalRepo localRepo : localRepos) {
            createOrUpdate(localRepo);
        }
    }

    /**
     * 删除本地仓库数据
     */
    public void delete(LocalRepo localRepo){
        try {
            dao.delete(localRepo);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 查询本地仓库(图像处理的，架构的...,能查到全部或未分类的？？？)
     *
     * @param groupId 类别ID号
     * @return 仓库列表
     */
    public List<LocalRepo> queryForGroupId(int groupId){
        try {
            return dao.queryForEq(LocalRepo.COLUMN_GROUP_ID, groupId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * 查询本地仓库(未分类的)
     */
    public List<LocalRepo> queryForNoGroup(){
        try {
            return dao.queryBuilder().where().isNull(LocalRepo.COLUMN_GROUP_ID).query();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 查询本地仓库(全部的)
     */
    public List<LocalRepo> queryForAll(){
        try {
            return dao.queryForAll();
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }
}
