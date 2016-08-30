package com.example.fourth.projectdemoiii_newsapp.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;


/**
 * Created by Administrator on 2016/8/27.
 */
//这个类仅仅代表显示topnews的ViewPager，
public class TopNewsViewPager extends ViewPager {
    private float startx;
    private float endx;
    private float starty;
    private float endy;
    public TopNewsViewPager(Context context) {
        super(context);
    }

    public TopNewsViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        //
        switch (ev.getAction()) {

            case MotionEvent.ACTION_DOWN:

                startx = ev.getRawX();
                starty = ev.getRawY();

                break;
            case MotionEvent.ACTION_MOVE:

                endx = ev.getRawX();
                endy = ev.getRawY();

                float dx = Math.abs(endx - startx);
                float dy = Math.abs(endy - starty);

                //增加判断上滑还是下滑
                if(dx>dy){
                    //水平滑动
                    if (endx - startx > 0) {
                        //右滑{
                        if (getCurrentItem() != 0) {
                            getParent().requestDisallowInterceptTouchEvent(true);
                        }
                   /* 直接不用写，可写可不写
                   else{//左滑
                        //第一张图片不用请求不拦截，因为在NewsMenuPage中，直接设置了slidingmenu是可以滑出来的
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }*/

                    } else {
                        //左滑

                        //让最后一张图page，只有在右滑时，才请求不拦截;在左滑时，要父控件拦截
                        // ---->即让newsMenuPage的第二张page显示出来
                        //注意----左滑时，一定不能请求父控件不拦截，
                        // 因为如果左滑时，还去 请求父控件不拦截 ，
                        // 则onTouchEvent，TopNews的ViewPager消耗了，虽然他并没有下一个图片page被需要滑出来
                        //这样就导致了NewsMenuPage中ViewPager的下一个page，即第二张page会滑不出来
                    }
                }else{
                    //竖直方向  允许父控件拦截，即让Listview滑动
                    getParent().requestDisallowInterceptTouchEvent(false);
                }



        }

        return super.dispatchTouchEvent(ev);
    }
}
