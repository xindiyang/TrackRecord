package com.example.codoon.myapplication.SportLines;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * 创建者：Sunday
 * 项目名：coddonGMap
 * 包名：com.example.codoon.myapplication.SportLines
 * 文件名：MyApplication
 * 创建时间：2018/10/25 上午10:55
 * 描述：TODO
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化DBFlow
        FlowManager.init(new FlowConfig.Builder(this).build());
    }
}
