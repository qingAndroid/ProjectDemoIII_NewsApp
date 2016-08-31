package com.example.fourth.projectdemoiii_newsapp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;


/*
JPush SDK 收到推送，通过广播的方式，转发给开发者App，这样开发者就可以灵活地进行处理。

        这个动作不是必须的。用户有需要才定义 Receiver 类来处理 SDK过来的广播。

        如果不做这个动作，即不写自定义 Receiver，也不在 AndroidManifest.xml 里配置这个 Receiver，则默认的行为是：

        接收到推送的自定义消息，则没有被处理
        可以正常收到通知，用户点击打开应用主界面
*/


public class MyPushReceiver extends BroadcastReceiver {
    private static final String TAG = "MyPushReceiver";

    public MyPushReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        Log.i(TAG,intent.getAction());

        //客户端收到jpush广播后，可以自己下一步做什么
        //这里收到后，去启动Activity
       if(intent.getAction().equals("cn.jpush.android.intent.NOTIFICATION_OPENED")) {

      //我们想实现的推送效果是，让服务器在推送时，直接将url发送给客户端，让客户端在点击通知的时候，跳到新闻页面
           //对应 Portal 推送消息界面上的“可选设置”里的附加字段。
        //保存服务器推送下来的附加字段。这是个 JSON 字符串。进行json解析，
           Bundle bundle = intent.getExtras();
           String json = bundle.getString(JPushInterface.EXTRA_EXTRA);

           try {
               JSONObject jsonObject = new JSONObject(json);
               String url = jsonObject.getString("url");

               //跳到下个Activity
               Intent intent1 = new Intent();
               intent1.setAction("com.cskaoyan.showNews");
               intent1.putExtra("url",url);
               intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               context.startActivity(intent1);

           } catch (JSONException e) {
               e.printStackTrace();
           }
       }

    }
}
