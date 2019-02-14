package lchj.ylnews.mvp.model.api.service;

import java.util.List;

import io.reactivex.Observable;
import lchj.ylnews.app.utils.Const;
import lchj.ylnews.app.utils.PrefUtils;
import lchj.ylnews.mvp.model.entity.CatBean;
import lchj.ylnews.mvp.model.entity.LoginBody;
import lchj.ylnews.mvp.model.entity.NewsBean;
import lchj.ylnews.mvp.model.entity.NewsDetailsBean;
import lchj.ylnews.mvp.model.entity.TokenBean;
import lchj.ylnews.mvp.model.entity.UpvoteBean;
import lchj.ylnews.mvp.model.entity.UpvoteBody;
import lchj.ylnews.mvp.model.entity.UserBean;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CommonService {
    /**
     * 获取首页
     * @return
     */
//    @Headers("sign: "+Const.sign)
    @GET("index/index")
    Observable<ResultInfo<ResultIndex>> getIndexlist();

    /**
     * 获取详情页
     * @param id
     * @return
     */
//    @Headers("sign: "+Const.sign)
    @GET("news/{id}")
    Observable<ResultInfo<NewsDetailsBean>> getNewDetails(@Path("id") int id);

    /**
     * 获取栏目
     * @return
     */
//    @Headers("sign: "+Const.sign)
    @GET("cat")
    Observable<ResultInfo<List<CatBean>>> getCatList();

    /**
     * 根据栏目id获取新闻
     * @param catid
     * @return
     */
//    @Headers("sign: "+Const.sign)
    @GET("news")
    Observable<ResultInfo<ResultList<NewsBean>>> getNewlist(@Query("catid") int catid,
                                                            @Query("page") int page,
                                                            @Query("size") int size);

    @POST("login")
    Observable<ResultInfo<TokenBean>> ToLogin(@Body LoginBody Body);



    @GET("user/1")
    Observable<ResultInfo<UserBean>> getUsersData();

    @PUT("user/1")
    Observable<ResultInfo> getSaveUser(@Query("image") String url,
                                                 @Query("username") String UserName,
                                                 @Query("sex") int sex,
                                                 @Query("password") String UserPwd,
                                                 @Query("signature") String UserSign);

    /**
     * 是否点赞
     * @return
     */
    @GET("upvote/{id}")
    Observable<ResultInfo<UpvoteBean>> isUpvote(@Path("id") int id);

    /**
     * 点赞

     * @return
     */
    @POST("upvote")
    Observable<ResultInfo> toUpvote(@Body UpvoteBody upvoteBody);

    /**
     * 取消点赞
     * @param upvoteBody
     * @return
     *
     * 因为 自带的@DELETE不能使用@Body
     * 则重新定义个method = "DELETE"，同时设置hasBody = true，如下
     */
    @HTTP(method = "DELETE", path = "upvote", hasBody = true)
//    @DELETE("upvote")
    Observable<ResultInfo> toUpvoteNo(@Body UpvoteBody upvoteBody);


}
