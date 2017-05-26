package com.cango.palmcartreasure.api;

import com.cango.palmcartreasure.model.GroupList;
import com.cango.palmcartreasure.model.GroupTaskCount;
import com.cango.palmcartreasure.model.GroupTaskQuery;
import com.cango.palmcartreasure.model.TaskAbandon;
import com.cango.palmcartreasure.model.TaskAbandonRequest;
import com.cango.palmcartreasure.model.TaskManageList;

import java.util.List;
import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by cango on 2017/5/9.
 */

public interface AdminService {
    String BASE_URL = Api.BASE_URL;

    //组任务统计，所有组列表
    @GET("trailer/grouptaskcount")
    Observable<GroupTaskCount> getGroupTaskCount(@Query("userid") int userId, @Query("LAT") double lat, @Query("LON") double lon);

    //所有管理员未分配的任务
    @GET("trailer/taskmanagelist")
    Observable<TaskManageList> getTaskManageList(@Query("userid") int userId, @Query("LAT") double lat,
                                                 @Query("LON") double lon, @Query("pageIndex") int pageIndex,
                                                 @Query("pageSize") int pageSize);

    ///api/trailer/grouptaskquery?userid={userid}&LAT={LAT}&LON={LON}&pageIndex={pageIndex}&pageSize={pageSize}&ApiToken={APITOKEN}
    //分组任务查询
    @POST("trailer/grouptaskquery")
//    Observable<GroupTaskQuery> getGroupTaskQuery(@Query("userid") int userId, @Body int[] groupIds, @Query("LAT") float lat,
//                                                 @Query("LON") float lon, @Query("pageIndex") int pageIndex,
//                                                 @Query("pageSize") int pageSize);
    Observable<GroupTaskQuery> getGroupTaskQuery(@Body Map<String,Object> objectMap);

    //POST /api/trailer/grouptaskdraw?userid={userid}&ApiToken={APITOKEN}
    //组任务抽回接口
    @POST("trailer/grouptaskdraw")
//    Observable<TaskAbandon> groupTaskDraw(@Query("userid") int userId, @Body List<GroupTaskQuery.DataBean.TaskListBean> taskListBeanList);
    Observable<TaskAbandon> groupTaskDraw(@Body Map<String,Object> objectMap);

    ///api/trailer/taskabandon
    //放弃任务接口
    @POST("trailer/taskabandon")
//    Observable<TaskAbandon> TaskAbandon(@Query("userid") int userId, @Body TaskAbandonRequest[] requests);
    Observable<TaskAbandon> TaskAbandon(@Body Map<String,Object> objectMap);

    //POST /api/trailer/taskarrange?userid={userid}&groupid={groupid}&ApiToken={APITOKEN}
    //任务分配接口
    @POST("trailer/taskarrange")
//    Observable<TaskAbandon> taskArrange(@Query("userid") int userId, @Query("groupid") int groupId, @Body List<TaskManageList.DataBean.TaskListBean> taskListBeanList);
    Observable<TaskAbandon> taskArrange(@Body Map<String,Object> objectMap);

    //GET /api/trailer/grouplist?userid={userid}&type={type}&ApiToken={APITOKEN}:type:T,已分组；F：未分组
    @GET("trailer/grouplist")
    Observable<GroupList> getGroupList(@Query("userid") int userId, @Query("type") String type);

    //POST /api/trailer/groupmdf?userid={userid}&ApiToken={APITOKEN}
    //分组编辑接口
    @POST("trailer/groupmdf")
    Observable<TaskAbandon> groupMDF(@Query("userid") int userId, @Body List<GroupList.DataBean.GroupListBean> groupListBeanList);

    @POST("trailer/groupmdf")
    Observable<TaskAbandon> groupMDFTest(@Body Map<String,Object> objectMap);
}
