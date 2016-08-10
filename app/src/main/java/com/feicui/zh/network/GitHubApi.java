package com.feicui.zh.network;

import com.feicui.zh.hotfragment.model.ReposResult;
import com.feicui.zh.repoinfo.RepoContentResult;
import com.feicui.zh.hotuser.HotUserResult;
import com.feicui.zh.login.model.AccessTokenResult;
import com.feicui.zh.login.model.User;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2016/7/29.
 */
public interface GitHubApi {
    // 获取square公司retrofit仓库的所有参与者信息
    // 请求方式:Get
    // 请求路径:repos/square/retrofit/contributors
    // 请求参数：无
    // 请求头：无(其实OKHTTP内部会帮我们做一些基本数据补全)
    // 最终首次构建完成了一个Call模型
//    @GET("repos/square/retrofit/contributors")
//    Call<ResponseBody> getRetrofitContributors();

    String CALL_BACK = "feicui";
    // GitHub开发者，申请就行
    String CLIENT_ID = "aa7a3fb1b42f8c07a232";
    String CLIENT_SECRET = "841a9cfd15ded1abb9d75ba51d2d8dd0189ebb77";

    // 授权时申请的可访问域
    String AUTH_SCOPE = "user,public_repo,repo";
    // 授权登陆页面(用WebView来加载)
    String AUTH_RUL = "https://github.com/login/oauth/authorize?client_id=" + CLIENT_ID + "&scope=" + AUTH_SCOPE;

    /**
     * 获取访问令牌API
     */
    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("https://github.com/login/oauth/access_token")
    Call<AccessTokenResult> getOAuthToken(
            @Field("client_id") String client,
            @Field("client_secret") String clientSecret,
            @Field("code") String code);

    /**
     * 获取当前token用户信息
     */
    @GET("user")
    Call<User> getUserInfo();

    /**
     * 获取仓库列表
     * @Param query 查询参数(language:java)
     * @Param pageId 查询页数据(从1开始)
     */
    @GET("/search/repositories")
    Call<ReposResult> searchRepos(
            @Query("q")String query,
            @Query("page")int pageId);

    /***
     * 获取readme
     * @param owner 仓库拥有者
     * @param repo 仓库名称
     * @return 仓库的readme页面内容,将是markdown格式且做了base64处理
     */
    @GET("/repos/{owner}/{repo}/readme")
    Call<RepoContentResult> getReadme(
            @Path("owner") String owner,
            @Path("repo") String repo);


    /***
     * 获取一个markdonw内容对应的HTML页面
     *
     * @param body 请求体,内容来自getReadme后的RepoContentResult
     */
    @Headers({
            "Content-Type:text/plain"
    })
    @POST("/markdown/raw")
    Call<ResponseBody> markDown(@Body RequestBody body);


    /**
     * 获取用户列表
     * @Param query 查询参数(followers:>1000)
     * @Param pageId 查询页数据(从1开始)
     */
    @GET("/search/users")
    Call<HotUserResult> searchUsers(
            @Query("q")String query,
            @Query("page")int pageId);

    /**
     * 获取用户
     * @Param login 请求路径,用户名
     */
    @GET("/users/{login}")
    Call<User> getUser(@Path("login")String login);


}
