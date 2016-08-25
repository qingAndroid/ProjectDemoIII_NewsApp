package com.example.fourth.projectdemoiii_newsapp.menupage;

import android.app.Activity;
import android.app.ActivityManager;
import android.view.View;

import com.example.fourth.projectdemoiii_newsapp.bean.Categories;

/**
 * Created by Administrator on 2016/8/24.
 */
// 为什么创建BaseMenuPage基类？
// --------------------------来表示page的中间部分（除去标题栏的那部分）要显示的内容及其相关信息
// --------------------和BasePage类似，可以让NewsPage的代码和MenuPage显示的代码分离，让代码更清晰

//关于成员变量的定义
//1、view不需要让外界传进来，需要我们自己在这个类的子类中去写显示的view，而不是在NewsPage中写，是为了减少代码复杂。
//2、在初始化这个类的时候，就将要显示的数据(MenuDataInfo类)传进来
//3、初始化时，将json数据传进来，可以拿来显示在page的中间部分
//4、传Activity进来，是因为在子类中创建要显示的View的时候，需要这个参数作为上下文Context(Activity是Context的子类)
public abstract class BaseMenuPage {

    public Activity mActivity;
    public View mMenuPageView;
    public Categories.MenuDataInfo menuDataInfo;

    public BaseMenuPage(Activity mActivity, Categories.MenuDataInfo menuDataInfo) {
        this.mActivity = mActivity;
        this.menuDataInfo = menuDataInfo;//MenuDataInfo类

        mMenuPageView= initView();
    }
//-----------------子类要重写抽象方法

    //在new 这个类的时候（构造方法），就初始化要显示的视图
    public abstract View initView() ;

    //初始化要显示的数据--->这个方法是准备让外界可以调用这个类的方法
    public abstract void initData();
}
