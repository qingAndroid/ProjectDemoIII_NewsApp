package com.example.fourth.projectdemoiii_newsapp.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;


/**
 * Created by Administrator on 2016/8/23.
 */
//slidingMenu  NoScrollViewPager  NewsDetail--ViewPager
//Day4  事件分发机制：
// 1、Q：事件冲突：滑动第二层viewpager时，滑动事件在slidingMenu处就被拦截了。，因为slidingMenu要将自己的侧边栏滑出来。
//A:请求父控件不要拦截（写在自定义的ViewPager控件类中。重写），则第二层viewpager的所有祖先控件都不会拦截它的滑动事件。

public class NoScrollViewPager extends ViewPager {
   //自定义的viewPager， 被禁用了滑动事件，只能通过button的点击来切换page

   //两种构造方法
    public NoScrollViewPager(Context context) {
        super(context);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    // 禁用viewpager的左右滑动事件,,
    //让在全屏左、右滑动时，slidingmenu可以滑动---去处理滑动事件
   @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;//super.onTouchEvent(ev);

        //该方法的返回值表示是否消耗该事件。
        // 返回true表示消耗。
        // 返回False表示不消耗。如果不消耗，则同一个事件序列中，当前view无法再次接受到该事件序列的后续事件。

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    //判断要不要拦截
    /*
         * This method JUST determines whether we want to intercept the motion.
         * If we return true, onMotionEvent will be called and we do the actual
         * scrolling there.
         */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;//super.onInterceptTouchEvent(ev);
    }
}
