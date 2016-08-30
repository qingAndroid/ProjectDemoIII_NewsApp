package com.example.fourth.projectdemoiii_newsapp.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2016/8/26.
 */
public class NewsMenuPageViewPager extends ViewPager {


    public NewsMenuPageViewPager(Context context) {
        super(context);
    }

    public NewsMenuPageViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //请求父控件不要拦截（写在自定义的ViewPager控件类中。重写）
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //手指右滑时，可能会将slidingmenu滑出来，

        //判断一下，当前是第几个page，
      /* int currentItem = getCurrentItem();

        if(currentItem!=0){
            //只有从第1个page之后自己处理----请求父控件不处理
            getParent().requestDisallowInterceptTouchEvent(true);
        }else{
            //第0个page(北京)不想处理，让slidingmenu处理滑动事件（让slidingmenu滑出来）
            getParent().requestDisallowInterceptTouchEvent(false);
        }
*/
        /*去往右滑时。会滑出slidingmenu
将使用事件分发机制禁止Slidingmenu滑动改为使用HomeActivity的API：

所以在page1之后的所有page:,禁止Slidingmenu滑动。
*/
        return super.dispatchTouchEvent(ev);
    }
}
