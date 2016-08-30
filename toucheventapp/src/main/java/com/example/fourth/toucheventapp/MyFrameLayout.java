package com.example.fourth.toucheventapp;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Created by Administrator on 2016/8/25.
 */
public class MyFrameLayout extends FrameLayout{
    private static final String TAG = "middle";

    public MyFrameLayout(Context context) {
        super(context);
    }

    public MyFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        Log.i(TAG,"dispatch---");

        return super.dispatchTouchEvent(event);
    }

    //判断要不要拦截：拦截之后子控件就无法收到事件。
    //默认没有拦截 retrun false
    // 拦截 true
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.i(TAG,"Intercept---");

        return false;
        //return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG,"Touch---");

        return false;//super.onTouchEvent(event);
    }
}
