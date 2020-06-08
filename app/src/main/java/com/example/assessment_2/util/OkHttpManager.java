package com.example.assessment_2.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;

import com.example.assessment_2.base.BaseResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.request.RequestCall;

import java.io.File;
import java.util.Map;

import okhttp3.Call;

public class OkHttpManager {
  /*=======================Model=================*/
  /**
   * 是否手动取消Http
   */
  private boolean isCancelNormal = false;
  /**
   * 当前请求网络的上下文
   */
  private Context mContext;
  /**
   * 设置超时时间
   */
  private long connTimeOut = 1000 * 30;
  /**
   * 是否显示加载中对话框
   */
  private boolean isShowLoading = false;
  /**
   * 每一个请求的OkHttp实例
   */
  private RequestCall call = null;
  /**
   * 解析成功时要返回的数据对象
   */
  private Class responseClass;
  /**
   * 请求成功的实体类
   */
  private ResponseCallback mResponseCallback;
  /**
   * 下载文件时请求成功的实体类
   */
  private DownLoadFileRespCallback mDownloadFileRespCallBack;
  /**
   * 传过来的url
   */
  private String url = "";
  /**
   * post请求的json
   */
  private String postJson = "";
  /**
   * 是否立即显示对话框
   */
  private boolean isNowDialog;
  /**
   * 单个文件 File
   */
  private File file = null;
  /**
   * 添加图片时候的 路径
   */
  private String profileImage = "";
  /**
   * 上传文件时的文件名
   */
  private String fileName = "";
  /**
   * 下载文件时的文件名
   */
  private String downloadFileName = "";
  /*================View==================*/
  /**
   * 加载中对话框
   */
  private ProgressDialog mLoadingDialog = null;

  /*=================Control================*/

  /*=================LifeCycle================*/

  /**
   * 普通 接口请求
   *
   * @param mContext          当前应用的上下文
   * @param url               要请求的接口url 例：/web-app/createActivity/viewActInfo
   *                          默认参数类型为：application/json; charset=UTF-8
   * @param request           对应的请求实体类,,为null时，表示get请求
   * @param responseClass     对应的响应实体类模型
   * @param isShowLoading     请求过程中是否显示“加载中...”对话框
   * @param mResponseCallback 操作成功与失败的返回结果
   */
  public OkHttpManager(Context mContext, String url, Object request, Class responseClass,
                       boolean isShowLoading, ResponseCallback mResponseCallback) {
    this.mContext = mContext;
    this.url = url;
    this.responseClass = responseClass;
    this.isShowLoading = isShowLoading;
    this.mResponseCallback = mResponseCallback;
    if (request != null) {
      postJson = new Gson().toJson(request);
    }
  }

  /**
   * 上传图片、文件 单个上传
   * 接口请求
   *
   * @param profileImage      传给服务器时 文件的 key
   * @param mContext
   * @param url
   * @param file
   * @param responseClass
   * @param isShowLoading
   * @param mResponseCallback
   */
  public OkHttpManager(String profileImage, String fileName, Context mContext, String url, File file, Object request,
                       Class responseClass, boolean isShowLoading, ResponseCallback mResponseCallback) {
    this.profileImage = profileImage;
    this.fileName = fileName;
    this.mContext = mContext;
    this.url = url;
    this.file = file;
    this.responseClass = responseClass;
    this.isShowLoading = isShowLoading;
    this.mResponseCallback = mResponseCallback;
    if (request != null) {
      postJson = new Gson().toJson(request);
    }
  }


  /**
   * 开始请求
   */
  public void execute() {
    if (!this.check(mContext)) {  //判断网络
      if (mResponseCallback != null) {//接口异常
        mResponseCallback.onError(-1, 0, "网络断开");
      }
      return;
    }
    if (isShowLoading) {
      showNormalLoading();
    }

    try {
      if (postJson == null || TextUtils.isEmpty(postJson)) {//get请求
        call = OkHttpUtils
            .get()
            .url(url)
            .addHeader("Content_Type", "application/x-www-form-urlencoded;charset=utf-8")
            .build()
            .connTimeOut(connTimeOut);
      } else {
        /* 将JSON数据转换为Map<String, String> */
        GsonBuilder gb = new GsonBuilder();
        Gson g = gb.create();
        Map<String, String> map = g.fromJson(postJson, new TypeToken<Map<String, String>>() {
        }
            .getType());
        /* 设置okhttp */

        call = OkHttpUtils
            .post()
            .url(url)
            .params(map)
            .addHeader("Content_Type", "application/x-www-form-urlencoded; charset=utf-8")
            .build()
            .connTimeOut(connTimeOut);
      }
    } catch (Exception e) {
      if (mLoadingDialog != null) {
        mLoadingDialog.dismiss();
        mLoadingDialog = null;
      }
      if (mResponseCallback != null) {
        Log.e("TAG", "==okHttp error " + e.toString());
        mResponseCallback.onError(3, 0, "请求设置的参数异常");//数据解析异常
      }
      return;
    }

    Log.i("TAG", "==okHttp request \n url :" + url + "\npostJson :" + postJson);
    call.execute(new StringCallback() {
      @Override
      public void onError(Call call, Exception e, int id) {
        if (mLoadingDialog != null) {
          mLoadingDialog.dismiss();
          mLoadingDialog = null;
        }
        if (isCancelNormal) {//手动断开
          return;
        }
        if (e.toString().contains("SocketTimeoutException")) {//网络超时
          if (mResponseCallback != null) {//接口异常
            mResponseCallback.onError(0, id, "连接超时,请稍后再试");
          }
        } else if (e.toString().contains("UnknownHostException")) {//网络断开
//                    Toast.showToast("网络断开", JJBToast.WARNING);
          if (mResponseCallback != null) {//接口异常
            mResponseCallback.onError(-1, id, "网络断开,请检查网络连接");
          }
        } else {
          if (mResponseCallback != null) {//接口异常
            mResponseCallback.onError(1, id, "服务器没有响应,请联系管理员");
          }
        }

      }

      @Override
      public void onResponse(String response, int id) {
        if (mLoadingDialog != null) {
          mLoadingDialog.dismiss();
          mLoadingDialog = null;
        }

        Object object = null;
        try {
          Log.i("TAG", "==okHttp responseJson " + url + ": \n" + response);

          object = new Gson().fromJson(response, responseClass);
        } catch (Exception e) {
          e.printStackTrace();
          if (mResponseCallback != null) {
            Log.e("TAG", "==okHttp error " + e.toString());
            mResponseCallback.onError(2, 0, "数据解析异常");//数据解析异常
          }
        }
        /*判断code为非200的各种状态的toast*/
        if (object instanceof BaseResponse) {
          BaseResponse baseResponse = (BaseResponse) object;
          if (baseResponse.status != 0) {
            mResponseCallback.onError(-1, 0, baseResponse.message);
          }
        }
        if (mResponseCallback != null && object != null) {
          mResponseCallback.onSuccess(object);
        }
      }
    });
  }

  /**
   * 上传文件请求
   */
  public void executeFile() {
    if (!this.check(mContext)) {  //判断网络
//            JJBToast.showToast("网络断开", JJBToast.WARNING);
      if (mResponseCallback != null) {//接口异常
        mResponseCallback.onError(-1, 0, "网络断开");
      }
      return;
    }
    if (this.file == null) {
      if (mResponseCallback != null) {//接口异常
        mResponseCallback.onError(-1, 0, "图片上传失败");
      }
      return;
    }
    if (isShowLoading) {
      showNormalLoading();
    }
    try {
      /* 将JSON数据转换为Map<String, String> */
      GsonBuilder gb = new GsonBuilder();
      Gson g = gb.create();
      Map<String, String> map = g.fromJson(postJson, new TypeToken<Map<String, String>>() {
      }
          .getType());
      call = OkHttpUtils.post()//
          .params(map)
          .addFile(profileImage, fileName, file)
          .url(url)
//                    .params(params)//
          .addHeader("Content_Type", "application/x-www-form-urlencoded; charset=utf-8")
          .build();
    } catch (Exception e) {
      if (mLoadingDialog != null) {
        mLoadingDialog.dismiss();
        mLoadingDialog = null;
      }
      if (mResponseCallback != null) {
        Log.e("TAG", "==okHttp error " + e.toString());
        mResponseCallback.onError(3, 0, "请求设置的参数异常");//数据解析异常
      }
      return;
    }
    Log.i("TAG", "==okHttp request \n url :" + url + "\npostJson :" + postJson);
    call.execute(new StringCallback() {
      @Override
      public void onError(Call call, Exception e, int id) {
        if (mLoadingDialog != null) {
          mLoadingDialog.dismiss();
          mLoadingDialog = null;
        }
        if (isCancelNormal) {//手动断开
          return;
        }
        if (e.toString().contains("SocketTimeoutException")) {//网络超时
          if (mResponseCallback != null) {//接口异常
            mResponseCallback.onError(0, id, "连接超时,请稍后再试");
          }
        } else if (e.toString().contains("UnknownHostException")) {//网络断开
//                    Toast.showToast("网络断开", JJBToast.WARNING);
          if (mResponseCallback != null) {//接口异常
            mResponseCallback.onError(-1, id, "网络断开,请检查网络连接");
          }
        } else {
          if (mResponseCallback != null) {//接口异常
            mResponseCallback.onError(1, id, "连接服务器失败,请联系管理员");
          }
        }

      }

      @Override
      public void onResponse(String response, int id) {
        if (mLoadingDialog != null) {
          mLoadingDialog.dismiss();
          mLoadingDialog = null;
        }

        Object object = null;
        try {
          Log.i("TAG", "==okHttp responseJson " + url + ": \n" + response);

          object = new Gson().fromJson(response, responseClass);
        } catch (Exception e) {
          e.printStackTrace();
          if (mResponseCallback != null) {
            Log.e("TAG", "==okHttp error " + e.toString());
            mResponseCallback.onError(2, 0, "数据解析异常");//数据解析异常
          }
        }
        if (mResponseCallback != null && object != null) {
          mResponseCallback.onSuccess(object);
        }
      }
    });
  }


  /**
   * 取消当前接口请求
   */
  public void cancel() {
    if (mLoadingDialog != null) {
      mLoadingDialog.dismiss();
      mLoadingDialog = null;
    }
    if (call != null) {
      isCancelNormal = true;
      call.cancel();
    }
  }

  /**
   * 响应的操作接口
   */
  public interface ResponseCallback {
    /**
     * 失败原因
     *
     * @param errorType -1：网络断开  ０:超时　　1：网络异常,http的code码非200　　２：根据响应Bean数据解析异常  3：请求设置的参数异常
     * @param errorCode 　网络请求错误的code码
     * @param errorMsg  具体的异常信息（通常没有）
     */
    void onError(int errorType, int errorCode, String errorMsg);

    /**
     * 请求成功
     *
     * @param response 对应的成功的实体类
     */
    void onSuccess(Object response);

  }

  /**
   * 下载文件时响应的操作接口
   */
  public interface DownLoadFileRespCallback {
    /**
     * 失败原因
     *
     * @param errorType -1：网络断开  ０:超时　　1：网络异常,http的code码非200　　２：根据响应Bean数据解析异常  3：请求设置的参数异常
     * @param errorCode 　网络请求错误的code码
     * @param errorMsg  具体的异常信息（通常没有）
     */
    void onError(int errorType, int errorCode, String errorMsg);

    /**
     * 请求成功
     *
     * @param file 下载成功后的文件
     */
    void onSucc(File file);

    /**
     * 请求成功
     *
     * @param progress 当前下载的进度
     */
    void onDownloadProgress(float progress);

  }

  /*=================================*/

  /**
   * 显示加载中对
   */
  public void showNormalLoading() {
    Log.i("TAG", "showNormalLoading");
    if (mLoadingDialog != null) {
      mLoadingDialog.dismiss();
      mLoadingDialog = null;
    }
    /*显示与关掉不到1秒,则延迟1秒关闭*/
    mLoadingDialog = ProgressDialog.show(mContext, null, "正在加载...");
    mLoadingDialog.setCancelable(false);
    mLoadingDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
      @Override
      public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
        if (i == KeyEvent.KEYCODE_BACK) {
          if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
          }
        }
        return false;
      }
    });
    //控制显示时常
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
//                if (System.currentTimeMillis() - startTime > 1000 * 1) {//本次请求大于一秒，需要弹出请求框
        if (mLoadingDialog != null) {
          mLoadingDialog.show();
        }
//                }
      }
    }, 1000 * 0);

  }

  /**
   * check net state
   * 检查网络是否联网以及网络状态
   *
   * @param context
   * @return
   */
  private Boolean check(Context context) {
    boolean isNet = true;
    ConnectivityManager connectMgr = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
    if (connectMgr != null) {
      NetworkInfo mobNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
      NetworkInfo wifiNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
      if (mobNetInfo == null && wifiNetInfo != null) {
        //没连接移动也没连接wifi
        if (!wifiNetInfo.isConnected()) {
          isNet = false;
        }
      } else if (mobNetInfo != null && wifiNetInfo == null) {
        if (!mobNetInfo.isConnected()) {
          isNet = false;
        }
      } else if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
        //没连接移动也没连接wifi
        isNet = false;
      }
    }
    return isNet;
  }
}
