package com.example.fourth.projectdemoiii_newsapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import fragment.ContentFragment;
import fragment.LeftMenuFragment;
//侧边栏使用导入的库，要修改名字，因为后面还要导入其他的库，不能存在同名的库
//实现了HomePage的里面的viewpager里的五个Page的分离。
////实现了从服务器获取并解析侧边栏的数据并显示在侧边栏的listview上。(使用xutils的jar)
//使用Gson去解析服务器的json字符串，优化了侧边栏显示的效果。       （使用Gson的jar）
//--------------------让本页面继承于SlidingFragmentActivity
//-左侧的slidingmenu和右侧的content正文部分是有交互的，所以选择用Fragment
public class HomeActivity extends SlidingFragmentActivity {

    private FrameLayout fl_homeactivity_content;
    public SlidingMenu slidingMenu;
    private FragmentManager fm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设定右侧主页面显示的内容
        setContentView(R.layout.activity_home);


        fl_homeactivity_content = (FrameLayout) findViewById(R.id.fl_homeactivity_content);

        //一、首先设定slidingmenu显示的内容

        //只有在slidingMenu所在的activity中才可以修改关于slidingmenu的内容及设置
        setBehindContentView(R.layout.slidingmenu);
        slidingMenu = getSlidingMenu();
        slidingMenu.setMode(SlidingMenu.LEFT);//方向：表示从左可以滑出侧边栏
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);//表示在整个屏幕上都可以将侧边栏滑出;还可以设置TOUCHMODE_MARGIN，表示从边界处才能滑出来
        slidingMenu.setBehindOffset(320);//表示右侧的content页面显示多少像素


        //二、动态引入要显示的Fragment  ContentFragment和LeftMenuFragment
        //1、获得FragmentManager
        fm = getFragmentManager();
        //2、事务开始
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        //3、用fragment替换某个控件(将frgment的内容替换content和侧边栏的内容)
        fragmentTransaction.replace(R.id.fl_homeactivity_content,new ContentFragment(),"content");//正文部分，设置tag,相当于取了别名
        fragmentTransaction.replace(R.id.fl_homeactivity_leftmenu,new LeftMenuFragment(),"leftmenu");//侧边栏部分
        //4、事务提交
        fragmentTransaction.commit();
    }

    //暴露API： 来控制slidingmenu是否可以滑出 --->让Basepage可以调用
    public void setSlidingMenuEnable(boolean enable){

        //enable = ture  可以拖动，false 无法拖动 侧边栏
        if(enable){
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        }else{
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }

    //暴露API：让sliding可以弹出或收起---->让NewsPage调用，在点击button时，可以达到这个效果
    public  void toggleSlidingMenu(){
        slidingMenu.toggle();

    }

    public LeftMenuFragment getLeftMenuFragment(){
     //因为我们是在点击新闻中心的时候，才需要显示服务器的数据，所以应该在ContentFragment中（的NewsPage部分）去拿数据;
        // 为了在ContentFragment中 拿到服务器数据，同时在ContentFragment中将数据给到LeftFragment中去显示一些数据
     //所以要 1、先在这里HomeActvity中暴露一个API，来让别人可以获得LeftFragment（只有Fragment所在的Activity才能获得Fragment）
    // 从而 2、别人(ContentFragment中的NewsPage)就可以调用LeftFragment中的API --> setMenuData，来让listview 显示数据

        //通过tag，来找到需要的fragment,在fragment实例化（new出来）的时候设置的。
        LeftMenuFragment leftmenu = (LeftMenuFragment) fm.findFragmentByTag("leftmenu");
        return leftmenu;
    }

    public  ContentFragment getContentFragment(){
        //暴露API： 拿到ContentFragment
        ContentFragment contentFragment = (fragment.ContentFragment) fm.findFragmentByTag("content");
        return contentFragment;

    }


}
