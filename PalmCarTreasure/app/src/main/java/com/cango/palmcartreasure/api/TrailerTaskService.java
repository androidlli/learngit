package com.cango.palmcartreasure.api;

import com.cango.palmcartreasure.model.TypeTaskData;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by cango on 2017/4/24.
 */

public interface TrailerTaskService {
    String BASE_URL=Api.BASE_URL;

    @GET("trailer/newtasklist")
    Observable<TypeTaskData> getNewTaskList(@Query("userid") String userId,@Query("LAT") float lat,@Query("LON") float lon,@Query("token") String token);

    @GET("trailer/taskinprogresslist")
    Observable<TypeTaskData> getTaskInprogressList(@Query("userid") String userId,@Query("LAT") float lat,@Query("LON") float lon,@Query("token") String token);

    @GET("trailer/taskdonelist")
    Observable<TypeTaskData> getTaskDoneList(@Query("userid") String userId,@Query("LAT") float lat,@Query("LON") float lon,@Query("token") String token);
}
