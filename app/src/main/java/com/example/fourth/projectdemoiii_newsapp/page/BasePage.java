package com.example.fourth.projectdemoiii_newsapp.page;

import android.app.Activity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fourth.projectdemoiii_newsapp.HomeActivity;
import com.example.fourth.projectdemoiii_newsapp.R;

/**
 * Created by Administrator on 2016/8/23.
 */

//  利用分离思想，ContentFragment中viewpager中要显示的page的内容,与fragment的代码分离，
//  这样可以让代码更清晰---因为新闻中心的page是很复杂的*/
//-----------基类的功能--------------------------------
//1、要显示的5个page都继承于这个基类
//2、在这里填充page要显示的view
//3、提供一个抽象方法，让子类去实现，可以让子类去初始化自己的数据
//4、子类只需要继承并重写抽象方法initData()即可
// -------------------------------------------------

public abstract  class BasePage {

    public View mPageView;       //就是viewpager里面显示的每一个page视图
    public Activity mActivity;   //下面填充view需要用到上下文
    protected ImageButton   bt_pageview_leftbutton;
    protected ImageButton   bt_pageview_rightbutton;
    protected LinearLayout  ll_pageview_content;
    protected TextView      tv_pageview_title;

    public BasePage(Activity mActivity) {
        this.mActivity = mActivity;//在fragment中初始化类的时候 ，用
        initView();
        initData();//在这个API中去修改当前这个page的tv_pageview_title等信息。

    }

    public abstract void initData();

    //初始化page要显示的视图
    public void initView(){
        mPageView = View.inflate(mActivity, R.layout.page_content, null);
        bt_pageview_leftbutton = (ImageButton) mPageView.findViewById(R.id.bt_pageview_leftbutton);
        bt_pageview_rightbutton = (ImageButton) mPageView.findViewById(R.id.bt_pageview_rightbutton);
        ll_pageview_content = (LinearLayout) mPageView.findViewById(R.id.ll_pageview_content);
        tv_pageview_title = (TextView) mPageView.findViewById(R.id.tv_pageview_title);
    }

   public void slidingMenuEnable(boolean enable){

       //调用HomeActivity中的API，控制slidingmenu是否可以滑出
       HomeActivity homeActivity = (HomeActivity)mActivity;
       homeActivity.setSlidingMenuEnable(enable);
   }

}
