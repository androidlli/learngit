package com.cango.palmcartreasure.api;

import com.cango.palmcartreasure.model.LoginData;
import com.cango.palmcartreasure.model.MessageList;
import com.cango.palmcartreasure.model.PersonMain;
import com.cango.palmcartreasure.model.PersonalInfo;
import com.cango.palmcartreasure.model.TaskAbandon;

import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by cango on 2017/4/18.
 */

public interface LoginService {
    String BASE_URL=Api.BASE_URL;

    @GET("user/login")
    Observable<LoginData> getLoginData(@Query("username") String username,@Query("password") String password,
                                       @Query("imei") String imei,@Query("lat") float lat,@Query("lon") float lon,
                                       @Query("deviceToken") String deviceToken,@Query("deviceType") String deviceType);

    //POST /api/user/logout?userid={userid}&LAT={LAT}&LON={LON}&ApiToken={APITOKEN}
    //app用户退出系统
    @POST("user/logout")
    Observable<TaskAbandon> logout(@Query("userid") int userId,@Query("lat") float lat,@Query("lon") float lon);

    @POST("user/logout")
    Observable<TaskAbandon> logoutTest(@Body Map<String,Object> stringMap);

    //GET /api/trailer/personmain?userid={userid}&ApiToken={APITOKEN}  个人信息主页列表传输接口
    @GET("trailer/personmain")
    Observable<PersonMain> getPersonMain(@Query("userid") int userId);

    //GET /api/trailer/personalinfo?userid={userid}&ApiToken={APITOKEN}   个人详细信息
    @GET("trailer/personalinfo")
    Observable<PersonalInfo> getPersonalInfo(@Query("userid") int userId);

    //GET /api/trailer/messagelist?userid={userid}&status={status}&ApiToken={APITOKEN}  消息列表传输接口
    @GET("trailer/messagelist")
    Observable<MessageList> getMessageList(@Query("userid") int userId,@Query("status") int status,@Query("pageIndex") int pageIndex,
                                           @Query("pageSize") int pageSize);

    //POST /api/trailer/messageread?ApiToken={APITOKEN}    消息阅读通知接口
    @POST("trailer/messageread")
    Observable<TaskAbandon> postMessageRead(@Body Map<String,Object> objectMap );
}
