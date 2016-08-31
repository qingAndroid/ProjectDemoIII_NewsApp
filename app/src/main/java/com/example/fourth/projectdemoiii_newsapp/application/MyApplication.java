package com.example.fourth.projectdemoiii_newsapp.application;

import android.app.Application;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2016/8/30.
 */
public class MyApplication extends Application{
    //要在Manifest里的application节点 注册
        public void onCreate() {
            super.onCreate();
            JPushInterface.setDebugMode(true);
            JPushInterface.init(this);
        }
}
