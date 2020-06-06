package com.example.assessment_2.util;

import android.net.Uri;
import android.os.Environment;

import com.example.assessment_2.R;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.TakePhotoOptions;

import java.io.File;

public class TakePhotoHelper {
  /**
   * 是否裁剪
   */
  private boolean isCrop = false;
  /**
   * 是否压缩
   */
  private boolean isCompress = false;
  /**
   * 压缩大小不超过
   * 单位：B
   */
  private int compressMaxSize = 100000;

  /**
   * 裁剪比例 宽度
   */
  private int width = 1;
  /**
   * 裁剪比例 高度
   */
  private int height = 1;
  /**
   * 是否保留原图
   */
  private boolean enableReserveRaw = true;

  /**
   * TakePahoto管理工具
   *
   * @param isCrop          是否裁剪
   * @param isCompress      是否压缩
   * @param compressMaxSize 压缩最大大小
   * @param width           裁剪比例 宽
   * @param height          裁剪比例 高
   * @return
   */
  public static TakePhotoHelper getHelper(boolean isCrop, boolean isCompress, int compressMaxSize,
                                          int width, int height) {
    return new TakePhotoHelper(isCrop, isCompress, compressMaxSize, width, height);
  }

  private TakePhotoHelper(boolean isCrop, boolean isCompress, int compressMaxSize, int width, int height) {
    this.isCrop = isCrop;
    this.isCompress = isCompress;
    this.compressMaxSize = compressMaxSize * 1000;
    this.width = width;
    this.height = height;
  }

  public void setEnableReserveRaw(boolean enableReserveRaw) {
    this.enableReserveRaw = enableReserveRaw;
  }

  /**
   * 拍照
   *
   * @param takePhoto
   */
  public void onClick(int viewID, TakePhoto takePhoto) {
    File file = new File(Environment.getExternalStorageDirectory(), "/images/" + System.currentTimeMillis() + ".jpg");
    if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
    Uri imageUri = Uri.fromFile(file);
    configCompress(takePhoto);
    configTakePhotoOption(takePhoto);
    if (viewID == R.id.btn_paizhao) {
      /* 拍照 */
      if (isCrop) {
        takePhoto.onPickFromCaptureWithCrop(imageUri, getCropOptions());
      } else {
        takePhoto.onPickFromCapture(imageUri);
      }
    } else if (viewID == R.id.btn_bendi) {
      if (isCrop) {
        takePhoto.onPickMultipleWithCrop(1, getCropOptions());
      } else {
        takePhoto.onPickMultiple(1);
      }
    }

  }

  /**
   * 配置
   *
   * @param takePhoto
   */
  private void configTakePhotoOption(TakePhoto takePhoto) {
    TakePhotoOptions.Builder builder = new TakePhotoOptions.Builder();
    //是否使用TakePhoto的相册
//        builder.setWithOwnGallery(true);
    /* 拍摄的照片是否纠正照片角度 */
    builder.setCorrectImage(true);
    takePhoto.setTakePhotoOptions(builder.create());

  }

  /**
   * 压缩相关配置
   *
   * @param takePhoto
   */
  private void configCompress(TakePhoto takePhoto) {
    if (!isCompress) {
      /* 是否压缩 */
      takePhoto.onEnableCompress(null, false);
      return;
    }
    int compressWidth = 1000;
    int compressHeight = 800;
    CompressConfig config;
    config = new CompressConfig.Builder()
        .setMaxSize(compressMaxSize)
        .setMaxPixel(compressWidth >= compressHeight ? compressWidth : compressHeight)
        .enableReserveRaw(enableReserveRaw)//是否保留原图
        .create();
    takePhoto.onEnableCompress(config, false);
  }

  /**
   * 裁剪相关配置
   *
   * @return
   */
  private CropOptions getCropOptions() {
    /* 是否使用第三方裁剪工具 */
    boolean withWonCrop = true;

    CropOptions.Builder builder = new CropOptions.Builder();
    builder.setAspectX(width).setAspectY(height);

    builder.setWithOwnCrop(withWonCrop);
    return builder.create();
  }

}
