package com.example.fourth.notificationdemo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

//屏幕适配的demo     	04screenadapterapp
//增加了guide向导上的Indicator宽度适配，及主页面的content宽度适配   	 app

//把通知栏推送push的SDK 集成到app中

//notificationdemo  : Notification的使用，单纯写通知栏弹出通知
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void showNotification(View v){

       //怎么实现 在通知栏上点击一条通知，然后在我们的应用程序中得到响应？
                // 和widget类似：因为通知栏是另一个应用程序，所以要在点击通知栏后我们的应用程序会有响应，
                // 应该通过发送Intent意图 ，让两个程序之间通信
                // pendingIntent（ 阻塞意图 不是立即会发送的，在特定的时候才会发送出去的），
                // 把pendingIntent给到notification,则在notification被我们点击之后才会发送出去的

        //1、点击Button，在通知栏弹通知
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("第一条通知标题");
        builder.setContentText("第一条通知的内容");
        builder.setSmallIcon(R.mipmap.ic_launcher);

        //2、在通知栏上点击一条通知，然后在我们的应用程序中得到响应.
        //方法一：点击通知后，通过Intent,隐式启动Activity

        /*Intent intent = new Intent();
        intent.setAction("com.cskaoyan.showNews");//要启动的Activity的Intent
        intent.putExtra("url","http://www.jiguang.com");//让intent携带数据到下个Activity
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1000, intent, PendingIntent.FLAG_ONE_SHOT);//使用默认的fla
        builder.setContentIntent(pendingIntent);//先设置pendingintent,在创建Notification*/

        //builder.setAutoCancel(true);//让点击之后通知消失

        //方法二：点击通知后，通过Intent 发送广播，我们的应用收到广播之后，决定去做什么事情

        //所以要写在我们的应用中写一个广播接收者,在收到广播后可以启动Activity
        Intent intent = new Intent();
        intent.setAction("com.cskaoyan.sendBroadcast");
        intent.putExtra("url","http://www.baidu.com");//让intent携带数据到下个Activity
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1000, intent, PendingIntent.FLAG_ONE_SHOT);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);

        Notification notification = builder.build();
        //notification.flags=Notification.FLAG_NO_CLEAR;//加上这个，通知就去除不掉了
        notificationManager.notify(1000,notification);
        //参数1  id  用来标识每条通知
        //参数2  Notification类  和创建对话框类似，用Builder创建Notification通知框长什么样子








    }
}
