package com.example.fourth.notificationdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "MyReceiver";

    public MyReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        //验证一下：看收到广播 是不是我们Manifest中的
        String action = intent.getAction();
        Log.i(TAG,action);//MyReceiver: com.cskaoyan.sendBroadcast
        String url = intent.getStringExtra("url");
        Log.i(TAG,url);///MyReceiver: http://www.baidu.com


        //收到广播后，启动另一个Activity
        //加上一个Flag，防止没有任务栈存放Activity,和之前的在service中启动Activity类似
        Intent intent1 = new Intent();
        intent1.setAction("com.cskaoyan.showNews");
        intent1.putExtra("url","http://www.taobao.com");
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent1);
    }
}
