package com.example.fourth.projectdemoiii_newsapp.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;


/**
 * Created by Administrator on 2016/8/23.
 */
public class NoScrollViewPager extends ViewPager {
   //自定义的viewPager， 被禁用了滑动事件，只能通过button的点击来切换page

   //两种构造方法
    public NoScrollViewPager(Context context) {
        super(context);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    // 禁用viewpager的左右滑动事件
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;//super.onTouchEvent(ev);
    }
}
