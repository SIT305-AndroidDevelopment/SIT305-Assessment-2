package com.example.assessment_2;

import android.app.Application;
import android.content.Context;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.example.assessment_2.util.UserInfoManager;

public class AssessmentApplication extends Application {

  private static Context mContext;

  @Override
  public void onCreate() {
    super.onCreate();
    mContext = getApplicationContext();

    //在使用SDK各组件之前初始化context信息，传入ApplicationContext
    SDKInitializer.initialize(this);
    //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
    //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
    SDKInitializer.setCoordType(CoordType.BD09LL);
//    InitializationConfig config = InitializationConfig.newBuilder(this)
//        .connectionTimeout(30 * 1000)
//        .readTimeout(30 * 1000)
//        .retry(10)
//        .build();
//    NoHttp.initialize(config);
    UserInfoManager.getInstance().init(this);
    initAppData();

    UserInfoManager.getInstance().init(this);
  }

  private void initAppData() {

  }

  /**
   * 提供一个应用级别的全局上下文mContext
   */
  public static Context getContext() {
    return mContext;
  }
}
