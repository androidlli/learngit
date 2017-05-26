package com.cango.palmcartreasure.net;

import com.cango.palmcartreasure.MtApplication;
import com.cango.palmcartreasure.api.Api;
import com.cango.palmcartreasure.util.CommUtil;
import com.umeng.message.PushAgent;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by cango on 2017/3/28.
 */

public class NetManager {
    private static final int DEFAULT_TIMEOUT = 30;
    private static NetManager mNetManager;

    private NetManager() {

    }

    public static NetManager getInstance() {
        if (CommUtil.checkIsNull(mNetManager))
            mNetManager = new NetManager();
        return mNetManager;
    }

    public <T> T create(Class<T> service) {
        Retrofit retrofit = new Retrofit.Builder()
                .client(getOkHttpClient())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(getBaseUrl(service))
                .build();
        return retrofit.create(service);
    }

    private <T> String getBaseUrl(Class<T> service) {
        String baseUrl = null;
        try {
            Field base_url = service.getField("BASE_URL");
            baseUrl = (String) base_url.get(service);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return baseUrl;
    }

    public OkHttpClient getOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        //配置log打印拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(loggingInterceptor);
        //配置request header 添加的token拦截器
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                String token = MtApplication.mSPUtils.getString(Api.TOKEN);
                String deviceToken = null;
                PushAgent pushAgent = PushAgent.getInstance(MtApplication.getmContext());
                if (pushAgent!=null)
                    deviceToken = pushAgent.getRegistrationId();
                if (CommUtil.checkIsNull(token))
                    return chain.proceed(originalRequest);
                Request request = chain.request().newBuilder()
                        .addHeader("Authorization", token)
                        .addHeader("DEVICETOKEN",deviceToken)
                        .build();
                return chain.proceed(request);
            }
        });
        return builder.build();
    }
}
