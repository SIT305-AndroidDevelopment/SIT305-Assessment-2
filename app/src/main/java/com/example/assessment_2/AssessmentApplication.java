package com.example.assessment_2;

import android.app.Application;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;

public class AssessmentApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(this);

        SDKInitializer.setCoordType(CoordType.BD09LL);

        initAppData();
    }

    private void initAppData() {

    }
}
