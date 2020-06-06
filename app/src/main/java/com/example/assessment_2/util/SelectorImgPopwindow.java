package com.example.assessment_2.util;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.example.assessment_2.R;
import com.jph.takephoto.app.TakePhoto;


/**
 * 从哪里获取图片： 拍照、从相册选择、取消
 * Created by WangLu on 2017/3/6.
 */

public class SelectorImgPopwindow {

  private static volatile SelectorImgPopwindow _instance;

  private PopupWindow popwindow;

  private TakePhotoHelper helper;

  private TakePhoto photo = null;

  private Activity activity;

  private View.OnClickListener clickListener = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      if (v.getId() == R.id.btn_cancel) {
      } else if (v.getId() == R.id.btn_paizhao) {
        helper.setEnableReserveRaw(false);
        helper.onClick(R.id.btn_paizhao, photo);
      } else if (v.getId() == R.id.btn_bendi) {
        helper.setEnableReserveRaw(true);
        helper.onClick(R.id.btn_bendi, photo);
      }
      popwindow.dismiss();
    }
  };

  private SelectorImgPopwindow() {

  }

  public static SelectorImgPopwindow getInstance() {
    if (_instance == null) {
      synchronized (SelectorImgPopwindow.class) {
        if (_instance == null) {
          _instance = new SelectorImgPopwindow();
        }
      }
    }
    return _instance;
  }

  /**
   * @param activity
   * @param takePhoto
   * @param isCrop          是否裁剪
   * @param isCompress      是否压缩
   * @param compressMaxSize 压缩最大大小 kb
   * @param width           裁剪比例
   * @param height          裁剪比例
   */
  public void showPop(final Activity activity, TakePhoto takePhoto, boolean isCrop,
                      boolean isCompress, int compressMaxSize, int width, int height) {
    init(activity, takePhoto, isCrop, isCompress, compressMaxSize, width, height);

  }

  private void init(Activity activity, TakePhoto takePhoto, boolean isCrop, boolean isCompress, int compressMaxSize, int width, int height) {
    helper = TakePhotoHelper.getHelper(isCrop, isCompress, compressMaxSize, width, height);
    photo = takePhoto;
    LayoutInflater layoutInflater = LayoutInflater.from(activity);
    View popview = layoutInflater.inflate(R.layout.pop_picture_select, null);

    //本地相册
    RelativeLayout btnBendi = (RelativeLayout) popview.findViewById(R.id.btn_bendi);
    btnBendi.setOnClickListener(clickListener);
    //取消
    RelativeLayout btnCancel = (RelativeLayout) popview.findViewById(R.id.btn_cancel);
    btnCancel.setOnClickListener(clickListener);
    //拍照
    RelativeLayout btnCapture = (RelativeLayout) popview.findViewById(R.id.btn_paizhao);
    btnCapture.setOnClickListener(clickListener);

    popwindow = new PopupWindow(popview, LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.MATCH_PARENT);
    popwindow.setBackgroundDrawable(new ColorDrawable(0x80000000));
    popwindow.setAnimationStyle(R.style.AnimBottom);
    popwindow.setTouchable(true);
    popwindow.setOutsideTouchable(true);
    popwindow.setFocusable(true);
    //虚拟按钮遮挡pop 所以要添加这条
    popwindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    if (!popwindow.isShowing()) {
      //虚拟按钮遮挡pop 使用activity.getWindow().getDecorView()
      popwindow.showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }
  }

}
