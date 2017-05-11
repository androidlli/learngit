package com.cango.palmcartreasure.api;

import com.cango.palmcartreasure.model.LoginData;

import retrofit2.http.GET;
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

}
