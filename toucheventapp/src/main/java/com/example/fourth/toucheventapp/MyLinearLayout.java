package com.example.fourth.toucheventapp;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2016/8/25.
 */
public class MyLinearLayout extends LinearLayout{

    private static final String TAG = "out";


    public MyLinearLayout(Context context) {
        super(context);
    }

    public MyLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
   // 系统的常见的布局
    //linearlayout relativelayout framelayout。。
    //默认不会去拦截事件，即 intercept返回false,onTouch返回false



    //事件分发：用来进行事件的分发，如果事件能传递给该view，则此方法一定会被调用。
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.i(TAG,"dispatch---");
        return super.dispatchTouchEvent(event);
    }

    //事件拦截：用来判断是否拦截该事件，如果view拦截了某个事件，则在同一个事件序列中，该方法不会被再次调用

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.i(TAG,"Intercept---");

        return super.onInterceptTouchEvent(ev);
    }

    //事件处理：该方法的返回值表示是否消耗该事件。返回true表示消耗。False表示不消耗。如果不消耗，则同一个事件序列中，当前view无法再次接受到该事件序列的后续事件。

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        Log.i(TAG,"Touch---");

        return true;//super.onTouchEvent(event);
    }
}
